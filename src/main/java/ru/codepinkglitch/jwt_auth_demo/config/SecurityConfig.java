package ru.codepinkglitch.jwt_auth_demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.codepinkglitch.jwt_auth_demo.filters.JwtRequestFilter;

// Класс, конфигурирующий Spring Security

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtRequestFilter jwtRequestFilter;

    // Настройка Security.
    // Доступ к эндпоинтам /authentication для получения токена и /registration для регистрации не требует аутентификации, для остальных требуется указание токена в хэдере.
    // При каждом запросе, требующем аутентификации, указывается токен - создание сессий не требуется, в sessionManagement указывается SessionCreationPolicy.STATELESS.
    // Фильтр добавляется в цепочку фильтров перед стандартным UsernamePasswordAuthenticationFilter и до него аутентифицирует пользователя в SecurityContextHolder.

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/authentication").permitAll()
                .antMatchers("/registration").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // В проекте используется стандартный AuthenticationManager из Spring Security для аутентификации в классе AuthenticationService.
    // В прошлых версиях спринг данный бин создавался сам, теперь прописываем бин.

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Создается бин для шифрования паролей, спринг автоматически его использует.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // При указании аннотации @Bean или аналогичной (в данном случае @Component)
    // над классом фильтра (JwtRequestFilter), спринг автоматически его добавляет в цепочку фильтров.
    // Данный бин отключает автоматическое встраивание фильтра в цепочку.

    @Bean
    public FilterRegistrationBean jwtRequestFilterRegistration(JwtRequestFilter jwtRequestFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(jwtRequestFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

}
