package com.spring.LoginService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTOResponse {

    private UUID id;
    private String email;
    private String userName;
    private Set<String> roles;
}
