package gui;

import DAO.UserDAO;
import Library.Doctor;
import Library.User;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DoctorManager extends JFrame {
    private final UserDAO userDAO = new UserDAO();
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    
    public DoctorManager() {
        setTitle("Manage Doctors");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        layoutComponents();
        refreshDoctorTable();
    }
    
    private void initComponents() {
        tableModel = new DefaultTableModel(
            new Object[]{"SSN", "Name", "Surname", "Email", "Phone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        doctorTable = new JTable(tableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Table
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton addButton = new JButton("Add Doctor");
        addButton.addActionListener(e -> showAddDoctorDialog());
        
        JButton editButton = new JButton("Edit Selected");
        editButton.addActionListener(e -> editSelectedDoctor());
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedDoctor());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshDoctorTable());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void refreshDoctorTable() {
        tableModel.setRowCount(0);
        ArrayList<User> doctors = userDAO.getUsersByRole("Doctor");
        
        for (User doctor : doctors) {
            tableModel.addRow(new Object[]{
                doctor.getSsn(),
                doctor.getUserName(),
                doctor.getUserSurname(),
                doctor.getE_mail(),
                doctor.getPhone()
            });
        }
    }
    
    private void showAddDoctorDialog() {
        JDialog dialog = new JDialog(this, "Add New Doctor", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField ssnField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
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
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (validateAndSaveDoctor(
                ssnField.getText(),
                nameField.getText(),
                surnameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                new String(passwordField.getPassword())
            )) {
                dialog.dispose();
                refreshDoctorTable();
            }
        });
        
        panel.add(new JLabel());
        panel.add(saveButton);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private boolean validateAndSaveDoctor(String ssn, String name, String surname, 
                                        String email, String phone, String password) {
        if (ssn.isEmpty() || name.isEmpty() || surname.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "SSN, Name, Surname and Password are required",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Doctor doctor = new Doctor(ssn, name, surname, "Doctor", email, phone, password);
            boolean success = userDAO.createUser(doctor);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Doctor added successfully!");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add doctor",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void editSelectedDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to edit",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String ssn = (String) tableModel.getValueAt(selectedRow, 0);
        User doctor = userDAO.getUserBySSN(ssn);
        
        if (doctor != null) {
            showEditDoctorDialog(doctor);
        }
    }
    
    private void showEditDoctorDialog(User doctor) {
        JDialog dialog = new JDialog(this, "Edit Doctor", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField ssnField = new JTextField(doctor.getSsn());
        ssnField.setEditable(false);
        JTextField nameField = new JTextField(doctor.getUserName());
        JTextField surnameField = new JTextField(doctor.getUserSurname());
        JTextField emailField = new JTextField(doctor.getE_mail());
        JTextField phoneField = new JTextField(doctor.getPhone());
        JPasswordField passwordField = new JPasswordField();
        
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
        panel.add(new JLabel("New Password (leave empty to keep current):"));
        panel.add(passwordField);
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            if (validateAndUpdateDoctor(
                doctor,
                nameField.getText(),
                surnameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                new String(passwordField.getPassword())
            )) {
                dialog.dispose();
                refreshDoctorTable();
            }
        });
                
        panel.add(new JLabel());
        panel.add(saveButton);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private boolean validateAndUpdateDoctor(User doctor, String name, String surname, 
                                          String email, String phone, String newPassword) {
        if (name.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Surname are required",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            // Update user data
            doctor.setUserName(name);
            doctor.setUserSurname(surname);
            doctor.setE_mail(email);
            doctor.setPhone(phone);
            
            // Update password 
            if (!newPassword.isEmpty()) {
                doctor.setPassword(newPassword);
            }
            
            boolean success = userDAO.updateUser(doctor);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update doctor",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void deleteSelectedDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to delete",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String ssn = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this doctor?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (userDAO.deleteUser(ssn)) {
                    JOptionPane.showMessageDialog(this, "Doctor deleted successfully!");
                    refreshDoctorTable();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting doctor: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}