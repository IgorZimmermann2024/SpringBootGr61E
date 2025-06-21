package de.ait.javalessons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * Конфигурация безопасности Spring Security с использованием JDBC-хранилища пользователей.
 */
@Configuration
@EnableMethodSecurity // Включает поддержку аннотаций @PreAuthorize, @Secured и др.
public class SecurityConfigJDBC {

    /**
     * Конфигурация HTTP-безопасности.
     *
     * @param http объект конфигурации безопасности HTTP.
     * @return цепочка фильтров безопасности.
     * @throws Exception в случае ошибки конфигурации.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->

                        // Разрешаем доступ без аутентификации к /public и консоли H2
                        auth.requestMatchers("/public", "/h2-console/**").permitAll()

                                // Все остальные запросы требуют аутентификации
                                .anyRequest().authenticated()
                )
                // Включаем стандартную форму логина
                .formLogin();

        // Для корректной работы H2 Console отключаем защиту от clickjacking и CSRF
        http.headers().frameOptions().disable();
        http.csrf().disable();

        return http.build();
    }

    /**
     * Создаёт сервис пользователей на основе базы данных.
     *
     * @param dataSource подключение к базе данных.
     * @return JDBC-реализация UserDetailsService.
     */
    @Bean
    public UserDetailsService users(DataSource dataSource) {
        // JdbcUserDetailsManager автоматически работает с таблицами users и authorities
        return new JdbcUserDetailsManager(dataSource);
    }

    /**
     * Кодировщик паролей, использующий BCrypt.
     *
     * @return бин PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}