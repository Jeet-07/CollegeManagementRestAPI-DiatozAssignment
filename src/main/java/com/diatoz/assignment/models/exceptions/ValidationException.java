package com.diatoz.assignment.models.exceptions;

import org.springframework.validation.BindingResult;

public class ValidationException extends Exception{
    private BindingResult bindingResult;
    public ValidationException(String message,BindingResult bindingResult){
        super(message);
        this.bindingResult=bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
