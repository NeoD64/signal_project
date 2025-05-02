package com.alerts.decorators;

import com.alerts.alertTypes.Alert;

public class PriorityAlertDecorator extends AlertDecorator{
    public PriorityAlertDecorator(Alert alert) {
        super(alert);
    }
    public String getCondition() {
        return "[PRIORITY] " + super.getCondition();
    }
}
