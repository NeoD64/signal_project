package com.alerts.factories;

import com.alerts.alertTypes.Alert;

/**
 * AlertFactory interface defines the contract for creating different types of alerts.
 * Each specific alert factory will implement this interface to create its own alert type.
 */
public interface AlertFactory {
    /**
     * Creates an alert based on the provided parameters.
     *
     * @param patientId The ID of the patient for whom the alert is created.
     * @param condition The condition that triggered the alert.
     * @param timestamp The timestamp when the alert was created.
     * @return An instance of Alert representing the created alert.
     */
    Alert createAlert(String patientId, String condition, long timestamp);
}
