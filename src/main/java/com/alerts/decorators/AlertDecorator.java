package com.alerts.decorators;

import com.alerts.alertTypes.Alert;

public class AlertDecorator implements Alert {
    private Alert alert;
    public AlertDecorator(Alert alert) {
        this.alert = alert;
    }

    @Override
    public String getPatientId() {
        return alert.getPatientId();
    }

    @Override
    public String getCondition() {
        return alert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return alert.getTimestamp();
    }
}
