package ru.codepinkglitch.test.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.codepinkglitch.test.entities.MyUserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// Класс для операций с токенами (создание, валидация, извлечение данных).
// Используется сторонняя библиотека jjwt.

@Service
public class JwtUtilService {

    private static final Long EXPIRATION_PERIOD = 1000 * 60 * 60L;
    private final String key;

    // Конструктор для инициализации ключа, использующегося при генерации токенов.
    // Ключ извлекается из application.yml

    @Autowired
    public JwtUtilService(@Value("${runtime_variables.secret_key}") String key) {
        this.key = key;
    }

    // Вспомогательный метод для создания токена, используется сторонняя библиотека.
    // В токен записывается имя пользователя, дата создания и дата истечения действия токена.

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_PERIOD))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // Метод для генерации токена.
    // Никакая дополнительная информация, кроме имени пользователя,
    // для создания токена не используется (Передается пустая карта).

    public String generateToken(MyUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // Извлечение всех параметров, записанных в токен.

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    // Извлечение конкретного параметра, записанных в токен.

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Извлечение имени пользователя из токена.

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Извлечение времени истечения токена.

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Проверка на действительность токена по времени.

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Валидация токена.

    public Boolean validateToken(String token, MyUserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && isTokenExpired(token).equals(false);
    }

}
