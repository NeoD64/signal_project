package com.alerts;

import com.alerts.alertTypes.Alert;

/**
 * This class is responsible for notifying the medical team when an alert is triggered.
 * It could be extended to send notifications via different channels (e.g., SMS, email, etc.).
 */

public class NotificationService {
    /**
     * Notifies the medical team about the alert.
     * @param alert The alert that was triggered.
     */
        public void notify(Alert alert) {
            // Stub: could call a webhook, send SMS, email, etc.
            System.out.println("Notifying medical team "+alert.getPatientId()+" with condition "+alert.getCondition()+" at time "+alert.getTimestamp());
        }


}
