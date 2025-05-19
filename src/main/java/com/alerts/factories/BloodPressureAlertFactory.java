package com.alerts.factories;

import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.BloodPressureAlert;

/**
 * BloodPressureAlertFactory is responsible for creating instances of BloodPressureAlert.
 * It implements the AlertFactory interface.
 */
public class BloodPressureAlertFactory implements AlertFactory {
    /**
     * Creates a new instance of BloodPressureAlert.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert
     * @param timestamp the timestamp of the alert
     * @return a new instance of BloodPressureAlert
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}
