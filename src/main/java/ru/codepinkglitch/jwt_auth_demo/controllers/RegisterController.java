package ru.codepinkglitch.jwt_auth_demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codepinkglitch.jwt_auth_demo.dtos.in.UserDetailsIn;
import ru.codepinkglitch.jwt_auth_demo.dtos.out.UserDetailsOut;
import ru.codepinkglitch.jwt_auth_demo.services.MyUserDetailsService;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegisterController {

    private final MyUserDetailsService userDetailsService;

    // Эндпоинт для регистрации пользователей, к данному эндпоинту можно получить доступ без предъявления токена.
    // Пользователь предоставляет свои данные, такие как: имя пользователя, пароль.
    // В случае успешной регистрации, система возвращает пользователю его зарегистрированные данные.

    @PostMapping
    public ResponseEntity<UserDetailsOut> register(@RequestBody UserDetailsIn userDetailsIn) {
        return new ResponseEntity<>(userDetailsService.createNew(userDetailsIn), HttpStatus.OK);
    }

}
