package data_management;


import com.data_management.DataStorage;
import com.data_management.WebSocketClientToStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The {@code WebSocketClientHandlerTest} class contains unit tests for the {@link WebSocketClientToStorage} class.
 * It verifies the functionality of handling messages, closing connections, and error handling.
 */
public class WebSocketClientHandlerTest {

    private WebSocketClientToStorage client;
    private DataStorage storage;

    @BeforeEach
    /**
     * Sets up the test environment by initializing a {@link DataStorage} instance and a {@link WebSocketClientToStorage} instance.
     *
     * @throws Exception if there is an error during setup
     */
    public void setUp() throws Exception {
        this.storage = new DataStorage();
        client = new WebSocketClientToStorage(new URI("ws://localhost:8080"),storage);
    }

    @Test
    /**
     * Tests the method with a valid message.
     * It verifies that the method does not throw any exceptions when processing a valid message.
     */
    public void testValidMessage() {
        String message = "101,1714500000000,ECG,1.2";
        assertDoesNotThrow(() -> client.onMessage(message));
    }

    @Test
    /**
     * Tests the method with an invalid message format.
     * It verifies that the method does not throw any exceptions when processing an invalid message format.
     */
    public void testInvalidMessageFormat() {
        String message = "bad,message";
        assertDoesNotThrow(() -> client.onMessage(message));
    }

    @Test
    /**
     * Tests the method with a message that has missing fields.
     * It verifies that the method does not throw any exceptions when processing a message with missing fields.
     */
    public void testEmptyMessage() {
        String message = "";
        assertDoesNotThrow(() -> client.onMessage(message));
    }

    @Test
    /**
     * Tests the method with a message that has invalid data types.
     * It verifies that the method does not throw any exceptions when processing a message with invalid data types.
     */
    public void testOnCloseLogsGracefully() {
        assertDoesNotThrow(() -> client.onClose(1000, "Test Close", false));
    }

    @Test
    /**
     * Tests the method with a message that has invalid data types.
     * It verifies that the method does not throw any exceptions when processing a message with invalid data types.
     */
    public void testOnErrorDoesNotCrash() {
        assertDoesNotThrow(() -> client.onError(new RuntimeException("Simulated Error")));
    }

    
}
