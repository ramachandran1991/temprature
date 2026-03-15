package org.ram.test.temprature.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class AlarmEvent implements Serializable {

    private String sensorId;
    private String sensorType; // "TEMPERATURE" or "HUMIDITY"
    private double measuredValue;
    private Instant timestamp;
    private double thresholdValue;
    private String warehouseId;
    private String alertMessage;
}
