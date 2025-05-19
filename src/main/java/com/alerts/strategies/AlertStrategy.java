package com.alerts.strategies;

import com.alerts.AlertGenerator;
import com.alerts.alertTypes.Alert;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * AlertStrategy interface defines the contract for different alert strategies.
 * Each strategy will implement the checkAlert method to define its own alert logic.
 */
public interface AlertStrategy {
    /**
     * Checks for alerts based on the provided patient records.
     *
     * @param patientId the ID of the patient
     * @param Record    the list of patient records to check
     * @param generator the AlertGenerator instance to trigger alerts
     */
    void checkAlert(int patientId, List<PatientRecord> Record, AlertGenerator generator);
}
