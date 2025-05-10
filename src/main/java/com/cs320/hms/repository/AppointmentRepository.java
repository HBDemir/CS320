package com.cs320.hms.repository;

import com.cs320.hms.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByDoctorSsnOrderByDate(String doctorSsn);
    List<Appointment> findByPatientSsnOrderByDate(String patientSsn);
}
