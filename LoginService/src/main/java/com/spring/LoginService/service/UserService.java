package com.spring.LoginService.service;

import com.spring.LoginService.constant.AppConstant;
import com.spring.LoginService.dto.request.UserDTORequest;
import com.spring.LoginService.dto.response.AppResponse;
import com.spring.LoginService.dto.response.UserDTOResponse;
import com.spring.LoginService.enums.Role;
import com.spring.LoginService.exception.AppValidateException;
import com.spring.LoginService.mapper.UserMapper;
import com.spring.LoginService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import com.spring.LoginService.entities.User;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTOResponse> getUsers()
    {
        return userMapper.userstoUserDTOResponses(userRepository.findAll());
    }

    public UserDTOResponse getUser(UUID id)
    {
        return userMapper.userToUserDTOResponse(findUser(id));
    }

    public UserDTOResponse createUser(UserDTORequest userDTORequest)
    {
        User user = userMapper.userDTORequestToUser(userDTORequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userDTORequest.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);
        if(userRepository.findByEmail(user.getEmail()).isPresent()
                || userRepository.findByUserName(user.getUserName()).isPresent())
        {
            throw new AppValidateException(new AppResponse<>(
                    new Date(),
                    AppConstant.FAIL,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    AppConstant.EMAIL_ALREADY_EXIST,
                    AppConstant.CREATE_ACCOUNT_FAIL
            ));
        } else{
            try{
                return userMapper.userToUserDTOResponse(userRepository.save(user));
            }catch (RuntimeException exception){
                throw new AppValidateException(new AppResponse<>(
                        new Date(),
                        AppConstant.FAIL,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        AppConstant.INTERNAL_SERVER_ERROR,
                        AppConstant.CREATE_ACCOUNT_FAIL));
            }

        }
    }

    public UserDTOResponse updateUser(UUID id, UserDTORequest userDTO) {
        User existingUser = findUser(id);

        User user = userMapper.userDTORequestToUser(userDTO);
        user.setId(existingUser.getId());
        try {
            HashSet<String> roles = new HashSet<>();
            roles.add(Role.USER.name());
            user.setRoles(roles);
            return userMapper.userToUserDTOResponse(userRepository.save(user));
        } catch (RuntimeException exception) {
            throw new AppValidateException(new AppResponse<>(
                    new Date(),
                    AppConstant.FAIL,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    AppConstant.INTERNAL_SERVER_ERROR,
                    AppConstant.UPDATE_ACCOUNT_FAIL
            ));
        }
    }

    public void deleteUser(UUID id)
    {
        User user = findUser(id);
        userRepository.delete(user);
    }

    private User findUser(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new AppValidateException(new AppResponse<>(
                        new Date(),
                        AppConstant.FAIL,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        AppConstant.USER_DOSE_NOT_EXIST,
                        AppConstant.GET_ACCOUNT_FAIL
                ))
        );
    }
}
