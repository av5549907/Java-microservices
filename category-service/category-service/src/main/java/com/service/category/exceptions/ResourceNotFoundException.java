package com.service.category.exceptions;


public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String s){
        super(s);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}