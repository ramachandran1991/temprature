package org.ram.test.temprature.warehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ram.test.temprature.model.SensorMeasurement;
import org.ram.test.temprature.model.SensorType;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private WarehouseService warehouseService;

    @BeforeEach
    void setUp() {
        warehouseService = new WarehouseService(eventPublisher);
    }

    @Test
    void shouldStoreMeasurement() {
        // Given
        SensorMeasurement measurement = createMeasurement();

        // When
        warehouseService.storeMeasurement(measurement);

        // Then
        List<SensorMeasurement> measurements = warehouseService.getAllMeasurements();
        assertThat(measurements).hasSize(1);
        assertThat(measurements.get(0)).isEqualTo(measurement);
    }

    @Test
    void shouldProcessMeasurementsAndPublishEvent() {
        // Given
        SensorMeasurement measurement = createMeasurement();

        // When
        warehouseService.processMeasurements(measurement);

        // Then
        List<SensorMeasurement> measurements = warehouseService.getAllMeasurements();
        assertThat(measurements).hasSize(1);
        verify(eventPublisher).publishEvent(measurement);
    }

    @Test
    void shouldReturnCopyOfMeasurements() {
        // Given
        SensorMeasurement measurement = createMeasurement();
        warehouseService.storeMeasurement(measurement);

        // When
        List<SensorMeasurement> measurements1 = warehouseService.getAllMeasurements();
        List<SensorMeasurement> measurements2 = warehouseService.getAllMeasurements();

        // Then
        assertThat(measurements1).isNotSameAs(measurements2);
        assertThat(measurements1).isEqualTo(measurements2);
    }

    @Test
    void shouldRemoveOldestMeasurementWhenCapacityReached() {
        // Given
        for (int i = 0; i < 101; i++) {
            SensorMeasurement measurement = SensorMeasurement.builder()
                    .sensorId("sensor" + i)
                    .value(20.0 + i)
                    .timestamp(Instant.now())
                    .sensorType(SensorType.TEMPERATURE)
                    .warehouseId("warehouse1")
                    .build();
            warehouseService.storeMeasurement(measurement);
        }

        // When
        List<SensorMeasurement> measurements = warehouseService.getAllMeasurements();

        // Then
        assertThat(measurements).hasSize(100);
        assertThat(measurements.get(0).getSensorId()).isEqualTo("sensor1");
        assertThat(measurements.get(99).getSensorId()).isEqualTo("sensor100");
    }

    private SensorMeasurement createMeasurement() {
        return SensorMeasurement.builder()
                .sensorId("sensor1")
                .value(25.0)
                .timestamp(Instant.now())
                .sensorType(SensorType.TEMPERATURE)
                .warehouseId("warehouse1")
                .build();
    }
}
