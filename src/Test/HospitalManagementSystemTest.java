package Test;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;

import Library.Doctor;
import Library.Nurse;
import Library.Patient;
import Library.Appointment;
import Library.Prescription;
import Library.Operation;
import Library.Treatment;
import Library.User;

public class HospitalManagementSystemTest {

    private Doctor testDoctor;
    private Nurse testNurse;
    private Patient testPatient;

    @Before
    public void setUp() {
        testDoctor = new Doctor("D12345", "John", "Smith", "Doctor", "john@hospital.com", "555-1234", "password");
        testNurse = new Nurse("N12345", "Jane", "Doe", "Nurse", "jane@hospital.com", "555-5678", "password");
        testPatient = new Patient("P12345", "Bob", "Johnson", "Patient", "bob@example.com", "555-9012");
        System.out.println("Test objects created successfully");
    }

    @Test
    public void testDoctorCreation() {
        System.out.println("Testing Doctor creation...");
        System.out.println("Doctor SSN: " + testDoctor.getSsn());
        System.out.println("Doctor Name: " + testDoctor.getUserName());
        System.out.println("Doctor Role: " + testDoctor.getUserRole());

        assertThat(testDoctor.getSsn(), is("D12345"));
        assertThat(testDoctor.getUserName(), is("John"));
        assertThat(testDoctor.getUserRole(), is("Doctor"));

        System.out.println("Doctor creation test passed!");
    }

    @Test
    public void testNurseCreation() {
        System.out.println("Testing Nurse creation...");
        System.out.println("Nurse SSN: " + testNurse.getSsn());
        System.out.println("Nurse Name: " + testNurse.getUserName());
        System.out.println("Nurse Role: " + testNurse.getUserRole());

        assertThat(testNurse.getSsn(), is("N12345"));
        assertThat(testNurse.getUserName(), is("Jane"));
        assertThat(testNurse.getUserRole(), is("Nurse"));

        System.out.println("Nurse creation test passed!");
    }

    @Test
    public void testPatientCreation() {
        System.out.println("Testing Patient creation...");
        System.out.println("Patient SSN: " + testPatient.getSsn());
        System.out.println("Patient Name: " + testPatient.getUserName());
        System.out.println("Patient Role: " + testPatient.getUserRole());

        assertThat(testPatient.getSsn(), is("P12345"));
        assertThat(testPatient.getUserName(), is("Bob"));
        assertThat(testPatient.getUserRole(), is("Patient"));

        System.out.println("Patient creation test passed!");
    }

    @Test
    public void testAppointmentCreation() {
        System.out.println("Testing Appointment creation...");

        String appointmentDate = "2025-05-20 10:00:00";
        Appointment appointment = new Appointment(appointmentDate, testDoctor, testPatient);

        System.out.println("Appointment Date: " + appointment.getDate());
        System.out.println("Appointment Doctor: " + appointment.getDoctor().getUserName());
        System.out.println("Appointment Patient: " + appointment.getPatient().getUserName());

        assertThat(appointment.getDate(), is(appointmentDate));
        assertThat(appointment.getDoctor(), is(sameInstance(testDoctor)));
        assertThat(appointment.getPatient(), is(sameInstance(testPatient)));

        System.out.println("Appointment creation test passed!");
    }

    @Test
    public void testAppointmentWithId() {
        System.out.println("Testing Appointment with ID...");

        int appointmentId = 101;
        String appointmentDate = "2025-05-20 10:00:00";
        Appointment appointment = new Appointment(appointmentId, appointmentDate, testDoctor, testPatient);

        System.out.println("Appointment ID: " + appointment.getAppointmentId());
        System.out.println("Appointment Date: " + appointment.getDate());

        assertThat(appointment.getAppointmentId(), is(appointmentId));
        assertThat(appointment.getDate(), is(appointmentDate));

        System.out.println("Appointment with ID test passed!");
    }

    @Test
    public void testPrescriptionCreation() {
        System.out.println("Testing Prescription creation...");

        String startDate = "2025-05-01";
        String endDate = "2025-05-14";
        double prescriptionId = 12345.0;

        ArrayList<String> medications = new ArrayList<>();
        medications.add("Amoxicillin");
        medications.add("Ibuprofen");

        ArrayList<String> dosages = new ArrayList<>();
        dosages.add("500mg twice daily");
        dosages.add("200mg as needed");

        Prescription prescription = new Prescription(startDate, endDate, prescriptionId, medications, dosages);

        System.out.println("Prescription Start Date: " + prescription.getStartDate());
        System.out.println("Prescription End Date: " + prescription.getEndDate());
        System.out.println("Prescription ID: " + prescription.getPrescriptionID());
        System.out.println("Medication Count: " + prescription.getMedication().size());

        assertThat(prescription.getStartDate(), is(startDate));
        assertThat(prescription.getEndDate(), is(endDate));
        assertThat(prescription.getPrescriptionID(), is(prescriptionId));
        assertThat(prescription.getMedication().size(), is(2));

        System.out.println("Prescription creation test passed!");
    }

    @Test
    public void testOperationCreation() {
        System.out.println("Testing Operation creation...");

        String startDate = "2025-06-15";
        String endDate = "2025-06-15";
        String operationName = "Appendectomy";

        Operation operation = new Operation(startDate, endDate, operationName);

        System.out.println("Operation Start Date: " + operation.getStartDate());
        System.out.println("Operation End Date: " + operation.getEndDate());
        System.out.println("Operation Name: " + operation.getOperationName());

        assertThat(operation.getStartDate(), is(startDate));
        assertThat(operation.getEndDate(), is(endDate));
        assertThat(operation.getOperationName(), is(operationName));

        System.out.println("Operation creation test passed!");
    }

    @Test
    public void testTreatmentBaseClass() {
        System.out.println("Testing Treatment base class...");

        Treatment treatment = new Treatment();
        String startDate = "2025-05-01";
        String endDate = "2025-05-14";

        treatment.setStartDate(startDate);
        treatment.setEndDate(endDate);

        System.out.println("Treatment Start Date: " + treatment.getStartDate());
        System.out.println("Treatment End Date: " + treatment.getEndDate());

        assertThat(treatment.getStartDate(), is(startDate));
        assertThat(treatment.getEndDate(), is(endDate));

        String newStartDate = "2025-05-02";
        String newEndDate = "2025-05-15";
        treatment.updateTreatment(newEndDate, newStartDate);

        System.out.println("Updated Treatment Start Date: " + treatment.getStartDate());
        System.out.println("Updated Treatment End Date: " + treatment.getEndDate());

        assertThat(treatment.getStartDate(), is(newStartDate));
        assertThat(treatment.getEndDate(), is(newEndDate));

        System.out.println("Treatment base class test passed!");
    }

    @Test
    public void testUserProfile() {
        System.out.println("Testing User profile...");

        String expectedProfile = "D12345,John,Smith,Doctor,john@hospital.com,555-1234,password";
        String actualProfile = testDoctor.viewProfile();

        System.out.println("Expected Profile: " + expectedProfile);
        System.out.println("Actual Profile: " + actualProfile);

        assertThat(actualProfile, is(expectedProfile));

        System.out.println("Modifying user profile...");
        testDoctor.modifyPofile("D98765", "Johnny", "Smithson", "Doctor",
                "johnny@hospital.com", "555-4321", "newpassword");

        System.out.println("Modified SSN: " + testDoctor.getSsn());
        System.out.println("Modified Name: " + testDoctor.getUserName());

        assertThat(testDoctor.getSsn(), is("D98765"));
        assertThat(testDoctor.getUserName(), is("Johnny"));

        System.out.println("User profile test passed!");
    }
}