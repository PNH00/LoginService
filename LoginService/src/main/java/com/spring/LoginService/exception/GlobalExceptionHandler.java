package com.spring.LoginService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.spring.LoginService.dto.response.AppResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    @ExceptionHandler(AppValidateException.class)
    public ResponseEntity<AppResponse<Object>> handleMenuValidationException(AppValidateException ex) {
        AppResponse<Object> appResponse = ex.getAppResponse();
        return new ResponseEntity<>(appResponse, HttpStatus.valueOf(appResponse.getCode()));
    }
}
