# Temperature Monitoring System

A Spring Boot application for monitoring temperature and humidity sensors in warehouses. The system collects sensor measurements, checks against configurable thresholds, and generates alarm events when thresholds are exceeded.

## Features

- **Sensor Data Collection**: Accepts and stores sensor measurements from temperature and humidity sensors
- **Real-time Monitoring**: Processes incoming measurements and publishes them to monitoring services
- **Alarm System**: Automatically detects when sensor values exceed predefined thresholds and generates alarm events
- **REST API**: Provides endpoints for retrieving measurements and adding new data
- **OpenAPI Documentation**: Integrated Swagger UI for API documentation
- **Modular Architecture**: Uses Spring Modulith for modular application structure

## Technologies Used

- **Java 17**
- **Spring Boot 4.0.3**
- **Spring Web** - For REST API endpoints
- **Spring Integration** - For message processing and integration
- **Spring Modulith** - For modular architecture
- **Lombok** - For reducing boilerplate code
- **SpringDoc OpenAPI** - For API documentation
- **Maven** - Build tool

## Project Structure

```
src/main/java/org/ram/test/temprature/
├── TempratureApplication.java          # Main Spring Boot application class
├── config/
│   ├── OpenAPIConfiguration.java       # OpenAPI/Swagger configuration
│   └── SensorsProps.java               # Sensor configuration properties
├── controller/
│   └── MonitoringController.java       # REST API endpoints
├── model/
│   ├── AlarmEvent.java                 # Alarm event data model
│   ├── SensorMeasurement.java          # Sensor measurement data model
│   └── SensorType.java                 # Sensor type enumeration
├── monitoring/
│   └── CentralMonitoringService.java   # Core monitoring and alarm logic
├── sensors/
│   └── SensorMessageParser.java        # Sensor message parsing (placeholder)
└── warehouse/
    └── WarehouseService.java           # Data storage and event publishing
```

## API Endpoints

### Get Latest Measurements
- **GET** `/api/monitoring/measurements`
- Returns a list of all stored sensor measurements

### Add Measurement
- **POST** `/api/monitoring/measurements`
- Accepts a `SensorMeasurement` object in the request body
- Processes and stores the measurement, triggering alarm checks if necessary

### API Documentation
- **Swagger UI**: Available at `http://localhost:8080/swagger-ui.html` when the application is running

## Configuration

The application uses the following configuration properties (defined in `SensorsProps.java`):

- `temperature.sensor.temperaturePort` - Port for temperature sensor communication (default: 3344)
- `temperature.sensor.humidityPort` - Port for humidity sensor communication (default: 3355)
- `temperature.sensor.temperatureThreshold` - Temperature alarm threshold in °C (default: 35.0)
- `temperature.sensor.humidityThreshold` - Humidity alarm threshold in % (default: 50.0)

These can be overridden in `application.properties` or via environment variables.

## Data Models

### SensorMeasurement
```json
{
  "sensorId": "string",
  "value": "number",
  "timestamp": "Instant",
  "sensorType": "TEMPERATURE|HUMIDITY",
  "warehouseId": "string"
}
```

### AlarmEvent
```json
{
  "sensorId": "string",
  "sensorType": "string",
  "measuredValue": "number",
  "timestamp": "Instant",
  "thresholdValue": "number",
  "warehouseId": "string",
  "alertMessage": "string"
}
```

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run with Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

4. The application will start on port 8080 by default

### Building the Application

```bash
./mvnw clean package
```

This creates a JAR file in the `target/` directory that can be run with:
```bash
java -jar target/temprature-0.0.1-SNAPSHOT.jar
```

## Testing

Run the tests with:
```bash
./mvnw test
```

## Future Enhancements

- Implement the `SensorMessageParser` for parsing raw sensor messages
- Add database persistence for measurements and alarms
- Implement real-time notifications (email, SMS) for alarms
- Add data visualization dashboard
- Implement sensor registration and management endpoints
- Add authentication and authorization</content>
<parameter name="filePath">C:\Users\ramac\IdeaProjects\temprature\README.md
