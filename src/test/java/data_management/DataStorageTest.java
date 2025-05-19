package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * The {@code DataStorageTest} class contains unit tests for the {@link DataStorage} class.
 * It verifies the functionality of adding and retrieving patient data records.
 */
class DataStorageTest {

    /**
     * Test to check if the DataStorage can add and retrieve records correctly.
     */
    @Test
    void testAddAndGetRecords() {
        // TODO Perhaps you can implement a mock data reader to mock the test data?
        // DataReader reader
        DataStorage storage = new DataStorage();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
    }

    /**
     * Test to check if the DataStorage can handle multiple patients and records.
     */
    @Test
    public void testAddAndRetrieveSingleRecord() {
        DataStorage storage = new DataStorage();
        long timestamp = System.currentTimeMillis();

        storage.addPatientData(1, 98.6, "Saturation", timestamp);
        List<PatientRecord> records = storage.getRecords(1, timestamp - 1000, timestamp + 1000);

        assertEquals(1, records.size());
        assertEquals("Saturation", records.get(0).getRecordType());
        assertEquals(98.6, records.get(0).getMeasurementValue(), 0.001);
    }

    /**
     * Test to check if the DataStorage can handle multiple patients and records.
     */
    @Test
    public void testPreventDuplicateRecords() {
        DataStorage storage = new DataStorage();
        long timestamp = System.currentTimeMillis();

        // Add the same record twice
        storage.addPatientData(1, 120.0, "SystolicPressure", timestamp);
        storage.addPatientData(1, 120.0, "SystolicPressure", timestamp);

        List<PatientRecord> records = storage.getRecords(1, timestamp - 1000, timestamp + 1000);
        assertEquals(1, records.size()); // Only one should be stored
    }


    /**
     * Test to check if the DataStorage can handle multiple patients and records.
     */
    @Test
    public void testConcurrentPatientDataAddition() throws InterruptedException {
        DataStorage storage = new DataStorage();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long timestamp = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executor.submit(() -> {
                storage.addPatientData(finalI % 5, 98.6 + finalI, "ECG", timestamp + (finalI % 3));
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(1000);
        }

        // Check that no duplicate (same patient, type, and timestamp) exists
        for (int pid = 0; pid < 5; pid++) {
            List<PatientRecord> records = storage.getRecords(pid, timestamp - 1000, timestamp + 1000);
            long uniqueRecords = records.stream()
                    .map(r -> r.getRecordType() + "-" + r.getTimestamp())
                    .distinct().count();

            assertEquals(uniqueRecords, records.size());
        }
    }
}
