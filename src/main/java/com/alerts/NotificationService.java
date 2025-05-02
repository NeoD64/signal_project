package com.alerts;

import com.alerts.alertTypes.Alert;

public class NotificationService {
        public void notify(Alert alert) {
            // Stub: could call a webhook, send SMS, email, etc.
            System.out.println("Notifying medical team "+alert.getPatientId()+" with condition "+alert.getCondition()+" at time "+alert.getTimestamp());
        }


}
