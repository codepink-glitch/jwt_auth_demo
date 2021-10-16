package ru.codepinkglitch.jwt_auth_demo.dtos.in;

import lombok.Data;

// Класс, созданный для получения данных о сообщениях от пользователей.

@Data
public class MessageIn {
    private String name;
    private String message;
}
