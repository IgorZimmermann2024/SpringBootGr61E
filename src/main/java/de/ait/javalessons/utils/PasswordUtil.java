package de.ait.javalessons.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    public static void main(String[] args) {
        String password = "admin";
        System.out.println(new BCryptPasswordEncoder().encode(password));
    }
}
