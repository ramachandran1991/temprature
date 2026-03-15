package org.ram.test.temprature.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OpenAPIConfigurationTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void shouldCreateOpenAPIBean() {
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("Temperature Monitoring System");
        assertThat(openAPI.getInfo().getDescription()).isEqualTo("API for monitoring sensors ( temperature and humidity");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0.0");
    }
}
