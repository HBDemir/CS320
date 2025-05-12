package gui;

import DAO.UserDAO;
import Library.Doctor;
import Library.Nurse;
import Library.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JFrame {
    private JTextField ssnField;
    private JPasswordField passwordField;

    public LoginPanel() {
        setTitle("Hospital Login");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("SSN:"));
        ssnField = new JTextField();
        formPanel.add(ssnField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton signUpBtn = new JButton("Sign Up");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginBtn);
        buttonPanel.add(signUpBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loginBtn.addActionListener((ActionEvent e) -> {
            String ssn = ssnField.getText().trim();
            String password = new String(passwordField.getPassword());

            UserDAO userDAO = new UserDAO();
            User user = userDAO.authenticateUser(ssn, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
                return;
            }

            if (user instanceof Doctor) {
                dispose();
                new DoctorWindow((Doctor) user);
            } else if (user instanceof Nurse) {
                dispose();
                new NurseWindow((Nurse) user);
            } else {
                JOptionPane.showMessageDialog(this, "Access denied.");
            }
        });

        signUpBtn.addActionListener(e -> new SignUpDialog(this));

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPanel();
    }
}