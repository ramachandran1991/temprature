package org.ram.test.temprature.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "temperature.sensor")
public class SensorsProps {

    private int temperaturePort=3344;
    private int humidityPort=3355;
    private double temperatureThreshold=35.0;
    private double humidityThreshold=50.0;
}
