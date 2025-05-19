package com.alerts.factories;

import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.ECGAlert;

/**
 * ECGAlertFactory is responsible for creating instances of ECGAlert.
 * It implements the AlertFactory interface.
 */
public class ECGAlertFactory implements AlertFactory {
    /**
     * Creates a new instance of ECGAlert.
     *
     * @param patientId the ID of the patient
     * @param condition the condition that triggered the alert
     * @param timestamp the timestamp of the alert
     * @return a new instance of ECGAlert
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}
