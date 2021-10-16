package ru.codepinkglitch.test.entities;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;

// Класс, отображающий сущность пользователя в базе данных.

@Entity
@Data
@Table(name = "USER_DETAILS")
public class MyUserDetails implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_DETAILS_ID", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "userDetails", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MyAuthority> authorities;

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NON_EXPIRED")
    private boolean accountNonExpired;

    @Column(name = "NON_LOCKED")
    private boolean accountNonLocked;

    @Column(name = "CREDENTIALS_NON_EXPIRED")
    private boolean credentialsNonExpired;

    @Column(name = "ENABLED")
    private boolean enabled;
}
