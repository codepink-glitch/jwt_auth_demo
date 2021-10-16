package ru.codepinkglitch.test.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.codepinkglitch.test.entities.MyUserDetails;
import ru.codepinkglitch.test.models.AuthenticationRequest;
import ru.codepinkglitch.test.models.AuthenticationResponse;

// Класс-сервис, взаимодействующий с контроллером аутентификации.

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtilService jwtUtilService;


    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        // С помощью стандартного AuthenticationManager из Spring Security запрос аутентифицируется.
        // В случае предоставления пользователем неверных данных, выбрасывается ошибка.

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Wrong username or password.");
        }

        // Пользователь достается из базы данных, для него генерируется токен и передается в контроллер в виде класса AuthenticationResponse.
        // Контроллер сериализует данный класс в json и возвращает пользователю.

        MyUserDetails authUser = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String token = jwtUtilService.generateToken(authUser);
        return new AuthenticationResponse(token);
    }
}
