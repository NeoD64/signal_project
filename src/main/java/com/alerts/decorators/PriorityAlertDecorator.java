package com.alerts.decorators;

import com.alerts.alertTypes.Alert;

/**
 * AlertDecorator is a base class for decorating alerts.
 * It implements the Alert interface and delegates the calls to the wrapped alert.
 * This allows for adding additional functionality to the alert without modifying its structure.
 */
public class PriorityAlertDecorator extends AlertDecorator{

    /**
     * Constructor for PriorityAlertDecorator.
     * It takes an Alert object and wraps it.
     *
     * @param alert the Alert object to be decorated
     */
    public PriorityAlertDecorator(Alert alert) {
        super(alert);
    }
    /**
     * This method adds a priority condition to the alert.
     * It overrides the getCondition method of the Alert interface.
     *
     * @return the condition of the alert with "PRIORITY" prepended
     */
    public String getCondition() {
        return "[PRIORITY] " + super.getCondition();
    }
}
