package ru.codepinkglitch.jwt_auth_demo.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Data;

// Класс, созданный для предоставления пользователю токена аутентификации.

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
}
