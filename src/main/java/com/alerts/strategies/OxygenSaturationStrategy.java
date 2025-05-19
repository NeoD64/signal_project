package com.alerts.strategies;

import com.alerts.AlertGenerator;
import com.alerts.alertTypes.Alert;
import com.alerts.factories.BloodOxygenAlertFactory;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OxygenSaturationStrategy implements the AlertStrategy interface to check for oxygen saturation-related alerts.
 * It checks for critical oxygen saturation levels and trends in the patient's records.
 */
public class OxygenSaturationStrategy implements AlertStrategy {
    /**
     * Checks for oxygen saturation-related alerts based on the provided patient records.
     *
     * @param patientId the ID of the patient
     * @param Record    the list of patient records to check
     * @param generator the AlertGenerator instance to trigger alerts
     */
    @Override
    public void checkAlert(int patientId, List<PatientRecord> Record, AlertGenerator generator) {

        List<PatientRecord> SaturationRecords= Record.stream()
                .filter(record->record.getRecordType().equalsIgnoreCase("Saturation"))
                .collect(Collectors.toList()) ;
        for (PatientRecord record : SaturationRecords) {
            // Check for <92%
            if(record.getMeasurementValue()<92) {
                generator.triggerAlert(new BloodOxygenAlertFactory().createAlert(Integer.toString(patientId),"Lack of oxygen Saturation",record.getTimestamp()));
                break;
            }
            // rapid 5% drop within 10 minutes
            for(PatientRecord record2 : SaturationRecords.stream().filter(r->r.getTimestamp()<=record.getTimestamp()+600000 && r.getTimestamp()>=record.getTimestamp() ).collect(Collectors.toList())) {
                if(record.getMeasurementValue()-record2.getMeasurementValue()>=5) {
                    generator.triggerAlert(new BloodOxygenAlertFactory().createAlert(Integer.toString(patientId),"Critical drop in Saturation",record2.getTimestamp()));
                    break;
                }
            }
        }
    }
}
