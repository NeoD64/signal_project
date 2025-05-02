package com.alerts.alertTypes;

public class BloodPressureAlert implements Alert {
    String patientId;
    String condition;
    long timestamp;
    public BloodPressureAlert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }
}
