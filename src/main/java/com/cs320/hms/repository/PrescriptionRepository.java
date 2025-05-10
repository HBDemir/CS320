package com.cs320.hms.repository;

import com.cs320.hms.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByPatient_SsnOrderByStartDateDesc(String patientSsn);
}
