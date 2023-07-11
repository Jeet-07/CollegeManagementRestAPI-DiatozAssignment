package com.diatoz.assignment.controllers;

import com.diatoz.assignment.models.exceptions.ResourceNotFoundException;
import com.diatoz.assignment.models.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    Logger logger = LoggerFactory.getLogger(ExceptionController.class);
    @ExceptionHandler(value= ResourceNotFoundException.class)
    public ResponseEntity handleResourceNotFound(ResourceNotFoundException ex, WebRequest req){
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("timeStamp",new Date());
        body.put("status", HttpStatus.NOT_FOUND);
        body.put("error",ex.getMessage());
        body.put("path",req.getDescription(false));
        logger.warn("Exception occurred : "+ex.getMessage()+" at "+req.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(value= ValidationException.class)
    public ResponseEntity handleValidationError(ValidationException exception,WebRequest req){
        List<String> messages = exception.getBindingResult().getAllErrors().stream().map(ex -> ex.getDefaultMessage()).collect(Collectors.toList());
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("timeStamp",new Date());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("error",exception.getMessage());
        body.put("path",req.getDescription(false));
        body.put("messages",messages);
        logger.warn("Exception occurred : "+exception.getMessage()+" at "+req.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(value=Exception.class)
    public ResponseEntity handleGlobalException(Exception ex,WebRequest req){
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("timeStamp",new Date());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        body.put("error",ex.getMessage());
        body.put("path",req.getDescription(false));
        logger.warn("Exception occurred : "+ex.getMessage()+" at "+req.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }



}
