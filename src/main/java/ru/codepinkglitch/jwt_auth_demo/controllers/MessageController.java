package ru.codepinkglitch.jwt_auth_demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codepinkglitch.jwt_auth_demo.dtos.in.MessageIn;
import ru.codepinkglitch.jwt_auth_demo.services.MessageService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;


    // Эндпоинт для регистрации сообщений / получения последних n сообщений.
    // Пользователь отравляет сообщение в виде json файла с указанием имени пользователя и сообщением.
    // В ответ система возвращает зарегистрированное сообщение.
    // К этому эндпоинту пользователь может получить доступ только в случае предъявления токена, полученного в эндпоинте /authentication, в хэдере запроса.
    // В случае, если сообщение имеет вид: history n:Integer, система возвращает пользователю список последних n сообщений, зарегестрированных в системе.

    @PostMapping
    public ResponseEntity<String> processMessage(@RequestBody MessageIn messageIn) {
        return new ResponseEntity<>(messageService.processMessage(messageIn), HttpStatus.OK);
    }
}
