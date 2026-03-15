package org.ram.test.temprature.monitoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ram.test.temprature.config.SensorsProps;
import org.ram.test.temprature.model.AlarmEvent;
import org.ram.test.temprature.model.SensorMeasurement;
import org.ram.test.temprature.model.SensorType;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CentralMonitoringServiceTest {

    @Mock
    private SensorsProps sensorsProps;

    private CentralMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        sensorsProps = new SensorsProps();
        monitoringService = new CentralMonitoringService(sensorsProps);
    }

    @Test
    void shouldHandleSensorMeasurementAndCheckForAlarms() {
        // Given
        SensorMeasurement measurement = createTemperatureMeasurement(40.0); // Above threshold

        // When
        monitoringService.handleSensorMeasurement(measurement);

        // Then
        List<AlarmEvent> alarms = monitoringService.getAlarmEvents();
        assertThat(alarms).hasSize(1);
        AlarmEvent alarm = alarms.get(0);
        assertThat(alarm.getSensorId()).isEqualTo("sensor1");
        assertThat(alarm.getSensorType()).isEqualTo("TEMPERATURE");
        assertThat(alarm.getMeasuredValue()).isEqualTo(40.0);
        assertThat(alarm.getThresholdValue()).isEqualTo(35.0);
        assertThat(alarm.getWarehouseId()).isEqualTo("warehouse1");
        assertThat(alarm.getAlertMessage()).contains("Alarm: TEMPERATURE sensor sensor1 exceeded threshold!");
    }

    @Test
    void shouldNotTriggerAlarmWhenBelowThreshold() {
        // Given
        SensorMeasurement measurement = createTemperatureMeasurement(30.0); // Below threshold

        // When
        monitoringService.handleSensorMeasurement(measurement);

        // Then
        List<AlarmEvent> alarms = monitoringService.getAlarmEvents();
        assertThat(alarms).isEmpty();
    }

    @Test
    void shouldTriggerAlarmForHumidity() {
        // Given
        SensorMeasurement measurement = createHumidityMeasurement(60.0); // Above threshold

        // When
        monitoringService.handleSensorMeasurement(measurement);

        // Then
        List<AlarmEvent> alarms = monitoringService.getAlarmEvents();
        assertThat(alarms).hasSize(1);
        AlarmEvent alarm = alarms.get(0);
        assertThat(alarm.getSensorType()).isEqualTo("HUMIDITY");
        assertThat(alarm.getThresholdValue()).isEqualTo(50.0);
        assertThat(alarm.getAlertMessage()).contains("Alarm: HUMIDITY sensor sensor1 exceeded threshold!");
    }

    @Test
    void shouldReturnCopyOfAlarmEvents() {
        // Given
        SensorMeasurement measurement = createTemperatureMeasurement(40.0);
        monitoringService.handleSensorMeasurement(measurement);

        // When
        List<AlarmEvent> alarms1 = monitoringService.getAlarmEvents();
        List<AlarmEvent> alarms2 = monitoringService.getAlarmEvents();

        // Then
        assertThat(alarms1).isNotSameAs(alarms2);
        assertThat(alarms1).isEqualTo(alarms2);
    }

    @Test
    void shouldLimitAlarmEventsToMaxCapacity() {
        // Given
        for (int i = 0; i < 101; i++) {
            SensorMeasurement measurement = createTemperatureMeasurement(40.0 + i);
            monitoringService.handleSensorMeasurement(measurement);
        }

        // When
        List<AlarmEvent> alarms = monitoringService.getAlarmEvents();

        // Then
        assertThat(alarms).hasSize(100);
    }

    private SensorMeasurement createTemperatureMeasurement(double value) {
        return SensorMeasurement.builder()
                .sensorId("sensor1")
                .value(value)
                .timestamp(Instant.now())
                .sensorType(SensorType.TEMPERATURE)
                .warehouseId("warehouse1")
                .build();
    }

    private SensorMeasurement createHumidityMeasurement(double value) {
        return SensorMeasurement.builder()
                .sensorId("sensor1")
                .value(value)
                .timestamp(Instant.now())
                .sensorType(SensorType.HUMIDITY)
                .warehouseId("warehouse1")
                .build();
    }
}
