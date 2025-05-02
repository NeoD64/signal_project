package com.alerts;

import com.alerts.alertTypes.Alert;
import com.alerts.alertTypes.BloodPressureAlert;
import com.alerts.strategies.BloodPressureStrategy;
import com.alerts.strategies.HeartRateStrategy;
import com.alerts.strategies.OxygenSaturationStrategy;
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
    public void evaluateData(Patient patient,long currentTime) {
        int patientId = patient.getPatientId();
        long pastHour=currentTime-60*60*1000;
        List<PatientRecord> records = dataStorage.getRecords(patientId,pastHour,currentTime);

        new BloodPressureStrategy().checkAlert(patientId,records,this);
        new HeartRateStrategy().checkAlert(patientId,records,this);
        new OxygenSaturationStrategy().checkAlert(patientId,records,this);


    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */


    public void triggerAlert(Alert alert) {
        System.out.println("!!! Alert triggered!!!");
        System.out.println(alert.getPatientId());
        System.out.println(alert.getCondition());
        System.out.println(alert.getTimestamp());

        alertLog.add(alert);

        notificationService.notify(alert);

    }
    public List<Alert> getAlertLog() {
        return alertLog;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }
}
