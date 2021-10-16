package ru.codepinkglitch.jwt_auth_demo.dtos.in;

import lombok.Data;

// Класс, созданный для получения данных аутентификации пользователя.

@Data
public class AuthenticationRequest {

    private String username;
    private String password;

}
