package ru.codepinkglitch.jwt_auth_demo.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.codepinkglitch.jwt_auth_demo.entities.MessageEntity;

import java.util.List;

//Класс-репозиторий для взаимодействия с базой данных.

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    // Метод для получения последних n сообщений из базы данных.

//    @Query(nativeQuery = true,
//    value = "SELECT * FROM MESSAGE ORDER BY CREATED_AT DESC FETCH FIRST :num ROWS ONLY")
    @Query(value = "select m from MessageEntity m order by createdAt desc")
    List<MessageEntity> findLastMessagesByCreatedAt(Pageable limit);


}
