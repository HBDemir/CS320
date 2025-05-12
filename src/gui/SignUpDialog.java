package GUI;

import DAO.UserDAO;
import Library.Doctor;
import Library.Nurse;
import Library.Patient;
import Library.User;

import javax.swing.*;
import java.awt.*;

public class SignUpDialog extends JDialog {
    public SignUpDialog(JFrame parent) {
        super(parent, "Sign Up", true);
        setSize(400, 400);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(8, 2));

        JTextField ssnField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Doctor", "Nurse"});

        panel.add(new JLabel("SSN:")); panel.add(ssnField);
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Surname:")); panel.add(surnameField);
        panel.add(new JLabel("Email:")); panel.add(emailField);
        panel.add(new JLabel("Phone:")); panel.add(phoneField);
        panel.add(new JLabel("Password:")); panel.add(passwordField);
        panel.add(new JLabel("Role:")); panel.add(roleBox);

        JButton signUpBtn = new JButton("Sign Up");
        add(panel, BorderLayout.CENTER);
        add(signUpBtn, BorderLayout.SOUTH);

        signUpBtn.addActionListener(e -> {
            String role = roleBox.getSelectedItem().toString();
            User user;
            if (role.equals("Doctor")) {
                user = new Doctor(ssnField.getText(), nameField.getText(), surnameField.getText(), role, emailField.getText(), phoneField.getText(), new String(passwordField.getPassword()));
            } else {
                user = new Nurse(ssnField.getText(), nameField.getText(), surnameField.getText(), role, emailField.getText(), phoneField.getText(), new String(passwordField.getPassword()));
            }
            boolean success = new UserDAO().createUser(user);
            JOptionPane.showMessageDialog(this, success ? "User created" : "Failed to create user");
            if (success) dispose();
        });

        setVisible(true);
    }
}