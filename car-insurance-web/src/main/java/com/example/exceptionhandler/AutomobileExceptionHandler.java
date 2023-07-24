package com.example.exceptionhandler;

import com.example.exception.AutomobileNotFoundException;
import com.example.exception.InsuranceCriteriaMismatchException;
import com.example.exception.LicensePlateAlreadyExistsException;
import com.example.exception.VinCodeAlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AutomobileExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AutomobileNotFoundException.class)
    public ResponseEntity<Object> handleAutomobileNotFoundException(AutomobileNotFoundException exception,
                                                                    WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InsuranceCriteriaMismatchException.class)
    public ResponseEntity<Object> handleInsuranceCriteriaMismatchException(InsuranceCriteriaMismatchException exception,
                                                                           WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }

    @ExceptionHandler(LicensePlateAlreadyExistsException.class)
    public ResponseEntity<Object> handleLicensePlateAlreadyExistsException(LicensePlateAlreadyExistsException exception,
                                                                           WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(VinCodeAlreadyExistsException.class)
    public ResponseEntity<Object> handleVinCodeAlreadyExistsException(VinCodeAlreadyExistsException exception,
                                                                      WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
