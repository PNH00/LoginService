package com.spring.LoginService.exception;

import com.spring.LoginService.dto.response.AppResponse;
import lombok.Getter;

@Getter
public class AppValidateException extends RuntimeException{
    private final AppResponse appResponse;

    public AppValidateException(AppResponse appResponse) {
        this.appResponse = appResponse;
    }
}
