package gui;

import DAO.UserDAO;
import Library.Doctor;
import Library.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DoctorManager extends JFrame {
    private UserDAO userDAO = new UserDAO();
    private JTable doctorTable;
    private DefaultTableModel tableModel;

    public DoctorManager() {
        setTitle("Manage Doctors");
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form for adding doctors
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JLabel ssnLabel = new JLabel("SSN:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel surnameLabel = new JLabel("Surname:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel passwordLabel = new JLabel("Password:");

        JTextField ssnField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField surnameField = new JTextField(10);
        JTextField emailField = new JTextField(10);
        JTextField phoneField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JButton addButton = new JButton("Add Doctor");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ssn = ssnField.getText();
                String name = nameField.getText();
                String surname = surnameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String password = new String(passwordField.getPassword());

                if (!ssn.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !password.isEmpty()) {
                    Doctor doctor = new Doctor(ssn, name, surname, "Doctor", email, phone, password);
                    boolean success = userDAO.createUser(doctor);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Doctor added successfully!");
                        refreshDoctorTable();
                        ssnField.setText("");
                        nameField.setText("");
                        surnameField.setText("");
                        emailField.setText("");
                        phoneField.setText("");
                        passwordField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add doctor.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill required fields (SSN, Name, Surname, Password).", "Error", JOptionPane.ERROR_MESSAGE);
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
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        // Table for displaying doctors
        String[] columnNames = {"SSN", "Name", "Surname", "Email", "Phone"};
        tableModel = new DefaultTableModel(columnNames, 0);
        doctorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        refreshDoctorTable();

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void refreshDoctorTable() {
        tableModel.setRowCount(0);
        ArrayList<User> doctors = userDAO.getUsersByRole("Doctor");
        for (User doctor : doctors) {
            Object[] row = {
                doctor.getSsn(),
                doctor.getUserName(),
                doctor.getUserSurname(),
                doctor.getE_mail(),
                doctor.getPhone()
            };
            tableModel.addRow(row);
        }
    }
}