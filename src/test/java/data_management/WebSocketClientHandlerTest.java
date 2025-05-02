package data_management;


import com.data_management.DataStorage;
import com.data_management.WebSocketClientToStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketClientHandlerTest {

    private WebSocketClientToStorage client;
    private DataStorage storage;

    @BeforeEach
    public void setUp() throws Exception {
        this.storage = new DataStorage();
        client = new WebSocketClientToStorage(new URI("ws://localhost:8080"),storage);
    }

    @Test
    public void testValidMessage() {
        String message = "101,1714500000000,ECG,1.2";
        assertDoesNotThrow(() -> client.onMessage(message));
    }

    @Test
    public void testInvalidMessageFormat() {
        String message = "bad,message";
        assertDoesNotThrow(() -> client.onMessage(message));
    }

    @Test
    public void testEmptyMessage() {
        String message = "";
        assertDoesNotThrow(() -> client.onMessage(message));
    }

    @Test
    public void testOnCloseLogsGracefully() {
        assertDoesNotThrow(() -> client.onClose(1000, "Test Close", false));
    }

    @Test
    public void testOnErrorDoesNotCrash() {
        assertDoesNotThrow(() -> client.onError(new RuntimeException("Simulated Error")));
    }

    
}
