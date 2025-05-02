package com.alerts.strategies;

import com.alerts.AlertGenerator;
import com.alerts.alertTypes.Alert;
import com.data_management.PatientRecord;

import java.util.List;

public interface AlertStrategy {
    void checkAlert(int patientId, List<PatientRecord> Record, AlertGenerator generator);
}
