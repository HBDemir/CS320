package DAO;

import DBConnection.DBConnection;
import Library.Appointment;
import Library.Doctor;
import Library.Patient;

import java.sql.*;
import java.util.ArrayList;

public class AppointmentDAO {

    private UserDAO userDAO = new UserDAO();

    public boolean createAppointment(Appointment appointment) {
        String sql = "INSERT INTO Appointments (date, doctor_ssn, patient_ssn) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, appointment.getDate());
            pstmt.setString(2, appointment.getDoctor().getSsn());
            pstmt.setString(3, appointment.getPatient().getSsn());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error creating appointment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Appointment getAppointmentById(int appointmentId) {
        String sql = "SELECT * FROM Appointments WHERE appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String date = rs.getString("date");
                String doctorSsn = rs.getString("doctor_ssn");
                String patientSsn = rs.getString("patient_ssn");

                Doctor doctor = (Doctor) userDAO.getUserBySSN(doctorSsn);
                Patient patient = (Patient) userDAO.getUserBySSN(patientSsn);

                return new Appointment(date, doctor, patient);
            }

            return null;

        } catch (SQLException e) {
            System.err.println("Error retrieving appointment: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Appointment> getAppointmentsByDoctor(String doctorSsn) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointments WHERE doctor_ssn = ? ORDER BY date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, doctorSsn);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String date = rs.getString("date");
                String patientSsn = rs.getString("patient_ssn");

                Doctor doctor = (Doctor) userDAO.getUserBySSN(doctorSsn);
                Patient patient = (Patient) userDAO.getUserBySSN(patientSsn);

                appointments.add(new Appointment(date, doctor, patient));
            }

            return appointments;

        } catch (SQLException e) {
            System.err.println("Error retrieving doctor's appointments: " + e.getMessage());
            e.printStackTrace();
            return appointments;
        }
    }

    public ArrayList<Appointment> getAppointmentsByPatient(String patientSsn) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointments WHERE patient_ssn = ? ORDER BY date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patientSsn);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String date = rs.getString("date");
                String doctorSsn = rs.getString("doctor_ssn");

                Doctor doctor = (Doctor) userDAO.getUserBySSN(doctorSsn);
                Patient patient = (Patient) userDAO.getUserBySSN(patientSsn);

                appointments.add(new Appointment(date, doctor, patient));
            }

            return appointments;

        } catch (SQLException e) {
            System.err.println("Error retrieving patient's appointments: " + e.getMessage());
            e.printStackTrace();
            return appointments;
        }
    }

    public boolean updateAppointmentDate(int appointmentId, String newDate) {
        String sql = "UPDATE Appointments SET date = ? WHERE appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newDate);
            pstmt.setInt(2, appointmentId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating appointment date: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM Appointments WHERE appointment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointmentId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointments ORDER BY date";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String date = rs.getString("date");
                String doctorSsn = rs.getString("doctor_ssn");
                String patientSsn = rs.getString("patient_ssn");

                Doctor doctor = (Doctor) userDAO.getUserBySSN(doctorSsn);
                Patient patient = (Patient) userDAO.getUserBySSN(patientSsn);

                appointments.add(new Appointment(date, doctor, patient));
            }

            return appointments;

        } catch (SQLException e) {
            System.err.println("Error retrieving all appointments: " + e.getMessage());
            e.printStackTrace();
            return appointments;
        }
    }
}