package com.alerts.factories;

import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.BloodOxygenAlert;

public class BloodOxygenAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}
