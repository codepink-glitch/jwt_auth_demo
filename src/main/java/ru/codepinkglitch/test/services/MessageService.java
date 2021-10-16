package ru.codepinkglitch.test.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codepinkglitch.test.dtos.in.MessageIn;
import ru.codepinkglitch.test.dtos.out.MessageOut;
import ru.codepinkglitch.test.entities.MessageEntity;
import ru.codepinkglitch.test.repositories.MessageRepository;

import java.util.List;
import java.util.stream.Collectors;

// Класс, принимающий и обрабатывающий сообщения.

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final String HISTORY_PREFIX = "history";
    private static final Integer HISTORY_MAX_NUMS = 3;
    private static final String HISTORY_MAX_NUMS_REGEX = "\\d{1," + HISTORY_MAX_NUMS + "}";
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    // Сохранение сообщения, возвращает зарегистрированное сообщение.

    private MessageOut receiveMessage(MessageIn messageIn) {
        MessageEntity saved = messageRepository.save(objectMapper.convertValue(messageIn, MessageEntity.class));
        return objectMapper.convertValue(saved, MessageOut.class);
    }

    // Получение из базы данных последних n сообщений и возврат их в контроллер.

    private List<MessageOut> findLastFew(MessageIn messageIn) {
        int numOfMessages = Integer.parseInt(messageIn.getMessage().replaceAll("\\D+", ""));

        return messageRepository.findLastMessagesByCreatedAt(numOfMessages).stream()
                .map(x -> objectMapper.convertValue(x, MessageOut.class))
                .collect(Collectors.toList());
    }

    // Данный метод взаимодействует с контроллером.
    // Полученное сообщение проверяется на формат: сообщение или запрос последних n сообщений.
    // В случае сообщения, метод возвращает в контроллер сериализованное в json зарегистрированное сообщение.
    // В случае запроса последних n сообщений, метод возвращает в контроллер сериализованный в json список последних n сообщений.

    public String processMessage(MessageIn messageIn) {
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

}
