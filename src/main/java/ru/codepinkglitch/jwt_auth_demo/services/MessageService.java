package ru.codepinkglitch.jwt_auth_demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.codepinkglitch.jwt_auth_demo.dtos.in.MessageIn;
import ru.codepinkglitch.jwt_auth_demo.dtos.out.MessageOut;
import ru.codepinkglitch.jwt_auth_demo.entities.MessageEntity;
import ru.codepinkglitch.jwt_auth_demo.entities.MyUserDetails;
import ru.codepinkglitch.jwt_auth_demo.repositories.MessageRepository;
import ru.codepinkglitch.jwt_auth_demo.repositories.UserDetailsRepository;

import java.util.List;
import java.util.stream.Collectors;

// Класс-сервис, взаимодействующий с контроллером, принимающим сообщения.

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final String HISTORY_PREFIX = "history";
    private static final Integer HISTORY_MAX_NUMS = 3;
    private static final String HISTORY_MAX_NUMS_REGEX = "\\d{1," + HISTORY_MAX_NUMS + "}";
    private final MessageRepository messageRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final ObjectMapper objectMapper;
    private final JwtUtilService jwtUtilService;

    // Сохранение сообщения в бд, возвращает зарегистрированное сообщение.

    private MessageOut receiveMessage(MessageIn messageIn) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessage(messageIn.getMessage());
        MyUserDetails messageSender = userDetailsRepository.findMyUserDetailsByUsername(messageIn.getName());
        messageEntity.setMyUserDetails(messageSender);
        MessageEntity saved = messageRepository.save(messageEntity);
        return convertValueFromEntityToDto(saved);
    }

    // Получение из базы данных последних n сообщений и возврат их в контроллер.

    private List<MessageOut> findLastFew(MessageIn messageIn) {
        int numOfMessages = Integer.parseInt(messageIn.getMessage().replaceAll("\\D+", ""));
        Pageable pageable = PageRequest.of(0, numOfMessages);
        return messageRepository.findLastMessagesByCreatedAt(pageable).stream()
                .map(this::convertValueFromEntityToDto)
                .collect(Collectors.toList());
    }

    // Данный метод взаимодействует с контроллером.
    // Полученное сообщение проверяется на формат: сообщение или запрос последних n сообщений.
    // В случае сообщения, метод возвращает в контроллер сериализованное в json зарегистрированное сообщение.
    // В случае запроса последних n сообщений, метод возвращает в контроллер сериализованный в json список последних n сообщений.

    public String processMessage(MessageIn messageIn, String token) {
        checkUser(messageIn, token);
        String[] split = messageIn.getMessage().split(" ");
        try {
            return objectMapper.writeValueAsString(
                    split.length == 2 && split[0].equalsIgnoreCase(HISTORY_PREFIX) && split[1].matches(HISTORY_MAX_NUMS_REGEX) ?
                            findLastFew(messageIn) : receiveMessage(messageIn)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing message.");
        }
    }

    private void checkUser(MessageIn messageIn, String token){
        if(!jwtUtilService.extractUsername(token.substring(6)).equals(messageIn.getName())){
            throw new RuntimeException("Name don't matches.");
        }
    }

    private MessageOut convertValueFromEntityToDto(MessageEntity messageEntity){
        MessageOut messageOut = new MessageOut();
        messageOut.setMessage(messageEntity.getMessage());
        messageOut.setId(messageEntity.getId());
        messageOut.setName(messageEntity.getMyUserDetails().getUsername());
        return messageOut;
    }

}
