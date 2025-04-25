package Library;

public class Appointment {
    private String date;
    private Doctor doctor;
    private Patient patient;
    public Appointment(String date,Doctor doctor,Patient patient){
        this.date=date;
        this.doctor=doctor;
        this.patient=patient;

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

