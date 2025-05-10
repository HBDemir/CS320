package com.cs320.hms.service;

import com.cs320.hms.model.Appointment;
import com.cs320.hms.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;

    public AppointmentService(AppointmentRepository appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    public Appointment create(Appointment appointment) {
        return appointmentRepo.save(appointment);
    }

    public Appointment getById(int id) {
        return appointmentRepo.findById(id).orElse(null);
    }

    public List<Appointment> getByDoctor(String doctorSsn) {
        return appointmentRepo.findByDoctorSsnOrderByDate(doctorSsn);
    }

    public List<Appointment> getByPatient(String patientSsn) {
        return appointmentRepo.findByPatientSsnOrderByDate(patientSsn);
    }

    public List<Appointment> getAll() {
        return appointmentRepo.findAll();
    }

    public boolean updateDate(int id, String newDate) {
        Appointment app = getById(id);
        if (app != null) {
            app.setDate(newDate);
            appointmentRepo.save(app);
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        if (appointmentRepo.existsById(id)) {
            appointmentRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
