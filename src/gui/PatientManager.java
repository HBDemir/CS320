package gui;

import DAO.UserDAO;
import Library.Patient;
import Library.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PatientManager extends JFrame {
    private UserDAO userDAO = new UserDAO();
    private JTable patientTable;
    private DefaultTableModel tableModel;

    public PatientManager() {
        setTitle("Manage Patients");
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form for adding patients
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JLabel ssnLabel = new JLabel("SSN:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel surnameLabel = new JLabel("Surname:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel phoneLabel = new JLabel("Phone:");

        JTextField ssnField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField surnameField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JTextField phoneField = new JTextField(10);

        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ssn = ssnField.getText();
                String name = nameField.getText();
                String surname = surnameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();

                if (!ssn.isEmpty() && !name.isEmpty() && !surname.isEmpty()) {
                    Patient patient = new Patient(ssn, name, surname, "Patient", email, phone);
                    boolean success = userDAO.createUser(patient);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Patient added successfully!");
                        refreshPatientTable();
                        ssnField.setText("");
                        nameField.setText("");
                        surnameField.setText("");
                        emailField.setText("");
                        phoneField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add patient.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill required fields (SSN, Name, Surname).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        formPanel.add(ssnLabel);
        formPanel.add(ssnField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(surnameLabel);
        formPanel.add(surnameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        // Table for displaying patients
        String[] columnNames = {"SSN", "Name", "Surname", "Email", "Phone"};
        tableModel = new DefaultTableModel(columnNames, 0);
        patientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        refreshPatientTable();

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void refreshPatientTable() {
        tableModel.setRowCount(0);
        ArrayList<User> patients = userDAO.getUsersByRole("Patient");
        for (User patient : patients) {
            Object[] row = {
                patient.getSsn(),
                patient.getUserName(),
                patient.getUserSurname(),
                patient.getE_mail(),
                patient.getPhone()
            };
            tableModel.addRow(row);
        }
    }
}