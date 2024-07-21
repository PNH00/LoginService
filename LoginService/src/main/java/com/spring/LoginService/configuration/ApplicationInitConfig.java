package com.spring.LoginService.configuration;

import com.spring.LoginService.entities.User;
import com.spring.LoginService.enums.Role;
import com.spring.LoginService.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){

        return args -> {
            if (userRepository.findByUserName("admin").isEmpty())
            {
//                userRepository.delete(userRepository.findByUserName("admin").get());
                HashSet<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
                User user = User.builder()
                        .userName("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .email("please set the email for admin")
                        .build();

                userRepository.save(user);
                log.warn("Default admin had been created! change the password, email, and some value (Optional)");
            }
        };
    }
}
