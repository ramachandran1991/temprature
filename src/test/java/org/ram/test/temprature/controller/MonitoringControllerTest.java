package org.ram.test.temprature.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ram.test.temprature.model.SensorMeasurement;
import org.ram.test.temprature.model.SensorType;
import org.ram.test.temprature.warehouse.WarehouseService;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoringControllerTest {

    @Mock
    private WarehouseService warehouseService;

    private MonitoringController monitoringController;

    @BeforeEach
    void setUp() {
        monitoringController = new MonitoringController(warehouseService);
    }

    @Test
    void shouldReturnLatestMeasurements() {
        // Given
        List<SensorMeasurement> measurements = List.of(
                createTemperatureMeasurement(25.0),
                createHumidityMeasurement(45.0)
        );
        when(warehouseService.getAllMeasurements()).thenReturn(measurements);

        // When
        ResponseEntity<List<SensorMeasurement>> response = monitoringController.getLatestMeasurements();

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getSensorType()).isEqualTo(SensorType.TEMPERATURE);
        assertThat(response.getBody().get(1).getSensorType()).isEqualTo(SensorType.HUMIDITY);
        verify(warehouseService, times(1)).getAllMeasurements();
    }

    @Test
    void shouldAddMeasurementSuccessfully() {
        // Given
        SensorMeasurement measurement = createTemperatureMeasurement(30.0);
        doNothing().when(warehouseService).processMeasurements(measurement);

        // When
        ResponseEntity<String> response = monitoringController.addMeasurement(measurement);

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo("Measurement added and processed successfully");
        verify(warehouseService, times(1)).processMeasurements(measurement);
    }

    private SensorMeasurement createTemperatureMeasurement(double value) {
        return SensorMeasurement.builder()
                .sensorId("t1")
                .value(value)
                .timestamp(Instant.now())
                .sensorType(SensorType.TEMPERATURE)
                .warehouseId("warehouse1")
                .build();
    }

    private SensorMeasurement createHumidityMeasurement(double value) {
        return SensorMeasurement.builder()
                .sensorId("h1")
                .value(value)
                .timestamp(Instant.now())
                .sensorType(SensorType.HUMIDITY)
                .warehouseId("warehouse1")
                .build();
    }
}
