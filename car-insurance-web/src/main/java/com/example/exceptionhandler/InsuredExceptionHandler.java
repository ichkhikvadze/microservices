package com.example.exceptionhandler;

import com.example.exception.InsuredNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class InsuredExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InsuredNotFoundException.class)
    public ResponseEntity<Object> handleInsuredNotFoundException(InsuredNotFoundException exception,
                                                                 WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
