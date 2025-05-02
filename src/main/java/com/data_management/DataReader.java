package com.data_management;

import java.io.IOException;

public interface DataReader {
    /**
     * Connects to a real-time data source (e.g., WebSocket) and continuously receives data.
     * Data is stored in the provided DataStorage implementation.
     *
     * @param dataStorage the storage where received data will be stored
     * @throws IOException if there is an error during the connection or data reception
     */
    void startStreaming(DataStorage dataStorage) throws IOException;

    /**
     * Stops the data streaming process and closes the connection to the data source.
     *
     * @throws IOException if there is an error closing the connection
     */
    void stopStreaming() throws IOException;
}
