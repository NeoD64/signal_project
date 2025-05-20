package alert;
import com.alerts.AlertGenerator;
import com.alerts.NotificationService;
import com.alerts.decorators.AlertDecorator;

import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.BloodOxygenAlert;
import com.alerts.decorators.PriorityAlertDecorator;
import com.alerts.decorators.RepeatedAlertDecorator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlertDecoratorTest {

    @Test
    void testAlertDecoratorDelegation() {
        Alert alert = new BloodOxygenAlert("42", "Low Oxygen", 123456L);
        AlertDecorator decorator = new AlertDecorator(alert);

        assertEquals("42", decorator.getPatientId());
        assertEquals("Low Oxygen", decorator.getCondition());
        assertEquals(123456L, decorator.getTimestamp());
    }

    @Test
    void testPriorityAlertDecorator() {
        Alert alert = new BloodOxygenAlert("99", "Critical", 987654L);
        PriorityAlertDecorator priorityDecorator = new PriorityAlertDecorator(alert);

        assertEquals("99", priorityDecorator.getPatientId());
        assertEquals("[PRIORITY] Critical", priorityDecorator.getCondition());
        assertEquals(987654L, priorityDecorator.getTimestamp());
    }
    @Test
    void testReapetedDecorator() throws InterruptedException {
        Alert alert = new BloodOxygenAlert("0", "Lack of oxygen Saturation", 987654L);
        DataStorage dataStorage = new DataStorage();
        Patient patient = new Patient(0);
        dataStorage.addPatientData(0, 91.0, "Saturation", 987654L);
        NotificationService notificationService = new NotificationService();
        AlertGenerator generator = new AlertGenerator(dataStorage, notificationService);
        RepeatedAlertDecorator repeatedAlert = new RepeatedAlertDecorator(alert, 1000, 5, generator);

        assertEquals("0", repeatedAlert.getPatientId());
        assertEquals("Lack of oxygen Saturation", repeatedAlert.getCondition());
        assertEquals(987654L, repeatedAlert.getTimestamp());
    }
}