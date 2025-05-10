package com.cs320.hms.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Prescription extends Treatment {

    private double prescriptionID;

    @ElementCollection
    @CollectionTable(name = "Medications", joinColumns = @JoinColumn(name = "treatment_id"))
    private List<Medication> medications = new ArrayList<>();

    public Prescription() {}

    public Prescription(String startDate, String endDate, double prescriptionID,
                        List<String> medicationName, List<String> dosage) {
        setStartDate(startDate);
        setEndDate(endDate);
        this.prescriptionID = prescriptionID;
        for (int i = 0; i < medicationName.size(); i++) {
            this.medications.add(new Medication(medicationName.get(i), dosage.get(i)));
        }
    }

    public double getPrescriptionID() { return prescriptionID; }
    public void setPrescriptionID(double prescriptionID) { this.prescriptionID = prescriptionID; }

    public List<Medication> getMedications() { return medications; }
    public void setMedications(List<Medication> medications) { this.medications = medications; }

    @Transient
    public List<String[]> getMedication() {
        List<String[]> list = new ArrayList<>();
        for (Medication m : medications) {
            list.add(new String[]{m.getMedicationName(), m.getDosage()});
        }
        return list;
    }
}
