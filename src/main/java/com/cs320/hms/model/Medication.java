package com.cs320.hms.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Medication {

    private String medicationName;
    private String dosage;

    public Medication() {}

    public Medication(String medicationName, String dosage) {
        this.medicationName = medicationName;
        this.dosage = dosage;
    }

    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
}
