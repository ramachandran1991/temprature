package org.ram.test.temprature.monitoring;

import org.ram.test.temprature.config.SensorsProps;
import org.ram.test.temprature.model.AlarmEvent;
import org.ram.test.temprature.model.SensorMeasurement;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * CentralMonitoringService is responsible for aggregating and processing data from various sensors.
 * It can provide functionalities such as:
 * - Storing sensor measurements
 * - Analyzing data to detect anomalies
 * - Providing real-time monitoring and alerts
 * - Generating reports based on historical data
 *
 * This class can be further expanded to include methods for handling these functionalities.
 */
@Service
public class CentralMonitoringService {

    private static final Logger logger = Logger.getLogger(CentralMonitoringService.class.getName());

    private final SensorsProps sensorsProps;
    private final List<AlarmEvent> alarmEvents = new ArrayList<>();
    private static final int MAX_ALARM_EVENTS = 100; // Maximum number of alarm events to store

    public CentralMonitoringService(SensorsProps sensorsProps) {
        this.sensorsProps = sensorsProps;
    }

    @EventListener
    public void handleSensorMeasurement(SensorMeasurement measurement) {
        logger.info("Received sensor measurement: " + measurement);
        checkForAlarms(measurement);
    }

    public List<AlarmEvent> getAlarmEvents() {
        return new ArrayList<>(alarmEvents); // Return a copy to prevent external modification
    }

   private double getThreshold(SensorMeasurement measurement) {
        return switch (measurement.getSensorType()) {
            case TEMPERATURE -> sensorsProps.getTemperatureThreshold();
            case HUMIDITY -> sensorsProps.getHumidityThreshold();
        };
    }

    private void checkForAlarms(SensorMeasurement measurement) {
        double threshold = getThreshold(measurement);
        if (measurement.getValue() > threshold) {
            AlarmEvent alarmEvent = AlarmEvent.builder()
                    .sensorId(measurement.getSensorId())
                    .sensorType(measurement.getSensorType().name())
                    .measuredValue(measurement.getValue())
                    .timestamp(measurement.getTimestamp())
                    .thresholdValue(threshold)
                    .warehouseId(measurement.getWarehouseId())
                    .alertMessage(String.format("Alarm: %s sensor %s exceeded threshold! Measured value: %.2f %s, Threshold: %.2f %s",
                            measurement.getSensorType(), measurement.getSensorId(), measurement.getValue(), getUnit(measurement), threshold, getUnit(measurement)))
                    .build();
            addAlarmEvent(alarmEvent);
            logger.warning("Alarm triggered: " + alarmEvent);
        }
    }

    private synchronized void addAlarmEvent(AlarmEvent alarmEvent) {
        if (alarmEvents.size() >= MAX_ALARM_EVENTS) {
            logger.warning("Alarm events capacity reached. Oldest event will be removed.");
            alarmEvents.remove(0); // Remove the oldest event to make room for new one
        }
        alarmEvents.add(alarmEvent);
    }

    private String getUnit(SensorMeasurement measurement) {
        return switch (measurement.getSensorType()) {
            case TEMPERATURE -> "°C";
            case HUMIDITY -> "%";
        };
    }

}
