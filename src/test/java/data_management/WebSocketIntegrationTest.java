package data_management;



import com.alerts.AlertGenerator;
import com.alerts.NotificationService;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketIntegrationTest {

    private static final int PORT = 8081;
    private static final String URI_STRING = "ws://localhost:" + PORT;
    private static final CountDownLatch alertLatch = new CountDownLatch(1);

    static class MockNotificationService extends NotificationService {
        public volatile boolean alertTriggered = false;

        @Override
        public void notify(com.alerts.alertTypes.Alert alert) {
            alertTriggered = true;
            System.out.println("ALERT TRIGGERED: " + alert.getCondition());
            alertLatch.countDown();
        }
    }

    static class MockWebSocketServer extends WebSocketServer {

        public MockWebSocketServer(int port) {
            super(new InetSocketAddress(port));
        }



        @Override public void onMessage(WebSocket conn, String message) {}


        @Override
        public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
            // Send patient data on connection
            String testData = "1," + System.currentTimeMillis() + ",SystolicPressure,200";
            webSocket.send(testData);
        }

        @Override public void onClose(WebSocket conn, int code, String reason, boolean remote) {}
        @Override public void onError(WebSocket conn, Exception ex) { ex.printStackTrace(); }
        @Override public void onStart() {
            System.out.println("Mock WebSocket server started");
        }
    }

    @Test
    public void testWebSocketToAlertIntegration() throws Exception {
        // Start server
        MockWebSocketServer server = new MockWebSocketServer(PORT);
        server.start();
        Thread.sleep(1000); // Wait for server to start

        // Set up system
        DataStorage storage = new DataStorage();
        MockNotificationService notificationService = new MockNotificationService();
        AlertGenerator alertGenerator = new AlertGenerator(storage, notificationService);

        // Client
        WebSocketClient client = new WebSocketClient(new URI(URI_STRING)) {
            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("Client connected");
            }

            @Override
            public void onMessage(String message) {
                try {
                    String[] parts = message.split(",");
                    int patientId = Integer.parseInt(parts[0]);
                    long timestamp = Long.parseLong(parts[1]);
                    String type = parts[2];
                    double value = Double.parseDouble(parts[3]);

                    storage.addPatientData(patientId, value, type, timestamp);

                    // Evaluate after storing
                    Patient patient = new Patient(patientId);
                    alertGenerator.evaluateData(patient, System.currentTimeMillis());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override public void onClose(int code, String reason, boolean remote) {}
            @Override public void onError(Exception ex) { ex.printStackTrace(); }
        };

        client.connectBlocking();

        // Wait for alert to be triggered
        boolean alertReceived = alertLatch.await(5, TimeUnit.SECONDS);
        client.close();
        server.stop();

        // Assertions
        assertTrue(alertReceived, "Expected alert was not triggered.");
        List<Patient> patients = storage.getAllPatients();
        assertFalse(patients.isEmpty(), "Patient data was not stored.");
    }
}
