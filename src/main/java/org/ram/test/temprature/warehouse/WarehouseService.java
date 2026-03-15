package org.ram.test.temprature.warehouse;

import org.ram.test.temprature.model.SensorMeasurement;
import org.ram.test.temprature.model.SensorType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Warehouse class is responsible for storing the sensor data and providing methods to retrieve the data for monitoring and analysis.
 */
@Service
public class WarehouseService {

    private static final Logger logger = Logger.getLogger(WarehouseService.class.getName());

    private final List<SensorMeasurement> measurements = new ArrayList<>();

    private final ApplicationEventPublisher eventPublisher;
    private static final int MAX_CAPACITY = 100;// Maximum number of measurements to store

    public WarehouseService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public synchronized void storeMeasurement(SensorMeasurement measurement) {
        if (measurements.size() >= MAX_CAPACITY) {
            logger.warning("Warehouse capacity reached. Oldest measurement will be removed.");
            measurements.remove(0); // Remove the oldest measurement to make room for new one
        }
        validateAndSetSensorId(measurement);
        measurements.add(measurement);
        logger.info("Measurement stored: " + measurement);
    }

    private void validateAndSetSensorId(SensorMeasurement measurement) {
        if (SensorType.TEMPERATURE.equals(measurement.getSensorType())) {
            measurement.setSensorId("t1");
        } else if (SensorType.HUMIDITY.equals(measurement.getSensorType())) {
            measurement.setSensorId("h1");
        } else {
            throw new IllegalArgumentException("Invalid measurement type: " + measurement.getSensorType());
        }
    }


    public void processMeasurements(SensorMeasurement measurement) {
        // Placeholder for processing logic, e.g., checking thresholds, generating alerts, etc.
        logger.info("Processing measurement: " + measurement);
        storeMeasurement(measurement);
        publishToMonitoring(measurement);
    }

    private void publishToMonitoring(SensorMeasurement measurement) {
        // Publish the measurement to the monitoring system
        eventPublisher.publishEvent(measurement);
        logger.info("Measurement published to monitoring: " + measurement);
    }

    public List<SensorMeasurement> getAllMeasurements() {
        return new ArrayList<>(measurements); // Return a copy to prevent external modification
    }
}
