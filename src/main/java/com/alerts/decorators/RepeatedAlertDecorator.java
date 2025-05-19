package com.alerts.decorators;

import com.alerts.AlertGenerator;
import com.alerts.alertTypes.Alert;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.alerts.strategies.OxygenSaturationStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
/**
 * RepeatedAlertDecorator is a decorator for the Alert class that allows for
 * repeated alerts at specified intervals.
 * It extends the AlertDecorator class.
 */
public class RepeatedAlertDecorator extends AlertDecorator {
    private int maxCheckCount;
    private long interval;

    /**
     * Constructor for RepeatedAlertDecorator.
     * It takes an Alert object and wraps it.
     *
     * @param decoratedAlert the Alert object to be decorated
     * @param interval       the interval in milliseconds between checks
     * @param maxCheckCount  the maximum number of checks to perform
     * @param alertGenerator the AlertGenerator instance to trigger alerts
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public RepeatedAlertDecorator(Alert decoratedAlert, long interval,int maxCheckCount,AlertGenerator alertGenerator) throws InterruptedException  {
        super(decoratedAlert);
        this.interval = interval;
        this.maxCheckCount=maxCheckCount;

        long originalTime =decoratedAlert.getTimestamp() ;

        while (originalTime<=decoratedAlert.getTimestamp()+interval*maxCheckCount){
            originalTime+=interval;
            Thread.sleep(interval);
            Patient patient=alertGenerator.getDataStorage().getAllPatients().get(Integer.parseInt(decoratedAlert.getPatientId()));
            alertGenerator.evaluateData(patient,originalTime);
        }

    }



}
