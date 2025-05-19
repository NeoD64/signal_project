package com.cardio_generator.outputs;

/**
 * Interface for output strategies.
 * Implementations of this interface should provide the logic to output data
 * in different formats or to different destinations.
 */
public interface OutputStrategy {
    /**
     * Outputs the given data.
     *
     * @param patientId The ID of the patient.
     * @param timestamp The timestamp of the data.
     * @param label     The label for the data.
     * @param data      The actual data to be outputted.
     */
    void output(int patientId, long timestamp, String label, String data);
}
