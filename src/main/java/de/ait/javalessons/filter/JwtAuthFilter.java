package de.ait.javalessons.filter;

import de.ait.javalessons.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр, который проверяет наличие и валидность JWT-токена в каждом запросе.
 * Если токен валиден — устанавливает пользователя в SecurityContext.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils; // Утилита для работы с JWT

    @Autowired
    private UserDetailsService userDetailsService; // Сервис для загрузки данных пользователя

    /**
     * Метод выполняется один раз на каждый HTTP-запрос.
     * Проверяет заголовок Authorization, валидирует токен, извлекает пользователя и
     * устанавливает аутентификацию в контекст безопасности.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Получаем заголовок Authorization
        String authHeader = request.getHeader("Authorization");

        // Проверяем, начинается ли заголовок с "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // Извлекаем токен (без префикса "Bearer ")
            String token = authHeader.substring(7);

            // Проверяем валидность токена
            if (jwtUtils.validateToken(token)) {

                // Извлекаем имя пользователя из токена
                String username = jwtUtils.extractUsername(token);

                // Загружаем пользователя из базы или другого источника
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Создаём объект аутентификации с правами пользователя
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Устанавливаем объект аутентификации в контекст безопасности Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Передаём управление дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }
}