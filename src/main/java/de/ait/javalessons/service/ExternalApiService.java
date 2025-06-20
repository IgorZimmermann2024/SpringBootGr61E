package de.ait.javalessons.service;

import de.ait.javalessons.properties.ExternalApiProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private final RestTemplate restTemplate;

    private final ExternalApiProperties externalApiProperties;

    public ExternalApiService(RestTemplate restTemplate, ExternalApiProperties externalApiProperties) {
        this.restTemplate = restTemplate;
        this.externalApiProperties = externalApiProperties;
    }


    public String callExternalApi() {
        return restTemplate.getForObject(externalApiProperties.getUrl(), String.class);
    }

    public String getTimeout() {
        return externalApiProperties.getTimeout();
    }

}
