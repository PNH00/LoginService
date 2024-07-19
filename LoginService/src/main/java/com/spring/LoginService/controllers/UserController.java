package com.spring.LoginService.controllers;

import com.spring.LoginService.constant.AppConstant;
import com.spring.LoginService.dto.request.UserDTORequest;
import com.spring.LoginService.dto.response.AppResponse;
import com.spring.LoginService.dto.response.UserDTOResponse;
import com.spring.LoginService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public AppResponse<UserDTOResponse> createUser(@RequestBody UserDTORequest userDTORequest)
    {
        return new AppResponse<>(
                new Date(),
                AppConstant.TRUE,
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                AppConstant.CREATE_ACCOUNT_SUCCESSFULLY,
                userService.createUser(userDTORequest)
        );
    }

    @GetMapping
    public AppResponse<List<UserDTOResponse>> getUsers()
    {
        return new AppResponse<>(
                new Date(),
                AppConstant.TRUE,
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                AppConstant.GET_ACCOUNT_SUCCESSFULLY,
                userService.getUsers()
        );
    }

    @GetMapping("/{id}")
    public AppResponse<UserDTOResponse> getUser(@PathVariable UUID id)
    {
        return new AppResponse<>(
                new Date(),
                AppConstant.TRUE,
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                AppConstant.GET_ACCOUNT_SUCCESSFULLY,
                userService.getUser(id)
        );
    }

    @PutMapping("/{id}")
    public AppResponse<UserDTOResponse> updateUser(@PathVariable UUID id, @RequestBody UserDTORequest userDTORequest)
    {
        return new AppResponse<>(
                new Date(),
                AppConstant.TRUE,
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                AppConstant.UPDATE_ACCOUNT_SUCCESSFULLY,
                userService.updateUser(id,userDTORequest)
        );
    }

    @DeleteMapping("/{id}")
    public AppResponse<?> deleteUser(@PathVariable UUID id)
    {
        userService.deleteUser(id);
        return new AppResponse<>(
                new Date(),
                AppConstant.TRUE,
                HttpStatus.NO_CONTENT.value(),
                HttpStatus.NO_CONTENT.getReasonPhrase(),
                AppConstant.DELETE_ACCOUNT_SUCCESSFULLY,
                null
        );
    }
}
