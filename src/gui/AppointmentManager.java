package gui;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import Library.Appointment;
import Library.Doctor;
import Library.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AppointmentManager extends JFrame {
    private UserDAO userDAO = new UserDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private JComboBox<Doctor> doctorComboBox;
    private JComboBox<Patient> patientComboBox;
    private JTextField dateField;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public AppointmentManager() {
        setTitle("Manage Appointments");
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form for creating appointments
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel doctorLabel = new JLabel("Select Doctor:");
        JLabel patientLabel = new JLabel("Select Patient:");
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");

        ArrayList<Doctor> doctors = (ArrayList<Doctor>) userDAO.getUsersByRole("Doctor");
        ArrayList<Patient> patients = (ArrayList<Patient>) userDAO.getUsersByRole("Patient");
        doctorComboBox = new JComboBox<>(doctors.toArray(new Doctor[0]));
        patientComboBox = new JComboBox<>(patients.toArray(new Patient[0]));
        dateField = new JTextField(10);

        JButton createButton = new JButton("Create Appointment");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Doctor selectedDoctor = (Doctor) doctorComboBox.getSelectedItem();
                Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
                String dateStr = dateField.getText();

                if (selectedDoctor != null && selectedPatient != null && !dateStr.isEmpty()) {
                    Appointment appointment = new Appointment(dateStr, selectedDoctor, selectedPatient);
                    boolean success = appointmentDAO.createAppointment(appointment);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Appointment created successfully!");
                        refreshAppointmentTable();
                        dateField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to create appointment.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        formPanel.add(doctorLabel);
        formPanel.add(doctorComboBox);
        formPanel.add(patientLabel);
        formPanel.add(patientComboBox);
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(new JLabel());
        formPanel.add(createButton);

        // Table for displaying appointments
        String[] columnNames = {"Date", "Doctor SSN", "Patient SSN"};
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        refreshAppointmentTable();

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void refreshAppointmentTable() {
        tableModel.setRowCount(0);
        ArrayList<Appointment> appointments = appointmentDAO.getAllAppointments();
        for (Appointment app : appointments) {
            Object[] row = {
                app.getDate(),
                app.getDoctor().getSsn(),
                app.getPatient().getSsn()
            };
            tableModel.addRow(row);
        }
    }
}