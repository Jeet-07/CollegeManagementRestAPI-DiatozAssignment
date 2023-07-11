package com.diatoz.assignment.controllers;

import com.diatoz.assignment.models.dto.TeacherDTO;
import com.diatoz.assignment.models.exceptions.ResourceNotFoundException;
import com.diatoz.assignment.models.exceptions.ValidationException;
import com.diatoz.assignment.payload.request.create.TeacherCreateBody;
import com.diatoz.assignment.payload.request.updation.TeacherUpdateBody;
import com.diatoz.assignment.services.TeacherService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/teachers")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<TeacherDTO> getAllTeachers(){
        return teacherService.getAllTeachers();
    }

    @GetMapping("/teacher/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity getTeacherById(@PathVariable("id")String id) throws ResourceNotFoundException {
        TeacherDTO teacher = teacherService.getTeacherById(id);
        return setResponseData(HttpStatus.OK,null,teacher,null);
    }

    @PostMapping("/teacher/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createTeacher(@Valid @RequestBody TeacherCreateBody teacherBody, BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors())
            throw new ValidationException("Validation Error",bindingResult);

        if(teacherService.existsByUsername(teacherBody.getUsername()))
            return setResponseData(HttpStatus.BAD_REQUEST,null,null,teacherBody.getUsername()+
                    " Username already exists, Try something different");

        TeacherDTO teacher = teacherService.save(teacherBody);
        return setResponseData(HttpStatus.CREATED,"Teacher created successfully.",teacher,null);
    }

    @PutMapping("/teacher/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateTeacher(@PathVariable("id")String id,
                                        @Valid @RequestBody TeacherUpdateBody teacherBody,
                                        BindingResult bindingResult) throws ResourceNotFoundException {
        TeacherDTO updatedTeacher = teacherService.update(id,teacherBody);
        return setResponseData(HttpStatus.OK,"Teacher updated successfully",updatedTeacher,null);
    }
    @PutMapping("/teacher/{id}/enable/{enabled}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateEnableStatus(@PathVariable("id")String id,
                                             @PathVariable("enabled")boolean enabled) throws ResourceNotFoundException{
        TeacherDTO teacher = teacherService.updateEnableStatus(id,enabled);
        String message = enabled ? "Teacher enabled":"Teacher disabled";
        return setResponseData(HttpStatus.OK,message,teacher,null);
    }
    @DeleteMapping("/teacher/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteTeacher(@PathVariable("id")String id) throws ResourceNotFoundException{
        TeacherDTO teacher = teacherService.delete(id);
        return setResponseData(HttpStatus.OK,"Teacher deleted successfully",teacher,null);
    }

    private static ResponseEntity setResponseData(HttpStatus status, String message, Object data, Object errors){
        Map<String,Object> body= new LinkedHashMap<>();
        body.put("timeStamp",new Date());
        body.put("status",status.value());
        if(message!=null)body.put("message",message);
        if(data!=null)body.put("data",data);
        if(errors!=null)body.put("errors",errors);
        return ResponseEntity.status(status).body(body);
    }
}
