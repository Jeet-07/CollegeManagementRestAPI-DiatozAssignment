package com.diatoz.assignment.controllers;

import com.diatoz.assignment.models.dto.StudentDTO;
import com.diatoz.assignment.models.exceptions.ResourceNotFoundException;
import com.diatoz.assignment.models.exceptions.ValidationException;
import com.diatoz.assignment.payload.request.create.StudentCreateBody;
import com.diatoz.assignment.payload.request.create.SubjectDetailsBody;
import com.diatoz.assignment.payload.request.updation.StudentUpdateBody;
import com.diatoz.assignment.services.StudentService;
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
public class StudentController {
    @Autowired private StudentService service;

    @GetMapping("/students")
    public ResponseEntity getAllStudents(){
        List<StudentDTO> studentLists= service.getAllStudents();
        return setResponseData(HttpStatus.OK,null,studentLists,null);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity getStudentById(@PathVariable("id")String id) throws ResourceNotFoundException {
        StudentDTO student = service.getStudentById(id);
        return setResponseData(HttpStatus.OK,null,student,null);
    }

    @PostMapping("/student/create")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity createStudent(@Valid @RequestBody StudentCreateBody studentBody, BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors())
            throw new ValidationException("Validation Error",bindingResult);

        StudentDTO student = service.create(studentBody);
        return setResponseData(HttpStatus.CREATED,"Student created successfully",student,null);
    }

    @PutMapping("/student/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity updateStudent(@PathVariable("id")String id,
                                        @Valid
                                        @RequestBody
                                        StudentUpdateBody studentBody,
                                        BindingResult bindingResult) throws ResourceNotFoundException, ValidationException {
        if(bindingResult.hasErrors())
            throw new ValidationException("Validation Error",bindingResult);

        StudentDTO updatedStudent = service.update(id,studentBody);
        return setResponseData(HttpStatus.OK,"Student updated successfully",updatedStudent,null);
    }

    @DeleteMapping("/student/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity deleteStudent(@PathVariable("id")String id) throws ResourceNotFoundException{
        StudentDTO deletedStudent = service.deleteById(id);
        return setResponseData(HttpStatus.OK,"Student deleted successfully",deletedStudent,null);
    }

    @PutMapping("/student/{id}/enable/{enabled}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity updateEnableStatus(@PathVariable("id")String id,@PathVariable("enabled")boolean enabled) throws ResourceNotFoundException{
        StudentDTO updatedStudent =service.updateEnableStatus(id,enabled);
        String message = enabled ? "Student enabled":"Student disabled";
        return setResponseData(HttpStatus.OK,message,updatedStudent,null);
    }

    @PostMapping("/student/{id}/addMarks")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity addSubjectMarks(@PathVariable("id")String id,
                                          @Valid
                                          @RequestBody
                                          SubjectDetailsBody detailsBody,
                                          BindingResult bindingResult) throws ValidationException,ResourceNotFoundException{
        if(bindingResult.hasErrors())
            throw new ValidationException("Validation Error",bindingResult);

        StudentDTO student = service.addSubjectMarks(id,detailsBody);
        return setResponseData(HttpStatus.OK,"Added subject marks",student,null);
    }

    @PostMapping("/student/createMultiple")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity createMultipleStudents(@Valid @RequestBody List<StudentCreateBody> studentBodyList,BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors())
            throw new ValidationException("validation Error",bindingResult);

        List<StudentDTO> studentslist = service.createAllStudents(studentBodyList);
        return setResponseData(HttpStatus.OK,"All students added successfully",studentslist,null);
    }

    @PostMapping("/student/{id}/addMultipleSubject")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity addMultipleSubjects(@PathVariable("id")String id,
                                              @Valid
                                              @RequestBody
                                              List<SubjectDetailsBody> subjectsDetails,
                                              BindingResult bindingResult) throws ValidationException,ResourceNotFoundException{
        if(bindingResult.hasErrors())
            throw new ValidationException("Validation Error",bindingResult);

        StudentDTO student = service.addALlSubjectDetails(id,subjectsDetails);
        return setResponseData(HttpStatus.OK,"Subjects added successfully",student,null);
    }

    public ResponseEntity removeSubject(@PathVariable("id")String id,@PathVariable("subName")String subName) throws ResourceNotFoundException {
        StudentDTO student = service.removeSubject(id,subName);
        return setResponseData(HttpStatus.OK,"Subject removed successfully",student,null);
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
