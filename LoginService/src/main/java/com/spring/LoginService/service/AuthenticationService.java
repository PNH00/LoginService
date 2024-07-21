package com.spring.LoginService.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.spring.LoginService.constant.AppConstant;
import com.spring.LoginService.dto.request.IntrospectRequest;
import com.spring.LoginService.dto.response.AppResponse;
import com.spring.LoginService.dto.request.AuthenticationRequest;
import com.spring.LoginService.dto.response.AuthenticationResponse;
import com.spring.LoginService.dto.response.IntrospectResponse;
import com.spring.LoginService.entities.User;
import com.spring.LoginService.exception.AppValidateException;
import com.spring.LoginService.repositories.UserRepository;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    private final UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest)
    {
        var user = userRepository.findByUserName(authenticationRequest.getUserName()).orElseThrow(
                () -> new AppValidateException(new AppResponse<>(
                        new Date(),
                        AppConstant.FAIL,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        AppConstant.GET_ACCOUNT_FAIL,
                        null
                ))
        );

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(),user.getPassword());
        if(!authenticated)
            throw new AppValidateException(new AppResponse<>(
                    new Date(),
                    AppConstant.FAIL,
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    AppConstant.GET_ACCOUNT_FAIL,
                    null
            ));
        var token = genarateJwtToken(userRepository.findByUserName(authenticationRequest.getUserName()).orElseThrow(
                () -> new AppValidateException(new AppResponse<>(
                        new Date(),
                        AppConstant.FAIL,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        AppConstant.GET_ACCOUNT_FAIL,
                        null
                ))));

        return AuthenticationResponse
                .builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        String token = introspectRequest.getToken();
        Date expireCheck;
        boolean verified;

        try {
            JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);

            expireCheck = signedJWT.getJWTClaimsSet().getExpirationTime();
            verified = signedJWT.verify(jwsVerifier);
        } catch (JOSEException | ParseException e) {
            throw new AppValidateException(
                    new AppResponse<>(
                            new Date(),
                            AppConstant.FAIL,
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            AppConstant.INTERNAL_SERVER_ERROR,
                            null
                    )
            );
        }

        return new IntrospectResponse(verified && expireCheck.after(new Date()));
    }


    private String genarateJwtToken(User user)
    {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("test.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope",buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader,payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token",e);
            throw new AppValidateException(
                    new AppResponse<>(
                            new Date(),
                            AppConstant.FAIL,
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                            AppConstant.INTERNAL_SERVER_ERROR,
                            null
                    )
            );
        }
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
}
