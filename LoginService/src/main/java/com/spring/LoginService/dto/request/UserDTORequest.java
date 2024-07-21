package com.spring.LoginService.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTORequest {

    private UUID id;
    private String email;
    private String userName;
    private String password;
    private Set<String> roles;
}