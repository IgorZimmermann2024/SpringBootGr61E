package de.ait.javalessons.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Утилита для работы с JWT-токенами:
 * - генерация токена
 * - извлечение username
 * - валидация токена
 */
@Component
@Slf4j
public class JwtUtils {

    /**
     * Секретный ключ, используемый для подписи токена.
     * Должен быть как минимум 256 бит (32 символа ASCII для HS256).
     */
    private final String jwtSecret = "supersecretkeysupersecretkey12345678";

    /**
     * Срок действия токена в миллисекундах (здесь 2.4 часа).
     * Можно увеличить при необходимости (например, 86400000 = 24 ч).
     */
    private final long jwtExpirationMs = 8640000;

    /**
     * Секретный ключ, сгенерированный из строки `jwtSecret`.
     * Используется для подписи и валидации токенов.
     */
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    /**
     * Генерация JWT-токена на основе имени пользователя.
     *
     * @param username имя пользователя
     * @return строка токена
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Сохраняем имя пользователя в subject
                .setIssuedAt(new Date()) // Время создания
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Время окончания действия
                .signWith(key, SignatureAlgorithm.HS256) // Подпись токена алгоритмом HS256 и ключом
                .compact();
    }

    /**
     * Извлекает имя пользователя из токена.
     *
     * @param token JWT-токен
     * @return subject (username)
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Проверяет корректность токена: подпись, структура и срок действия.
     *
     * @param token JWT-токен
     * @return true если токен валиден, иначе false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // Бросает исключения при невалидном токене
            log.info("Token is valid");
            return true;
        } catch (JwtException exception) {
            log.error("Invalid JWT token", exception);
            return false;
        }
    }
}