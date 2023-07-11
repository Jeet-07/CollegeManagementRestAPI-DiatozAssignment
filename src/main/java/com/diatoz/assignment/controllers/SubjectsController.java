package com.diatoz.assignment.controllers;

import com.diatoz.assignment.models.entities.Subject;
import com.diatoz.assignment.models.exceptions.ResourceNotFoundException;
import com.diatoz.assignment.models.exceptions.ValidationException;
import com.diatoz.assignment.payload.request.create.SubjectCreateBody;
import com.diatoz.assignment.services.SubjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name="basicAuth")
public class SubjectsController {
    @Autowired
    private SubjectService service;
    @GetMapping("/subjects")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity getAllSubject(){
        List<Subject> subjects = service.getAllSubjects();
        return setResponseData(HttpStatus.OK,null,subjects,null);
    }

    @GetMapping("/subject/{subName}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity getSubByName(@PathVariable("subName")String subName)throws ResourceNotFoundException{
        if(subName.isEmpty() || subName.length()<4)
            return setResponseData(HttpStatus.BAD_REQUEST,null,null,"Subject name should contain min 4 characters");
        
        subName=subName.toUpperCase().trim();
        if(!service.existsByName(subName))
            throw new ResourceNotFoundException("No subject exists with name: "+subName);


        Subject subject = service.findByName(subName);
        return setResponseData(HttpStatus.OK,null,subject,null);

    }

    @PostMapping("/subject/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity createSubject(@Valid @RequestBody SubjectCreateBody subjectBody, BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors())
            throw new ValidationException("Validation Error",bindingResult);
        
        subjectBody.setSubName(subjectBody.getSubName().toUpperCase().trim());
        String subName = subjectBody.getSubName();
        if(service.existsByName(subName))
            return setResponseData(HttpStatus.BAD_REQUEST,null,null,"Subject "+subName+" already exists");
        else{
            Subject subject = new Subject(subName);
            subject = service.save(subject);
            return setResponseData(HttpStatus.CREATED,
                    "New subject created",
                    subject,null);
        }
    }

    @DeleteMapping("/subject/{subName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteSubject(@PathVariable("subName")String subName) throws ResourceNotFoundException {
        if(subName.isEmpty() || subName.length()<4)
            return setResponseData(HttpStatus.BAD_REQUEST,null,null,"Subject name should contain min 4 characters");

        subName = subName.toUpperCase().trim();
        if(!service.existsByName(subName))
            throw new ResourceNotFoundException("No subject exists with name: "+subName);

        Subject deletedSubject = service.deleteByName(subName);

        return setResponseData(HttpStatus.OK,"Subject deleted",deletedSubject,null);
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
