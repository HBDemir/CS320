package com.cs320.hms.repository;

import com.cs320.hms.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {
    List<Treatment> findByPatient_SsnOrderByStartDateDesc(String patientSsn);
}
