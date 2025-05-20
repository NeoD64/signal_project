package alert;


import com.alerts.AlertGenerator;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.OxygenSaturationStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class AlertStrategyTest {

    private AlertGenerator generator;

    @BeforeEach
    void setUp() {
        DataStorage mockStorage = new DataStorage();
        NotificationServiceTest mockNotificationService = new NotificationServiceTest();
        generator = new AlertGenerator(mockStorage,mockNotificationService);
    }

    @Test
    void testBloodPressureStrategy_CriticalSystolic() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 200, "SystolicPressure", 1000L));
        new BloodPressureStrategy().checkAlert(1, records, generator);

        assertTrue(NotificationServiceTest.wasCalled);
        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Critical blood pressure", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("1", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;

    }

    @Test
    void testBloodPressureStrategy_Trend() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 100, "SystolicPressure", 1000L));
        records.add(new PatientRecord(1, 115, "SystolicPressure", 2000L));
        records.add(new PatientRecord(1, 130, "SystolicPressure", 3000L));
        new BloodPressureStrategy().checkAlert(1, records, generator);

        assertTrue(NotificationServiceTest.wasCalled);
        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Decreasing or increasing blood pressure trend", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("1", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;
    }

    @Test
    void testOxygenSaturationStrategy_UnderSufficient() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 91, "Saturation", 1000L));
        new OxygenSaturationStrategy().checkAlert(1, records, generator);

        assertTrue(NotificationServiceTest.wasCalled);
        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Lack of oxygen Saturation", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("1", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;

    }

    @Test
    void testOxygenSaturationStrategy_RapidDrop() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 98, "Saturation", 1000L));
        records.add(new PatientRecord(1, 92, "Saturation", 2000L));
        new OxygenSaturationStrategy().checkAlert(1, records, generator);

        assertTrue(NotificationServiceTest.wasCalled);
        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Critical drop in Saturation", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("1", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;

    }

    @Test
    void testHeartRateStrategy_CriticalECG() {
        List<PatientRecord> records = new ArrayList<>();

        records.add(new PatientRecord(1, 60, "ECG", 1000L));
        records.add(new PatientRecord(1, 60, "ECG", 2000L));
        records.add(new PatientRecord(1, 120, "ECG", 3000L));
        records.add(new PatientRecord(1, 60, "ECG", 4000L));
        records.add(new PatientRecord(1, 60, "ECG", 5000L));

        new HeartRateStrategy().checkAlert(1, records, generator);

        assertTrue(NotificationServiceTest.wasCalled);
        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Critical ECG", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("1", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;

    }
}
