package org.ram.test.temprature.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ram.test.temprature.model.SensorMeasurement;
import org.ram.test.temprature.warehouse.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/monitoring")
@Tag(name="Monitoring", description = "Central Monitor API Serivice")
public class MonitoringController {


    private final WarehouseService warehouse;

    public MonitoringController(WarehouseService warehouse) {
        this.warehouse = warehouse;
    }

    @GetMapping("/measurements")
    @Operation(summary = "Get Latest Measurements", description = "Fetch the latest measurements from all sensors in the warehouse")
    public ResponseEntity<List<SensorMeasurement>> getLatestMeasurements() {
        return ResponseEntity.ok(warehouse.getAllMeasurements());
    }

    @PostMapping("/measurements")
    @Operation(summary = "Add Measurement", description = "Add a new measurement to the monitoring system")
    public ResponseEntity<String> addMeasurement(SensorMeasurement measurement) {
       if(measurement == null || measurement.getSensorType() == null || Double.isNaN(measurement.getValue())) {
           return ResponseEntity.badRequest().body("Invalid measurement data");
       }
        warehouse.processMeasurements(measurement);
        return ResponseEntity.ok("Measurement added and processed successfully");
    }
}
