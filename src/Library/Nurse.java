package Library;

import DAO.AppointmentDAO;
import DAO.UserDAO;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Nurse extends User {
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private UserDAO userDAO = new UserDAO();

    public Nurse(String ssn, String userName, String userSurname,
                 String userRole, String e_mail, String phone, String password) {
        super(ssn, userName, userSurname, "Nurse", e_mail, phone, password);
    }

    public void sendEmail(String e_mail, Appointment appointment) {
        try {
            String subject = "Appointment Confirmation";
            String body = "Dear Patient,\n\n" +
                    "Your appointment is confirmed for " + appointment.getDate() +
                    " with Dr. " + appointment.getDoctor().getUserName() + ".\n\n" +
                    "Best regards,\nClinic Team";

            String uriStr = String.format("mailto:%s?subject=%s&body=%s",
                    e_mail,
                    URLEncoder.encode(subject, "UTF-8"),
                    URLEncoder.encode(body, "UTF-8")
            );

            URI mailto = new URI(uriStr);

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
                Desktop.getDesktop().mail(mailto);
            } else {
                System.err.println("Desktop mail feature is not supported.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Appointment> viewAppointments(Patient patient) {
        return appointmentDAO.getAppointmentsByPatient(patient.getSsn());
    }

    public void rescheduleAppointment(Appointment appointment, String date) {
        // Get all appointments (we need the ID)
        ArrayList<Appointment> appointments = appointmentDAO.getAllAppointments();
        int appointmentId = -1;

        // Find the matching appointment to get its ID
        for (int i = 0; i < appointments.size(); i++) {
            Appointment app = appointments.get(i);
            if (app.getDate().equals(appointment.getDate()) &&
                    app.getDoctor().getSsn().equals(appointment.getDoctor().getSsn()) &&
                    app.getPatient().getSsn().equals(appointment.getPatient().getSsn())) {
                // Found the appointment, now reschedule it
                appointmentDAO.updateAppointmentDate(appointmentId, date);
                appointment.setDate(date); // Update the object too
                break;
            }
        }
    }

    public void recordPatient(String ssn, String userName, String userSurname,
                              String userRole, String e_mail, String phone) {
        Patient patient = new Patient(ssn, userName, userSurname, userRole, e_mail, phone);
        userDAO.createUser(patient);
    }

    public boolean createAppointment(Appointment appointment) {
        return appointmentDAO.createAppointment(appointment);
    }

    public boolean deleteAppointment(int appointmentId) {
        return appointmentDAO.deleteAppointment(appointmentId);
    }

    public ArrayList<Appointment> viewAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }
}