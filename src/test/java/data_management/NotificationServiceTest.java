package data_management;

import com.alerts.alertTypes.Alert;
import com.alerts.NotificationService;

import com.alerts.alertTypes.ECGAlert;

/**
 * NotificationServiceTest is a mock implementation of the NotificationService class.
 */
public class NotificationServiceTest extends NotificationService {
    public static boolean wasCalled = false;
    public static Alert receivedAlert = null;

    /**
     * Constructor for NotificationServiceTest.
     */
    @Override
    public void notify(Alert alert) {
        wasCalled = true;
        receivedAlert = alert;
    }
}


