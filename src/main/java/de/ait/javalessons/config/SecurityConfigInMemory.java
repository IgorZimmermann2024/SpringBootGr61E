package de.ait.javalessons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурационный класс для настройки безопасности Spring Security.
 */
@Configuration
@EnableMethodSecurity // Включает поддержку аннотаций @PreAuthorize, @Secured и др. на уровне методов.
public class SecurityConfigInMemory {

    /**
     * Конфигурация фильтра безопасности.
     *
     * @param http объект конфигурации безопасности HTTP.
     * @return настроенная цепочка фильтров безопасности.
     * @throws Exception в случае ошибки конфигурации.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->

                // Разрешаем доступ без аутентификации только к URL /public
                auth.requestMatchers(("/public")).permitAll()

                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
        );

        // Собираем и возвращаем цепочку фильтров безопасности
        return http.build();
    }

    /**
     * Настраивает пользователей и роли в памяти.
     *
     * @return объект UserDetailsService с предопределёнными пользователями.
     */
    @Bean
    public UserDetailsService userDetailsService() {

        // Создаем пользователя с логином "user", паролем "password" и ролью "USER"
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("password")) // Пароль кодируется с помощью BCrypt
                .roles("USER")
                .build();

        // Создаем пользователя с логином "admin", паролем "admin" и ролью "ADMIN"
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        // Возвращаем менеджер пользователей, работающий с памятью (без базы данных)
        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * Определяет и предоставляет бин кодировщика паролей.
     *
     * @return экземпляр BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Используем BCrypt — безопасный алгоритм хеширования паролей
        return new BCryptPasswordEncoder();
    }

}