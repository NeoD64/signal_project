package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private NotificationService notificationService;
    private List<Alert> alertLog = new ArrayList<>();

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage,NotificationService notificationService) {
        this.dataStorage = dataStorage;
        this.notificationService = notificationService;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        int patientId = patient.getPatientId();
        long now = System.currentTimeMillis();
        long pastHour = now - 3600000;

        List<PatientRecord> records = dataStorage.getRecords(patientId, pastHour, now);
        //separating all the recordtype to make it easier to manipulate
        List<PatientRecord> SystolicPressureRecords= records.stream()
                .filter(record-> record.getRecordType().equalsIgnoreCase("SystolicPressure"))
                .collect(Collectors.toList()) ;
        List<PatientRecord> DiastolicPressureRecords= records.stream()
                .filter(record->record.getRecordType().equalsIgnoreCase("DiastolicPressure"))
                .collect(Collectors.toList()) ;

        List<PatientRecord> SaturationRecords= records.stream()
                .filter(record->record.getRecordType().equalsIgnoreCase("Saturation"))
                .collect(Collectors.toList()) ;


        List<PatientRecord> ECGRecords= records.stream()
                .filter(record->record.getRecordType().equalsIgnoreCase("ECG"))
                .collect(Collectors.toList());

        //checking for hypo before because it would trigger the other two alert befor



        checkBloodPressureAlerts(patientId, SystolicPressureRecords,DiastolicPressureRecords);
        checkSaturationAlerts(patientId, SaturationRecords);
        checkHypotensiveHypoxemia(patientId,SystolicPressureRecords,SaturationRecords);
        checkECGAlerts(patientId, ECGRecords);
    }


    private void checkBloodPressureAlerts(int patientId, List<PatientRecord> SystolicPressureRecords, List<PatientRecord> DiastolicPressureRecords ) {

        PatientRecord antepenultimateSysRecord = null;
        PatientRecord previousSysRecord =null;
        for (PatientRecord record : SystolicPressureRecords) {
            // critical threshold
            if (record.getMeasurementValue()>180 ||record.getMeasurementValue()<90) {
                triggerAlert(new Alert(Integer.toString(patientId),"Critical blood pressure",record.getTimestamp()));
                break;
            }
            //decreasing or increasing trend
            if(antepenultimateSysRecord!=null && ((record.getMeasurementValue()-previousSysRecord.getMeasurementValue()>10 && previousSysRecord.getMeasurementValue()-antepenultimateSysRecord.getMeasurementValue()>10)
                    ||(record.getMeasurementValue()-previousSysRecord.getMeasurementValue()< -10 && previousSysRecord.getMeasurementValue()-antepenultimateSysRecord.getMeasurementValue()< -10))) {
                triggerAlert(new Alert(Integer.toString(patientId),"Decreasing or increasing blood pressure trend",record.getTimestamp()));
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
                triggerAlert(new Alert(Integer.toString(patientId),"Critical blood pressure",record.getTimestamp()));
                break;
            }
            if(antepenultimateDisRecord!=null && ((record.getMeasurementValue()-previousDisRecord.getMeasurementValue()>10 && previousDisRecord.getMeasurementValue()-antepenultimateDisRecord.getMeasurementValue()>10)
                    ||(record.getMeasurementValue()-previousDisRecord.getMeasurementValue()< -10 && previousDisRecord.getMeasurementValue()-antepenultimateDisRecord.getMeasurementValue()< -10))) {
                triggerAlert(new Alert(Integer.toString(patientId),"Decreasing or increasing blood pressure trend",record.getTimestamp()));
                break;
            }
            antepenultimateDisRecord = previousDisRecord;
            previousDisRecord = record;
        }
    }

    private void checkSaturationAlerts(int patientId, List<PatientRecord> SaturationRecords) {

        for (PatientRecord record : SaturationRecords) {
            // Check for <92%
            if(record.getMeasurementValue()<92) {
                triggerAlert(new Alert(Integer.toString(patientId),"Lack of oxygen Saturation",record.getTimestamp()));
                break;
            }
            // rapid 5% drop within 10 minutes
            for(PatientRecord record2 : SaturationRecords.stream().filter(r->r.getTimestamp()<=record.getTimestamp()+600000 && r.getTimestamp()>=record.getTimestamp() ).collect(Collectors.toList())) {
                if(record.getMeasurementValue()-record2.getMeasurementValue()>=5) {
                    triggerAlert(new Alert(Integer.toString(patientId),"Critical drop in Saturation",record2.getTimestamp()));
                    break;
                }
            }
        }
    }

    private void checkHypotensiveHypoxemia(int patientId,  List<PatientRecord> SystolicPressureRecords ,List<PatientRecord> SaturationRecords ) {


        for (PatientRecord record : SystolicPressureRecords) {
            // here I decide arbitrarily that two measurement one minute from each other is close enough
            for (PatientRecord record2 : SaturationRecords.stream().filter(r->r.getTimestamp()<=record.getTimestamp()+60000 && r.getTimestamp()>=record.getTimestamp()-60000).collect(Collectors.toList())) {
                if(record.getMeasurementValue()<90 && record2.getMeasurementValue()<92) {
                    triggerAlert(new Alert(Integer.toString(patientId),"Hypotensive Hypoxemia",Math.max(record.getTimestamp(), record2.getTimestamp())));
                    break;
                }
            }

        }
    }

    private void checkECGAlerts(int patientId, List<PatientRecord> ECGRecords) {
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
                    triggerAlert(new Alert(Integer.toString(patientId),"Critical ECG",record.getTimestamp()));
                    break;
                }
            }
        }
    }



    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */


    private void triggerAlert(Alert alert) {
        System.out.println("!!! Alert triggered!!!");
        System.out.println(alert.getPatientId());
        System.out.println(alert.getCondition());
        System.out.println(alert.getTimestamp());

        alertLog.add(alert);

        notificationService.notify(alert);

    }
}
