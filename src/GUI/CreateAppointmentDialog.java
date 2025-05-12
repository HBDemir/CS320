package GUI;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import Library.Appointment;
import Library.Doctor;
import Library.Nurse;
import Library.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CreateAppointmentDialog extends JDialog {
    public CreateAppointmentDialog(JFrame parent, Nurse nurse) {
        super(parent, "Create Appointment", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField dateField = new JTextField();
        JTextField patientSsnField = new JTextField();
        JTextField doctorSsnField = new JTextField();

        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Patient SSN:"));
        panel.add(patientSsnField);
        panel.add(new JLabel("Doctor SSN:"));
        panel.add(doctorSsnField);

        JButton submitBtn = new JButton("Create");
        add(panel, BorderLayout.CENTER);
        add(submitBtn, BorderLayout.SOUTH);

        submitBtn.addActionListener((ActionEvent e) -> {
            String date = dateField.getText();
            String pssn = patientSsnField.getText();
            String dssn = doctorSsnField.getText();

            Patient patient = (Patient) new UserDAO().getUserBySSN(pssn);
            Doctor doctor = (Doctor) new UserDAO().getUserBySSN(dssn);
            if (patient == null || doctor == null) {
                JOptionPane.showMessageDialog(this, "Doctor or patient not found");
                return;
            }

            Appointment appointment = new Appointment(date, doctor, patient);
            boolean success = new AppointmentDAO().createAppointment(appointment);
            if (success) {
                JOptionPane.showMessageDialog(this, "Appointment created successfully");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create appointment");
            }
        });

        setVisible(true);
    }
}





