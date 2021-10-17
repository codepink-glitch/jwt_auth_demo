package ru.codepinkglitch.jwt_auth_demo.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

// Класс, отображающий сущность сообщения в базе данных.
// Поле даты createdAt используется для сортировки и возвращения последних сообщений по запросу.

@Entity
@Data
@Table(name = "MESSAGE")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MESSAGE_ID")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "USERNAME", referencedColumnName = "USERNAME")
    private MyUserDetails myUserDetails;

    @Column(name = "MESSAGE_CONTENT", nullable = false)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", updatable = false)
    @CreationTimestamp
    @Setter(value = AccessLevel.NONE)
    private Date createdAt;
}
