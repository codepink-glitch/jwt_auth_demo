package ru.codepinkglitch.test.models;

import lombok.AllArgsConstructor;
import lombok.Data;

// Класс-модель, представляющий собой данные, возвращаемые после аутентификации (токен).

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
}
