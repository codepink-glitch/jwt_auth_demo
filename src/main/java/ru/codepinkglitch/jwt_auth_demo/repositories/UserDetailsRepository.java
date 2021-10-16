package ru.codepinkglitch.jwt_auth_demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codepinkglitch.jwt_auth_demo.entities.MyUserDetails;

//Класс-репозиторий для взаимодействия с базой данных.

@Repository
public interface UserDetailsRepository extends JpaRepository<MyUserDetails, Long> {

    Boolean existsMyUserDetailsByUsername(String username);

    MyUserDetails findMyUserDetailsByUsername(String username);
}
