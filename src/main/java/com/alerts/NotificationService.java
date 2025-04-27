package com.alerts;

public class NotificationService {
        public void notify(Alert alert) {
            // Stub: could call a webhook, send SMS, email, etc.
            System.out.println("Notifying medical team "+alert.getPatientId()+" with condition "+alert.getCondition()+" at time "+alert.getTimestamp());
        }


}
