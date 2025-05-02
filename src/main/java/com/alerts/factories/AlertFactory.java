package com.alerts.factories;

import com.alerts.alertTypes.Alert;

public interface AlertFactory {
    Alert createAlert(String patientId, String condition, long timestamp);
}
