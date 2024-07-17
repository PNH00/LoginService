package com.spring.LoginService.mapper;

import com.spring.LoginService.dto.UserDTO;
import com.spring.LoginService.entities.User;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDTOToUser(UserDTO userDTO);
    UserDTO userToUserDTO(User user);
    List<UserDTO> userstoUserDTOs(List<User> users);
}
