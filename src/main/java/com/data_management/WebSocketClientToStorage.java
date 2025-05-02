package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClientToStorage extends WebSocketClient implements DataReader {

    private DataStorage dataStorage;

    public WebSocketClientToStorage(URI serverUri, DataStorage dataStorage) throws URISyntaxException {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("WebSocket connected.");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received: " + message);
        try {
            String[] parts = message.split(",");
            if (parts.length != 4) {
                System.err.println("Invalid message format: " + message);
                return;
            }

            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String recordType = parts[2];
            double measurement = Double.parseDouble(parts[3]);

            dataStorage.addPatientData(patientId,measurement , recordType, timestamp);

        } catch (Exception e) {
            System.err.println("Failed to parse/store message: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }


    @Override
    public void startStreaming(DataStorage dataStorage) throws IOException {
        this.connect(); // Non-blocking; onOpen will be triggered
    }

    @Override
    public void stopStreaming() throws IOException {
        this.close();
    }
}
