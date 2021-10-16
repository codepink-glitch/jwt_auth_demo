package ru.codepinkglitch.jwt_auth_demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codepinkglitch.jwt_auth_demo.dtos.in.AuthenticationRequest;
import ru.codepinkglitch.jwt_auth_demo.dtos.out.AuthenticationResponse;
import ru.codepinkglitch.jwt_auth_demo.services.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    // Эндпоинт для аутентификации пользователя, пользователь предоставляет json файл с именем пользователя и паролем.
    // В ответ пользователь получает токен для последующей аутентификации.

    @PostMapping
    public ResponseEntity<AuthenticationResponse> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(authenticationService.authenticate(authenticationRequest), HttpStatus.OK);
    }
}
