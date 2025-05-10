package com.cs320.hms.service;

import com.cs320.hms.model.*;
import com.cs320.hms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepo;
    private final OperationRepository operationRepo;
    private final PrescriptionRepository prescriptionRepo;

    public TreatmentService(TreatmentRepository treatmentRepo,
                            OperationRepository operationRepo,
                            PrescriptionRepository prescriptionRepo) {
        this.treatmentRepo = treatmentRepo;
        this.operationRepo = operationRepo;
        this.prescriptionRepo = prescriptionRepo;
    }

    @Transactional
    public Operation createOperation(Operation operation, Patient patient) {
        operation.setPatient(patient);
        return operationRepo.save(operation);
    }

    @Transactional
    public Prescription createPrescription(Prescription prescription, Patient patient) {
        prescription.setPatient(patient);
        return prescriptionRepo.save(prescription);
    }

    public List<Treatment> getAllByPatient(String ssn) {
        return treatmentRepo.findByPatient_SsnOrderByStartDateDesc(ssn);
    }

    public List<Operation> getOperationsByPatient(String ssn) {
        return operationRepo.findByPatient_SsnOrderByStartDateDesc(ssn);
    }

    public List<Prescription> getPrescriptionsByPatient(String ssn) {
        return prescriptionRepo.findByPatient_SsnOrderByStartDateDesc(ssn);
    }

    public Treatment getById(int id) {
        return treatmentRepo.findById(id).orElse(null);
    }

    @Transactional
    public boolean updateTreatmentDates(int id, String newStart, String newEnd) {
        Treatment t = getById(id);
        if (t != null) {
            t.setStartDate(newStart);
            t.setEndDate(newEnd);
            treatmentRepo.save(t);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteTreatment(int id) {
        if (treatmentRepo.existsById(id)) {
            treatmentRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
