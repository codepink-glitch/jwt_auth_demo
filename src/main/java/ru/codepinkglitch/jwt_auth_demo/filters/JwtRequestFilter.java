package ru.codepinkglitch.jwt_auth_demo.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.codepinkglitch.jwt_auth_demo.entities.MyUserDetails;
import ru.codepinkglitch.jwt_auth_demo.services.JwtUtilService;
import ru.codepinkglitch.jwt_auth_demo.services.MyUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Класс, использующийся в качестве фильтра Spring Security.
// Наследуется от OncePerRequestFilter, таким образом, срабатывает один раз на запрос.

@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MyUserDetailsService userDetailsService;
    private final JwtUtilService jwtUtilService;


    // Метод из наследуемого класса, применяется, как только очередь в FilterChain доходит до данного фильтра.

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        // Проверяется, есть ли в запросе хэдер с токеном.
        // В случае наличия в хэдере токена, из токена извлекается имя пользователя.
        // Токен сохраняется в переменной для последующей валидации.

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtUtilService.extractUsername(token);
        }

        // Проверяется, было ли извлечено из токена имя пользователя и был ли пользователь аутентифицирован до этого.

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Данные пользователя извлекаются из базы данных по имени пользователя.

            MyUserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Проверяется валидность токена.

            if (jwtUtilService.validateToken(token, userDetails).equals(true)) {

                // В случае валидности токена, пользователь аутентифицируется в SecurityContextHolder.

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // Управление передается дальше по цепочке фильтров FilterChain.

        chain.doFilter(request, response);
    }
}
