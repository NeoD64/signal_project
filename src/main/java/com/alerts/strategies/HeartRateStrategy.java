package com.alerts.strategies;

import com.alerts.AlertGenerator;
import com.alerts.alertTypes.Alert;
import com.alerts.factories.BloodPressureAlertFactory;
import com.alerts.factories.ECGAlertFactory;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.stream.Collectors;


/**
 * HeartRateStrategy implements the AlertStrategy interface to check for heart rate-related alerts.
 * It checks for critical heart rate levels and trends in the patient's records.
 */
public class HeartRateStrategy implements AlertStrategy {
    /**
     * Checks for heart rate-related alerts based on the provided patient records.
     *
     * @param patientId the ID of the patient
     * @param Record    the list of patient records to check
     * @param generator the AlertGenerator instance to trigger alerts
     */
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
                    generator.triggerAlert(new ECGAlertFactory().createAlert(Integer.toString(patientId),"Critical ECG",record.getTimestamp()));
                    break;
                }
            }
        }
    }
}
