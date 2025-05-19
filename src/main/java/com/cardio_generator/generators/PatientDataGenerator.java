package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface for generating patient data.
 * Implementations of this interface should provide the logic to generate
 * patient data and output it using the specified OutputStrategy.
 */
public interface PatientDataGenerator {
    /**
     * Generates data for a specific patient.
     *
     * @param patientId The ID of the patient for whom data is generated.
     * @param outputStrategy The strategy used to output the generated data.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
