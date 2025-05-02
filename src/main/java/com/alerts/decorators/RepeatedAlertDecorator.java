package com.alerts.decorators;

import com.alerts.AlertGenerator;
import com.alerts.alertTypes.Alert;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.alerts.strategies.OxygenSaturationStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int maxCheckCount;
    private long interval;

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
