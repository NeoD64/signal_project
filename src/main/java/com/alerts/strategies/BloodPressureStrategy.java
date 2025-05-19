package com.alerts.strategies;

import com.alerts.AlertGenerator;
import com.alerts.alertTypes.Alert;
import com.alerts.factories.BloodPressureAlertFactory;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BloodPressureStrategy implements the AlertStrategy interface to check for blood pressure-related alerts.
 * It checks for critical blood pressure levels and trends in the patient's records.
 */
public class BloodPressureStrategy implements AlertStrategy {
    /**
     * Checks for blood pressure-related alerts based on the provided patient records.
     *
     * @param patientId the ID of the patient
     * @param Record    the list of patient records to check
     * @param generator the AlertGenerator instance to trigger alerts
     */
    @Override
    public void checkAlert(int patientId, List<PatientRecord> Record, AlertGenerator generator){
        List<PatientRecord> SystolicPressureRecords= Record.stream()
                .filter(record-> record.getRecordType().equalsIgnoreCase("SystolicPressure"))
                .collect(Collectors.toList()) ;
        List<PatientRecord> DiastolicPressureRecords= Record.stream()
                .filter(record->record.getRecordType().equalsIgnoreCase("DiastolicPressure"))
                .collect(Collectors.toList()) ;

        PatientRecord antepenultimateSysRecord = null;
        PatientRecord previousSysRecord =null;
        for (PatientRecord record : SystolicPressureRecords) {
            // critical threshold
            if (record.getMeasurementValue()>180 ||record.getMeasurementValue()<90) {
                generator.triggerAlert(new BloodPressureAlertFactory().createAlert(Integer.toString(patientId),"Critical blood pressure",record.getTimestamp()));
                break;
            }
            //decreasing or increasing trend
            if(antepenultimateSysRecord!=null && ((record.getMeasurementValue()-previousSysRecord.getMeasurementValue()>10 && previousSysRecord.getMeasurementValue()-antepenultimateSysRecord.getMeasurementValue()>10)
                    ||(record.getMeasurementValue()-previousSysRecord.getMeasurementValue()< -10 && previousSysRecord.getMeasurementValue()-antepenultimateSysRecord.getMeasurementValue()< -10))) {
                generator.triggerAlert(new BloodPressureAlertFactory().createAlert(Integer.toString(patientId),"Decreasing or increasing blood pressure trend",record.getTimestamp()));
                break;
            }
            antepenultimateSysRecord = previousSysRecord;
            previousSysRecord = record;
        }
        //Same method
        PatientRecord antepenultimateDisRecord = null;
        PatientRecord previousDisRecord =null;
        for (PatientRecord record : DiastolicPressureRecords) {

            if (record.getMeasurementValue()>120||record.getMeasurementValue()<60) {
                generator.triggerAlert(new BloodPressureAlertFactory().createAlert(Integer.toString(patientId),"Critical blood pressure",record.getTimestamp()));
                break;
            }
            if(antepenultimateDisRecord!=null && ((record.getMeasurementValue()-previousDisRecord.getMeasurementValue()>10 && previousDisRecord.getMeasurementValue()-antepenultimateDisRecord.getMeasurementValue()>10)
                    ||(record.getMeasurementValue()-previousDisRecord.getMeasurementValue()< -10 && previousDisRecord.getMeasurementValue()-antepenultimateDisRecord.getMeasurementValue()< -10))) {
                generator.triggerAlert(new BloodPressureAlertFactory().createAlert(Integer.toString(patientId),"Decreasing or increasing blood pressure trend",record.getTimestamp()));
                break;
            }
            antepenultimateDisRecord = previousDisRecord;
            previousDisRecord = record;
        }
    }
}
