package com.murilonerdx.gerenciador.controller;

import com.murilonerdx.gerenciador.dto.AccountCredentialDTO;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.response.LoginTokenResponse;
import com.murilonerdx.gerenciador.security.JwtTokenProvider;
import com.murilonerdx.gerenciador.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Api(tags = "AuthController")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserService service;

    @Operation(summary = "Authenticates a user and returns a token")
    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/signin", produces = { "application/json", "application/xml", "application/x-yaml" },
            consumes = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity signin(@RequestBody AccountCredentialDTO data) {
        try {
            String email = data.getEmail();
            String password = data.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            User userDetails = (User) service.loadUserByUsername(email);

            var token = "";

            if (userDetails != null) {
                token = tokenProvider.createToken(email, userDetails.getRoles());
            } else {
                throw new UsernameNotFoundException("E-mail " + email + " not found!");
            }

            return ok(new LoginTokenResponse(email, token));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }
}
