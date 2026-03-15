package org.ram.test.temprature.udp;

import org.ram.test.temprature.model.AlarmEvent;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.springframework.http.HttpHeaders.HOST;

/**
 * This class is responsible for handling UDP communication with the sensors. It will listen for incoming UDP packets
 * from the sensors, extract the raw data, and pass it to the SensorMessageParser for parsing. The UdpServer will
 * also handle any necessary setup for the UDP socket and ensure that it can receive messages from the sensors
 * reliably. Once a message is received and parsed, it can be processed further to check against thresholds and
 * generate alarm events if necessary.
 */
@Service
public class SensorSimulator {

    private static final int PORT = 5000;

    public void sendSensorData(AlarmEvent alarmEvent) throws IOException {

        DatagramSocket socket = new DatagramSocket();

        String message = alarmEvent.toString();

        byte[] buffer = message.getBytes();

        InetAddress address = InetAddress.getByName(HOST);

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);

        socket.send(packet);
        socket.close();

    }
}
