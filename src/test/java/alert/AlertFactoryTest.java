package alert;

import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.BloodOxygenAlert;
import com.alerts.alertTypes.BloodPressureAlert;
import com.alerts.alertTypes.ECGAlert;
import com.alerts.factories.AlertFactory;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.ECGAlertFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlertFactoryTest {

    @Test
    void testBloodOxygenAlertFactory() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("1", "Low Oxygen", 123L);
        assertTrue(alert instanceof BloodOxygenAlert);
        assertEquals("1", alert.getPatientId());
        assertEquals("Low Oxygen", alert.getCondition());
        assertEquals(123L, alert.getTimestamp());
    }

    @Test
    void testBloodPressureAlertFactory() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("2", "High Pressure", 456L);
        assertTrue(alert instanceof BloodPressureAlert);
        assertEquals("2", alert.getPatientId());
        assertEquals("High Pressure", alert.getCondition());
        assertEquals(456L, alert.getTimestamp());
    }

    @Test
    void testECGAlertFactory() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("3", "ECG Spike", 789L);
        assertTrue(alert instanceof ECGAlert);
        assertEquals("3", alert.getPatientId());
        assertEquals("ECG Spike", alert.getCondition());
        assertEquals(789L, alert.getTimestamp());
    }
}