package com.spring.LoginService.mapper;

import com.spring.LoginService.dto.request.UserDTORequest;
import com.spring.LoginService.dto.response.UserDTOResponse;
import com.spring.LoginService.entities.User;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDTOResponseToUser(UserDTOResponse userDTO);
    UserDTOResponse userToUserDTOResponse(User user);
    List<UserDTOResponse> userstoUserDTOResponses(List<User> users);

    User userDTORequestToUser(UserDTORequest userDTO);
    UserDTORequest userToUserDTORequest(User user);
    List<UserDTORequest> userstoUserDTORequests(List<User> users);
}
