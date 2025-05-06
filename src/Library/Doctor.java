package Library;

import DAO.AppointmentDAO;
import DAO.TreatmentDAO;
import DAO.UserDAO;

import java.util.ArrayList;

public class Doctor extends User {
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private TreatmentDAO treatmentDAO = new TreatmentDAO();
    private UserDAO userDAO = new UserDAO();

    public Doctor(String ssn, String userName, String userSurname,
                  String userRole, String e_mail, String phone, String password) {
        super(ssn, userName, userSurname, "Doctor", e_mail, phone, password);
    }

    public ArrayList<Patient> viewRecords() {
        ArrayList<User> users = userDAO.getUsersByRole("Patient");
        ArrayList<Patient> patients = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Patient) {
                patients.add((Patient) user);
            }
        }

        return patients;
    }

    public ArrayList<Appointment> viewAppointments() {
        return appointmentDAO.getAppointmentsByDoctor(this.getSsn());
    }

    public Object viewVitals(String type, Patient patient) {
        if (type.equals("Prescription")) {
            return treatmentDAO.getPrescriptionsByPatient(patient.getSsn());
        } else {
            return treatmentDAO.getOperationsByPatient(patient.getSsn());
        }
    }

    public boolean createPrescription(Prescription prescription, Patient patient) {
        return treatmentDAO.createPrescription(prescription, patient.getSsn());
    }

    public boolean createOperation(Operation operation, Patient patient) {
        return treatmentDAO.createOperation(operation, patient.getSsn());
    }

    public boolean updateTreatment(int treatmentId, String startDate, String endDate) {
        return treatmentDAO.updateTreatmentDates(treatmentId, startDate, endDate);
    }
}