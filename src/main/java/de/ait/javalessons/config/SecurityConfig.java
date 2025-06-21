package de.ait.javalessons.config;

import de.ait.javalessons.filter.JwtAuthFilter; // Фильтр, который будет проверять JWT токены
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация Spring Security с использованием JWT-токенов.
 */
@Configuration
@EnableMethodSecurity // Поддержка аннотаций безопасности на уровне методов: @PreAuthorize и т.п.
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // Через конструктор внедряется фильтр, который будет перехватывать запросы и проверять JWT
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Главная конфигурация цепочки фильтров безопасности.
     *
     * @param http конфигуратор HTTP-безопасности
     * @return построенная SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth
                                // Разрешаем доступ без аутентификации к login и public-эндпоинтам
                                .requestMatchers("/auth/login", "/api/public", "/h2-console/**").permitAll()
                                // Все остальные эндпоинты требуют аутентификации
                                .anyRequest().authenticated()
                )
                // Устанавливаем политику управления сессией — без состояния (для REST API)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Отключаем защиту от CSRF и запрет на iframe (для H2 консоли)
        http.csrf().disable();
        http.headers().frameOptions().disable();

        // Добавляем фильтр, который будет обрабатывать JWT до стандартного UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Получаем AuthenticationManager из конфигурации Spring Security.
     *
     * @param configuration конфигурация аутентификации
     * @return бин AuthenticationManager
     * @throws Exception если не удаётся получить менеджер
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Кодировщик паролей на основе BCrypt.
     * Используется при регистрации и проверке паролей.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}