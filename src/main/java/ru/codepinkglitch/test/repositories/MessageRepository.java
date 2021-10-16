package ru.codepinkglitch.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.codepinkglitch.test.entities.Message;

import java.util.List;

//Класс-репозиторий для взаимодействия с базой данных.

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Метод для получения последних n сообщений из базы данных.

    @Query(nativeQuery = true,
            value = "SELECT * FROM MESSAGE ORDER BY CREATED_AT DESC FETCH FIRST :num ROWS ONLY")
    List<Message> findLastMessagesByCreatedAt(@Param("num") Integer numOfLastMessages);
}
