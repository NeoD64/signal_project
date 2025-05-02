package data_management;

import com.alerts.alertTypes.Alert;
import com.alerts.NotificationService;

public class NotificationServiceTest extends NotificationService {
    public static boolean wasCalled = false;
    public static Alert receivedAlert = null;

    @Override
    public void notify(Alert alert) {
        wasCalled = true;
        receivedAlert = alert;
    }
}


