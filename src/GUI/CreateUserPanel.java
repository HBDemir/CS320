package GUI;

import DAO.UserDAO;
import Library.Doctor;
import Library.Nurse;
import Library.Patient;
import Library.User;

import javax.swing.*;
import java.awt.*;

public class CreateUserPanel extends JFrame {
    private JTextField ssnField, nameField, surnameField, emailField, phoneField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public CreateUserPanel() {
        setTitle("Create User");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2, 10, 10));

        // Fields
        add(new JLabel("SSN:"));
        ssnField = new JTextField();
        add(ssnField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Surname:"));
        surnameField = new JTextField();
        add(surnameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"Doctor", "Nurse", "Patient"});
        add(roleBox);

        JButton createButton = new JButton("Create User");
        createButton.addActionListener(e -> createUser());
        add(createButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton);

        setVisible(true);
    }

    private void createUser() {
        String ssn = ssnField.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        User user = switch (role) {
            case "Doctor" -> new Doctor(ssn, name, surname, role, email, phone, password);
            case "Nurse" -> new Nurse(ssn, name, surname, role, email, phone, password);
            case "Patient" -> new Patient(ssn, name, surname, role, email, phone);
            default -> null;
        };

        if (user != null) {
            UserDAO dao = new UserDAO();
            boolean success = dao.createUser(user);
            if (success) {
                JOptionPane.showMessageDialog(this, "User created successfully.");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create user.");
            }
        }
    }

    private void clearFields() {
        ssnField.setText("");
        nameField.setText("");
        surnameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passwordField.setText("");
        roleBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreateUserPanel::new);
    }
}

