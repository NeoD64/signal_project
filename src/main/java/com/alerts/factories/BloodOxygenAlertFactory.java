package com.alerts.factories;

import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.BloodOxygenAlert;

/**
 * BloodOxygenAlertFactory is responsible for creating BloodOxygenAlert instances.
 * It implements the AlertFactory interface.
 */
public class BloodOxygenAlertFactory implements AlertFactory {
    /**
     * Creates a new BloodOxygenAlert instance.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert
     * @param timestamp the timestamp of the alert
     * @return a new BloodOxygenAlert instance
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}
