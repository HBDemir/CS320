package Library;

public class Appointment {
    private int appointmentId; // ✅ Yeni alan
    private String date;
    private Doctor doctor;
    private Patient patient;

    // ✅ ID'li constructor
    public Appointment(int appointmentId, String date, Doctor doctor, Patient patient) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.doctor = doctor;
        this.patient = patient;
    }

    // ✅ ID'siz eski constructor (geriye dönük uyum için)
    public Appointment(String date, Doctor doctor, Patient patient) {
        this.date = date;
        this.doctor = doctor;
        this.patient = patient;
    }

    // ✅ Getter ve Setter'lar
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDate() {
        return date;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}