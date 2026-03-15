package org.ram.test.temprature.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI tempratureMonitoringAPI(){
        return new OpenAPI().info(new Info().title("Temperature Monitoring System").description("API for monitoring sensors ( temperature and humidity").version("1.0.0"));

    }
}
