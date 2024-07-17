package com.spring.LoginService.controllers;

import com.spring.LoginService.constant.AppConstant;
import com.spring.LoginService.dto.request.IntrospectRequest;
import com.spring.LoginService.dto.response.AppResponse;
import com.spring.LoginService.dto.request.AuthenticationRequest;
import com.spring.LoginService.dto.response.AuthenticationResponse;
import com.spring.LoginService.dto.response.IntrospectResponse;
import com.spring.LoginService.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/log-in")
    public AppResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest)
    {
        AuthenticationResponse success = authenticationService.authenticate(authenticationRequest);
        return new AppResponse<>(
                new Date(),
                AppConstant.TRUE,
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                AppConstant.LOGIN_SUCCESSFULLY,
                success
        );
    }

    @PostMapping("/introspect")
    public AppResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest introspectRequest)
    {
        IntrospectResponse success = authenticationService.introspect(introspectRequest);
        return success.isValidate()
                ? new AppResponse<>(
                new Date(),
                AppConstant.TRUE,
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                AppConstant.LOGIN_SUCCESSFULLY,
                success
        )
        : new AppResponse<>(
            new Date(),
            AppConstant.FAIL,
            HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
            AppConstant.GET_ACCOUNT_FAIL,
            success
    );
    }
}
