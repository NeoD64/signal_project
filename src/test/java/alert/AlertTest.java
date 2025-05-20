package alert;
import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.BloodOxygenAlert;
import com.alerts.alertTypes.BloodPressureAlert;
import com.alerts.alertTypes.ECGAlert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlertTest {

    @Test
    void testBloodOxygenAlert() {
        Alert alert = new BloodOxygenAlert("1", "Low Oxygen", 123456789L);
        assertEquals("1", alert.getPatientId());
        assertEquals("Low Oxygen", alert.getCondition());
        assertEquals(123456789L, alert.getTimestamp());
    }

    @Test
    void testBloodPressureAlert() {
        Alert alert = new BloodPressureAlert("2", "High Pressure", 987654321L);
        assertEquals("2", alert.getPatientId());
        assertEquals("High Pressure", alert.getCondition());
        assertEquals(987654321L, alert.getTimestamp());
    }

    @Test
    void testECGAlert() {
        Alert alert = new ECGAlert("3", "ECG Spike", 555555555L);
        assertEquals("3", alert.getPatientId());
        assertEquals("ECG Spike", alert.getCondition());
        assertEquals(555555555L, alert.getTimestamp());
    }
}