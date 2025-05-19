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

/**
 * The {@code WebSocketIntegrationTest} class contains integration tests for the
 * WebSocket client and server interaction with the alert generation system.
 * It verifies that patient data received via WebSocket is correctly processed
 * and alerts are generated as expected.
 */
public class WebSocketIntegrationTest {

    private static final int PORT = 8081;
    private static final String URI_STRING = "ws://localhost:" + PORT;
    private static final CountDownLatch alertLatch = new CountDownLatch(1);

    /**
     * Mock NotificationService to simulate alert notifications.
     */
    static class MockNotificationService extends NotificationService {
        public volatile boolean alertTriggered = false;


        @Override
        /**
         * This method is called when an alert is triggered.
         * It simulates sending a notification to the user.
         *
         * @param alert The alert that was triggered.
         */
        public void notify(com.alerts.alertTypes.Alert alert) {
            alertTriggered = true;
            System.out.println("ALERT TRIGGERED: " + alert.getCondition());
            alertLatch.countDown();
        }
    }

    /**
     * Mock WebSocket server to simulate incoming patient data.
     */
    static class MockWebSocketServer extends WebSocketServer {

        /**
         * Constructor for the MockWebSocketServer.
         *
         * @param port The port on which the server will listen for incoming connections.
         */
        public MockWebSocketServer(int port) {
            super(new InetSocketAddress(port));
        }

        @Override
        /**
         * This method is called when a message is received from a client.
         * It simulates sending patient data to the client.
         *
         * @param conn    The connection to the client.
         * @param message The message received from the client.
         */
        public void onMessage(WebSocket conn, String message) {}


        @Override
        /**
         * This method is called when a new connection is opened.
         * It simulates sending patient data to the client.
         *
         * @param webSocket The WebSocket connection to the client.
         * @param clientHandshake The handshake information for the connection.
         */
        public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
            // Send patient data on connection
            String testData = "1," + System.currentTimeMillis() + ",SystolicPressure,200";
            webSocket.send(testData);
        }

        @Override
        /**
         * This method is called when a connection is closed.
         * It simulates closing the connection to the client.
         *
         * @param conn   The connection to the client.
         * @param code   The close code.
         * @param reason The reason for closing the connection.
         * @param remote Whether the close was initiated by the remote peer.
         */
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {}

        /**
         * This method is called when an error occurs.
         * It simulates handling errors during the connection.
         *
         * @param conn The connection to the client.
         * @param ex   The exception that occurred.
         */
        @Override
        public void onError(WebSocket conn, Exception ex) { ex.printStackTrace(); }

        /**
         * This method is called when the server starts.
         * It simulates starting the server.
         */
        @Override public void onStart() {
            System.out.println("Mock WebSocket server started");
        }
    }

    @Test
    /**
     * Integration test for WebSocket client and server interaction.
     * It verifies that patient data received via WebSocket is correctly processed
     * and alerts are generated as expected.
     *
     * @throws Exception if there is an error during the test
     */
    public void testWebSocketToAlertIntegration() throws Exception {

        MockWebSocketServer server = new MockWebSocketServer(PORT);
        server.start();
        Thread.sleep(1000); // Wait for server to start


        DataStorage storage = new DataStorage();
        MockNotificationService notificationService = new MockNotificationService();
        AlertGenerator alertGenerator = new AlertGenerator(storage, notificationService);

        // Create a WebSocket client to connect to the server
        WebSocketClient client = new WebSocketClient(new URI(URI_STRING)) {
            @Override
            /**
             * This method is called when the WebSocket connection is opened.
             * It simulates sending a message to the server.
             *
             * @param handshake The handshake information for the connection.
             */
            public void onOpen(ServerHandshake handshake) {
                System.out.println("Client connected");
            }

            @Override
            /**
             * This method is called when a message is received from the server.
             * It simulates processing the received message and storing patient data.
             *
             * @param message The message received from the server.
             */
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

            @Override
            /**
             * This method is called when the WebSocket connection is closed.
             * It simulates closing the connection to the server.
             *
             * @param code   The close code.
             * @param reason The reason for closing the connection.
             * @param remote Whether the close was initiated by the remote peer.
             */
            public void onClose(int code, String reason, boolean remote) {}

            /**
             * This method is called when an error occurs.
             * It simulates handling errors during the connection.
             *
             * @param ex The exception that occurred.
             */
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
