package de.ait.javalessons.service;

import de.ait.javalessons.properties.ExternalApiProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Сервис для обращения к внешнему API.
 * Использует RestTemplate и параметры, заданные в application.yml или application.properties.
 */
@Service
public class ExternalApiService {

    // Объект RestTemplate — основной инструмент для HTTP-запросов
    private final RestTemplate restTemplate;

    // Класс, содержащий свойства внешнего API (URL, таймаут и пр.)
    private final ExternalApiProperties externalApiProperties;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param restTemplate            бин RestTemplate для выполнения HTTP-запросов
     * @param externalApiProperties   бин, содержащий настройки внешнего API
     */
    public ExternalApiService(RestTemplate restTemplate, ExternalApiProperties externalApiProperties) {
        this.restTemplate = restTemplate;
        this.externalApiProperties = externalApiProperties;
    }

    /**
     * Выполняет GET-запрос к внешнему API и возвращает результат как строку.
     *
     * @return ответ от внешнего API в виде строки
     */
    public String callExternalApi() {
        return restTemplate.getForObject(externalApiProperties.getUrl(), String.class);
    }

    /**
     * Возвращает значение таймаута, заданное в настройках.
     *
     * @return таймаут в виде строки (например, "5000")
     */
    public String getTimeout() {
        return externalApiProperties.getTimeout();
    }
}