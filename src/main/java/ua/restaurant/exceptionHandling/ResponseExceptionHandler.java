package ua.restaurant.exceptionHandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            NoSuchElementException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        log.info("ResponseExceptionHandler in work");
        log.info(ex.getMessage());

        String bodyOfResponse = ex.getMessage();
        HttpHeaders header = new HttpHeaders();
        return handleExceptionInternal(ex, bodyOfResponse, header, HttpStatus.CONFLICT, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_ACCEPTABLE, ex.getLocalizedMessage(), "Invalid input.");
        log.error("handleHttpMessageNotReadable in work");
        log.error(ex.getLocalizedMessage());
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        log.error("handleMethodArgumentNotValid on work");
        log.error(errors.toString());

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }

//    @Override
//    protected ResponseEntity<Object> handleNoHandlerFoundException(
//            NoHandlerFoundException ex, HttpHeaders headers,
//            HttpStatus status, WebRequest request) {
//
//        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
//        log.info(error);
//
//        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
//        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
//    }

}

