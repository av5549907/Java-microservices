package com.service.course.exceptions;

import com.service.course.dtos.CustomMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<CustomMessage> handleResourceNotFound(ResourceNotFoundException resourceNotFoundException){
        CustomMessage customMessage=new CustomMessage();
        customMessage.setMessage(resourceNotFoundException.getMessage());
        customMessage.setSuccess(false);
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(customMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationError(MethodArgumentNotValidException ex){

        Map<String,String> errors=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String fieldName=((FieldError)(error)).getField();
            String message=error.getDefaultMessage();
            errors.put(fieldName,message);
        });
        return  new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(AuthorizationDeniedException.class)
//    public ResponseEntity<CustomMessage> handleResourceError(AuthorizationDeniedException ex){
//        CustomMessage customMessage=new CustomMessage();
//        customMessage.setSuccess(false);
//        customMessage.setMessage(ex.getMessage());
//        return new ResponseEntity<>(customMessage,HttpStatus.UNAUTHORIZED);
//
//    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomMessage> handleResourceError(AccessDeniedException ex){
        CustomMessage customMessage=new CustomMessage();
        customMessage.setSuccess(false);
        customMessage.setMessage(ex.getMessage());
        return new ResponseEntity<>(customMessage,HttpStatus.UNAUTHORIZED);

    }
}