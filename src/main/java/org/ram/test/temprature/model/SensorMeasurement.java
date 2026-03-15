package org.ram.test.temprature.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorMeasurement {

    private String sensorId;
    private double value;
    private Instant timestamp;
    private SensorType sensorType;
    private String warehouseId;


}