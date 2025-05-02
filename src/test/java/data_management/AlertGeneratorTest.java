package data_management;


import com.alerts.AlertGenerator;
import com.alerts.NotificationService;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class AlertGeneratorTest {

    private DataStorage mockStorage;
    private AlertGenerator alertGenerator;
    private NotificationServiceTest mockNotificationService;
    private final long now = System.currentTimeMillis();

    @BeforeEach
    public void setUp() {
        mockStorage = new DataStorage();
        mockNotificationService = new NotificationServiceTest();
        alertGenerator = new AlertGenerator(mockStorage, mockNotificationService);
    }

    @Test
    public void testCriticalSystolicBloodPressureTriggersAlert() {
        Patient patient = new Patient(1);
        mockStorage.addPatientData(1,  190,"SystolicPressure", now);
        alertGenerator.evaluateData(patient,now);

        assertTrue(NotificationServiceTest.wasCalled);
        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Critical blood pressure", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("1", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;

    }

    @Test
    public void testDecreasingSystolicTrendTriggersAlert() {
        Patient patient = new Patient(2);
        mockStorage.addPatientData(2, 170,"SystolicPressure", now - 60000);
        mockStorage.addPatientData(2, 150,"SystolicPressure", now - 40000);
        mockStorage.addPatientData(2, 139,"SystolicPressure",now - 20000);
        alertGenerator.evaluateData(patient,now);

        assertTrue(NotificationServiceTest.wasCalled);
        NotificationServiceTest.wasCalled= false;

        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Decreasing or increasing blood pressure trend", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("2", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;
    }

    @Test
    public void testCriticalSaturationTriggersAlert() {
        Patient patient = new Patient(3);
        mockStorage.addPatientData(3, 90, "Saturation", now);
        alertGenerator.evaluateData(patient,now);

        assertTrue(NotificationServiceTest.wasCalled);
        NotificationServiceTest.wasCalled= false;

        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Lack of oxygen Saturation", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("3", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;
    }

    @Test
    public void testRapidSaturationDropTriggersAlert() {
        Patient patient = new Patient(4);
        mockStorage.addPatientData(4, 98,  "Saturation",now - 500000);
        mockStorage.addPatientData(4,  92, "Saturation", now);
        alertGenerator.evaluateData(patient,now);

        assertTrue(NotificationServiceTest.wasCalled);
        NotificationServiceTest.wasCalled= false;

        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Critical drop in Saturation", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("4", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;

    }

    /*@Test
    public void testHypotensiveHypoxemiaTriggersAlert() {
        Patient patient = new Patient(5);
        mockStorage.addPatientData(5, 85,"SystolicPressure", now-5000);
        mockStorage.addPatientData(5, 89,"Saturation", now);
        alertGenerator.evaluateData(patient,now);

        assertTrue(NotificationServiceTest.wasCalled);
        NotificationServiceTest.wasCalled= false;

        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Hypotensive Hypoxemia", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("5", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;

    }*/

    @Test
    public void testECGAlertTriggersForSpike() {
        Patient patient = new Patient(6);
        // Fill with low values first
        for (int i = 0; i < 5; i++) {
            mockStorage.addPatientData(6, 1.0,"ECG", now - (5000 - i * 1000));
        }
        // Spike
        mockStorage.addPatientData(6, 2.0,"ECG", now);
        alertGenerator.evaluateData(patient,now);

        assertTrue(NotificationServiceTest.wasCalled);
        NotificationServiceTest.wasCalled= false;

        assertNotNull(NotificationServiceTest.receivedAlert);
        assertEquals("Critical ECG", NotificationServiceTest.receivedAlert.getCondition());
        assertEquals("6", NotificationServiceTest.receivedAlert.getPatientId());
        NotificationServiceTest.receivedAlert = null;


    }
}
