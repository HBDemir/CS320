package gui;

import DAO.UserDAO;
import Library.Patient;
import Library.User;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PatientManager extends JFrame {
    private final UserDAO userDAO = new UserDAO();
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    public PatientManager() {
        setTitle("Manage Patients");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        layoutComponents();
        refreshPatientTable();
    }
    
    private void initComponents() {
        tableModel = new DefaultTableModel(
            new Object[]{"SSN", "Name", "Surname", "Email", "Phone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        searchField = new JTextField(20);
    }
    
    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchPatients());
        searchPanel.add(searchButton);
        
        // Table
        JScrollPane scrollPane = new JScrollPane(patientTable);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener(e -> showAddPatientDialog());
        
        JButton editButton = new JButton("Edit Selected");
        editButton.addActionListener(e -> editSelectedPatient());
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedPatient());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshPatientTable());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void refreshPatientTable() {
        tableModel.setRowCount(0);
        ArrayList<User> patients = userDAO.getUsersByRole("Patient");
        
        for (User patient : patients) {
            tableModel.addRow(new Object[]{
                patient.getSsn(),
                patient.getUserName(),
                patient.getUserSurname(),
                patient.getE_mail(),
                patient.getPhone()
            });
        }
    }
    
    private void searchPatients() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        
        if (searchTerm.isEmpty()) {
            refreshPatientTable();
            return;
        }
        
        tableModel.setRowCount(0);
        ArrayList<User> patients = userDAO.getUsersByRole("Patient");
        
        for (User patient : patients) {
            if (patient.getSsn().toLowerCase().contains(searchTerm) ||
                patient.getUserName().toLowerCase().contains(searchTerm) ||
                patient.getUserSurname().toLowerCase().contains(searchTerm) ||
                (patient.getE_mail() != null && patient.getE_mail().toLowerCase().contains(searchTerm)) ||
                (patient.getPhone() != null && patient.getPhone().toLowerCase().contains(searchTerm))) {
                
                tableModel.addRow(new Object[]{
                    patient.getSsn(),
                    patient.getUserName(),
                    patient.getUserSurname(),
                    patient.getE_mail(),
                    patient.getPhone()
                });
            }
        }
    }
    
    private void showAddPatientDialog() {
        JDialog dialog = new JDialog(this, "Add New Patient", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
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
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (validateAndSavePatient(
                ssnField.getText(),
                nameField.getText(),
                surnameField.getText(),
                emailField.getText(),
                phoneField.getText()
            )) {
                dialog.dispose();
                refreshPatientTable();
            }
        });
        
        panel.add(new JLabel());
        panel.add(saveButton);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private boolean validateAndSavePatient(String ssn, String name, String surname, 
                                         String email, String phone) {
        if (ssn.isEmpty() || name.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "SSN, Name and Surname are required",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Patient patient = new Patient(ssn, name, surname, "Patient", email, phone);
            boolean success = userDAO.createUser(patient);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Patient added successfully!");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add patient",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void editSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to edit",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String ssn = (String) tableModel.getValueAt(selectedRow, 0);
        User patient = userDAO.getUserBySSN(ssn);
        
        if (patient != null) {
            showEditPatientDialog(patient);
        }
    }
    
    private void showEditPatientDialog(User patient) {
        JDialog dialog = new JDialog(this, "Edit Patient", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField ssnField = new JTextField(patient.getSsn());
        ssnField.setEditable(false);
        JTextField nameField = new JTextField(patient.getUserName());
        JTextField surnameField = new JTextField(patient.getUserSurname());
        JTextField emailField = new JTextField(patient.getE_mail());
        JTextField phoneField = new JTextField(patient.getPhone());
        
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
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            if (validateAndUpdatePatient(
                patient,
                nameField.getText(),
                surnameField.getText(),
                emailField.getText(),
                phoneField.getText()
            )) {
                dialog.dispose();
                refreshPatientTable();
            }
        });
        
        panel.add(new JLabel());
        panel.add(saveButton);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private boolean validateAndUpdatePatient(User patient, String name, String surname, 
                                           String email, String phone) {
        if (name.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Surname are required",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            patient.setUserName(name);
            patient.setUserSurname(surname);
            patient.setE_mail(email);
            patient.setPhone(phone);
            
            boolean success = userDAO.updateUser(patient);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Patient updated successfully!");
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update patient",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void deleteSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String ssn = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this patient?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (userDAO.deleteUser(ssn)) {
                    JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
                    refreshPatientTable();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting patient: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}