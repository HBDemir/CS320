package gui;

import Library.Nurse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterPatientDialog extends JDialog {
    public RegisterPatientDialog(JFrame parent, Nurse nurse) {
        super(parent, "Register Patient", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(6, 2));
        JTextField ssnField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        panel.add(new JLabel("SSN:"));
        panel.add(ssnField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Surname:"));
        panel.add(surnameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        JButton submitBtn = new JButton("Register");
        add(panel, BorderLayout.CENTER);
        add(submitBtn, BorderLayout.SOUTH);

        submitBtn.addActionListener((ActionEvent e) -> {
            nurse.recordPatient(
                    ssnField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    "Patient",
                    emailField.getText(),
                    phoneField.getText()
            );
            JOptionPane.showMessageDialog(this, "Patient registered");
            dispose();
        });

        setVisible(true);
    }
}