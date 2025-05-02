package com.alerts.factories;

import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.ECGAlert;

public class ECGAlertFactory implements AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}
