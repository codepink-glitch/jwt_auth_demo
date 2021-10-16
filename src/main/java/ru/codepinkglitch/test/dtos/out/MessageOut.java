package ru.codepinkglitch.test.dtos.out;

import lombok.Data;

// Класс, созданный для предоставления данных о зарегистрированных сообщениях пользователям.

@Data
public class MessageOut {

    private Long id;
    private String name;
    private String message;
}
