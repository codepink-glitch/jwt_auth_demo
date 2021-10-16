package ru.codepinkglitch.jwt_auth_demo.entities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import ru.codepinkglitch.jwt_auth_demo.enums.Role;

import javax.persistence.*;

// Класс, отображающий права пользователей в системе. Является вложенной сущностью класса пользователя.
// Используется в Spring Security для разграничения прав пользователей.
// В системе предусмотрена всего одна роль - пользователь, присваивается всем пользователям.

@Entity
@Data
@Table(name = "AUTHORITY")
public class MyAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTHORITY_NAME", nullable = false)
    private Role authority;

    @ManyToOne
    @JoinColumn(name = "USER_DETAILS_ID", nullable = false)
    private MyUserDetails userDetails;

    @Override
    public String getAuthority() {
        return authority.name();
    }
}
