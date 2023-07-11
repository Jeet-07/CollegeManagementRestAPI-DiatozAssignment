package com.diatoz.assignment.controllers;

import com.diatoz.assignment.models.exceptions.ResourceNotFoundException;
import com.diatoz.assignment.models.dto.AdminDTO;
import com.diatoz.assignment.models.exceptions.ValidationException;
import com.diatoz.assignment.payload.request.create.AdminCreateBody;
import com.diatoz.assignment.payload.request.updation.AdminUpdateBody;
import com.diatoz.assignment.services.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name="basicAuth")
public class AdminController {
    @Autowired
    private AdminService adminService;

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAdminById(@PathVariable("id")String id) throws ResourceNotFoundException {

        AdminDTO admin = adminService.getAdminById(id);
        return setResponseData(HttpStatus.OK,null,admin,null);
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAllAdmins(){
        List<AdminDTO> admins = adminService.getAllAdmins();
        return setResponseData(HttpStatus.OK,null,admins,null);
    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity registerAdmin(@Valid @RequestBody AdminCreateBody adminBody, BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException("Validation Error",bindingResult);
        }

        if(adminService.existsByUsername(adminBody.getUsername()))
            return setResponseData(HttpStatus.BAD_REQUEST,
                    null,
                    null,
                    adminBody.getUsername()+" : Username already in use, Try Something different.");

        AdminDTO savedAdmin = adminService.save(adminBody);
        return setResponseData(HttpStatus.CREATED,
                "Admin created successfully",
                savedAdmin,null);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateAdminById(@PathVariable("id")String id, @Valid @RequestBody AdminUpdateBody adminBody,BindingResult bindingResult) throws ValidationException, ResourceNotFoundException {
        if(bindingResult.hasErrors()){
            throw new ValidationException("Validation Error",bindingResult);
        }

        AdminDTO savedAdmin = adminService.getAdminById(id);

        if(!adminBody.getUsername().equals(savedAdmin.getUsername()) &&
                adminService.existsByUsername(adminBody.getUsername()))
            return setResponseData(HttpStatus.BAD_REQUEST,
                    null,
                    null,
                    adminBody.getUsername()+" : Username already in use, Try Something different.");

        AdminDTO updatedAdmin = adminService.update(id,adminBody);
        return setResponseData(HttpStatus.OK,
                "Admin updated successfully",
                updatedAdmin,null);
    }

    @PutMapping("/admin/{id}/enable/{enabled}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateEnableStatus(@PathVariable("id")String id,
                                             @PathVariable("enabled")boolean enabled) throws ResourceNotFoundException{
        AdminDTO admin = adminService.updateEnableStatus(id,enabled);
        String message = enabled ? "Admin enabled":"Admin disabled";
        return setResponseData(HttpStatus.OK,message,admin,null);
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteAdminById(@PathVariable("id")String id) throws ResourceNotFoundException {
        AdminDTO deletedAdmin = adminService.deleteById(id);
        return setResponseData(HttpStatus.OK,
                    "Admin deleted successfully",
                    deletedAdmin,null);
    }

    private static ResponseEntity setResponseData(HttpStatus status,String message,Object data,Object errors){
        Map<String,Object> body= new LinkedHashMap<>();
        body.put("timeStamp",new Date());
        body.put("status",status.value());
        if(message!=null)body.put("message",message);
        if(data!=null)body.put("data",data);
        if(errors!=null)body.put("errors",errors);
        return ResponseEntity.status(status).body(body);
    }

}
