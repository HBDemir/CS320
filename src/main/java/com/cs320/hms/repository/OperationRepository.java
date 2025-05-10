package com.cs320.hms.repository;

import com.cs320.hms.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Integer> {
    List<Operation> findByPatient_SsnOrderByStartDateDesc(String patientSsn);
}
