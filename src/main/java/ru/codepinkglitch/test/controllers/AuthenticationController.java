package ru.codepinkglitch.test.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codepinkglitch.test.models.AuthenticationRequest;
import ru.codepinkglitch.test.models.AuthenticationResponse;
import ru.codepinkglitch.test.services.AuthenticationService;

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
