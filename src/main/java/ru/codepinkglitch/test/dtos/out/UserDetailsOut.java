package ru.codepinkglitch.test.dtos.out;

import lombok.Data;

// Класс, созданный для возвращения данных о регистрации пользователя пользователям.

@Data
public class UserDetailsOut {
    private Long id;
    private String username;
}
