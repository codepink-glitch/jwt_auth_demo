package ru.codepinkglitch.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codepinkglitch.test.entities.MyUserDetails;

//Класс-репозиторий для взаимодействия с базой данных.

@Repository
public interface UserDetailsRepository extends JpaRepository<MyUserDetails, Long> {

    Boolean existsMyUserDetailsByUsername(String username);

    MyUserDetails findMyUserDetailsByUsername(String username);
}
