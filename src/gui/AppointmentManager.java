package gui;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import Library.Appointment;
import Library.Doctor;
import Library.Patient;
import Library.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentManager extends JFrame {
    private final UserDAO userDAO = new UserDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private JComboBox<Doctor> doctorComboBox;
    private JComboBox<Patient> patientComboBox;
    private JFormattedTextField dateField;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public AppointmentManager() {
        setTitle("Manage Appointments");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        layoutComponents();
        refreshData();
    }

    private void initComponents() {
        doctorComboBox = new JComboBox<>();
        patientComboBox = new JComboBox<>();
        dateField = new JFormattedTextField();
        dateField.setColumns(10);
        
        tableModel = new DefaultTableModel(
            new Object[]{"Date", "Doctor", "Patient"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        appointmentTable = new JTable(tableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.add(new JLabel("Doctor:"));
        formPanel.add(doctorComboBox);
        formPanel.add(new JLabel("Patient:"));
        formPanel.add(patientComboBox);
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        formPanel.add(dateField);
        
        JButton createButton = new JButton("Create Appointment");
        createButton.addActionListener(e -> createAppointment());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshData());
        
        formPanel.add(createButton);
        formPanel.add(refreshButton);

        // Table panel
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedAppointment());
        buttonPanel.add(deleteButton);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void refreshData() {
        List<Doctor> doctors = userDAO.getUsersByRole("Doctor").stream()
            .filter(user -> user instanceof Doctor)
            .map(user -> (Doctor) user)
            .collect(Collectors.toList());
        
        List<Patient> patients = userDAO.getUsersByRole("Patient").stream()
            .filter(user -> user instanceof Patient)
            .map(user -> (Patient) user)
            .collect(Collectors.toList());

        doctorComboBox.removeAllItems();
        patientComboBox.removeAllItems();
        doctors.forEach(doctorComboBox::addItem);
        patients.forEach(patientComboBox::addItem);
        
       
        refreshAppointmentTable();
    }

    private void refreshAppointmentTable() {
        tableModel.setRowCount(0);
        ArrayList<Appointment> appointments = appointmentDAO.getAllAppointments();
        
        for (Appointment app : appointments) {
            tableModel.addRow(new Object[]{
                app.getDate(),
                app.getDoctor().getUserName() + " (" + app.getDoctor().getSsn() + ")",
                app.getPatient().getUserName() + " (" + app.getPatient().getSsn() + ")"
            });
        }
    }

    private boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void createAppointment() {
        Doctor doctor = (Doctor) doctorComboBox.getSelectedItem();
        Patient patient = (Patient) patientComboBox.getSelectedItem();
        String dateStr = dateField.getText().trim();

        if (doctor == null || patient == null) {
            showError("Please select both doctor and patient");
            return;
        }

        if (!isValidDate(dateStr)) {
            showError("Invalid date format. Use YYYY-MM-DD");
            return;
        }

        try {
            Appointment appointment = new Appointment(dateStr, doctor, patient);
            if (appointmentDAO.createAppointment(appointment)) {
                showSuccess("Appointment created successfully!");
                refreshData();
                dateField.setText("");
            } else {
                showError("Failed to create appointment");
            }
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void deleteSelectedAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select an appointment to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this appointment?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ArrayList<Appointment> appointments = appointmentDAO.getAllAppointments();
                if (selectedRow < appointments.size()) {
                    Appointment toDelete = appointments.get(selectedRow);
                    if (appointmentDAO.deleteAppointment(toDelete)) {
                        showSuccess("Appointment deleted successfully!");
                        refreshData();
                    }
                }
            } catch (Exception e) {
                showError("Error deleting appointment: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}