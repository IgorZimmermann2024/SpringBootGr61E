package de.ait.javalessons.controller;

import de.ait.javalessons.service.ExternalApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class ApiController {

   /** private final ExternalApiService externalApiService;

    public ApiController(ExternalApiService externalApiService){
        this.externalApiService = externalApiService;
    }

    @GetMapping("/call")
    public String callExternalApi(){
        return "Response: " + externalApiService.callExternalApi();
    }

    @GetMapping("/timeout")
    public String getTimeout(){
        return "Response: " + externalApiService.getTimeout();
    }


    */

}
