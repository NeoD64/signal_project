package com.alerts.decorators;

import com.alerts.alertTypes.Alert;

/**
 * AlertDecorator is a base class for decorating alerts.
 * It implements the Alert interface and delegates the calls to the wrapped alert.
 * This allows for adding additional functionality to the alert without modifying its structure.
 */
public class AlertDecorator implements Alert {
    private Alert alert;
    /**
     * Constructor that takes an Alert object to decorate.
     *
     * @param alert the Alert object to decorate
     */
    public AlertDecorator(Alert alert) {
        this.alert = alert;
    }

    /**
     * Gets the wrapped Alert object.
     *
     * @return the wrapped Alert object
     */
    @Override
    public String getPatientId() {
        return alert.getPatientId();
    }

    /**
     * Gets the condition of the wrapped Alert object.
     *
     * @return the condition of the wrapped Alert object
     */
    @Override
    public String getCondition() {
        return alert.getCondition();
    }

    /**
     * Gets the timestamp of the wrapped Alert object.
     *
     * @return the timestamp of the wrapped Alert object
     */
    @Override
    public long getTimestamp() {
        return alert.getTimestamp();
    }
}
