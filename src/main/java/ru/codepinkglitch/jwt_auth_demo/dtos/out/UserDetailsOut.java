package ru.codepinkglitch.jwt_auth_demo.dtos.out;

import lombok.Data;

// Класс, созданный для возвращения данных о регистрации пользователя пользователям.

@Data
public class UserDetailsOut {
    private Long id;
    private String username;
}
