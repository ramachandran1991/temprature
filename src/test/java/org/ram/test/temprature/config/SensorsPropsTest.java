package org.ram.test.temprature.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class SensorsPropsTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(SensorsProps.class);

    @Test
    void shouldLoadDefaultValues() {
        contextRunner.run(context -> {
            SensorsProps props = context.getBean(SensorsProps.class);
            assertThat(props.getTemperaturePort()).isEqualTo(3344);
            assertThat(props.getHumidityPort()).isEqualTo(3355);
            assertThat(props.getTemperatureThreshold()).isEqualTo(35.0);
            assertThat(props.getHumidityThreshold()).isEqualTo(50.0);
        });
    }

    @Test
    void shouldLoadCustomValues() {
        contextRunner
                .withPropertyValues(
                        "temperature.sensor.temperaturePort=4000",
                        "temperature.sensor.humidityPort=4001",
                        "temperature.sensor.temperatureThreshold=40.0",
                        "temperature.sensor.humidityThreshold=60.0"
                )
                .run(context -> {
                    SensorsProps props = context.getBean(SensorsProps.class);
                    assertThat(props.getTemperaturePort()).isEqualTo(4000);
                    assertThat(props.getHumidityPort()).isEqualTo(4001);
                    assertThat(props.getTemperatureThreshold()).isEqualTo(40.0);
                    assertThat(props.getHumidityThreshold()).isEqualTo(60.0);
                });
    }
}
