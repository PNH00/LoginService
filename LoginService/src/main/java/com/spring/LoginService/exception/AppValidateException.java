package com.spring.LoginService.exception;

import com.spring.LoginService.dto.response.AppResponse;
import lombok.Getter;

@Getter
public class AppValidateException extends RuntimeException{
    private final AppResponse<Object> appResponse;

    public AppValidateException(AppResponse<Object> appResponse) {
        this.appResponse = appResponse;
    }
}
