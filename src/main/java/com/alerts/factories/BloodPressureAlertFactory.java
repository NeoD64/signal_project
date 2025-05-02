package com.alerts.factories;

import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.BloodPressureAlert;

public class BloodPressureAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}
