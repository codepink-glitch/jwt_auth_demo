package ru.codepinkglitch.jwt_auth_demo.models;

import lombok.Data;

// Класс-модель, представляющий собой данные, которые пользователь предоставляет для аутентификации.

@Data
public class AuthenticationRequest {

    private String username;
    private String password;

}
