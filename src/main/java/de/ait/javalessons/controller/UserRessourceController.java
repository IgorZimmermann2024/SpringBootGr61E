package de.ait.javalessons.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserRessourceController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Публичная страница";
    }

    @GetMapping("/private")
    public String privateEndpoint() {
        return "Это защищенная страница, доступна только после входа";
    }

    @GetMapping("/secure")
    public String secure(){
        return "Вы успешно аутентифицированы с JWT";
    }
}
