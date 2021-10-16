package ru.codepinkglitch.jwt_auth_demo.dtos.in;

import lombok.Data;

//Класс, созданный для получения данных регистрации от пользователей.

@Data
public class UserDetailsIn {
    private String username;
    private String password;
}
