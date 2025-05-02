package com.alerts.strategies;

import com.alerts.AlertGenerator;
import com.alerts.alertTypes.Alert;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.stream.Collectors;

public class HeartRateStrategy implements AlertStrategy {
    @Override
    public void checkAlert(int patientId, List<PatientRecord> Record, AlertGenerator generator) {
        List<PatientRecord> ECGRecords= Record.stream()
                .filter(record->record.getRecordType().equalsIgnoreCase("ECG"))
                .collect(Collectors.toList());
        int windowSize = 5;
        for(int i=windowSize;i<= ECGRecords.size();i++) {
            List<PatientRecord> WindowsRecords = ECGRecords.subList(i-windowSize, i);
            double WindowAverage = 0.0;
            for (PatientRecord record : WindowsRecords) {
                WindowAverage += record.getMeasurementValue();
            }
            WindowAverage /= windowSize;
            for (PatientRecord record : WindowsRecords) {
                if(record.getMeasurementValue()>=WindowAverage*1.5) {
                    generator.triggerAlert(new BloodPressureAlertFactory().createAlert(Integer.toString(patientId),"Critical ECG",record.getTimestamp()));
                    break;
                }
            }
        }
    }
}
