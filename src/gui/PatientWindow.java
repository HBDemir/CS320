package GUI;

import DAO.UserDAO;
import Library.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PatientWindow extends JFrame {

    private final UserDAO userDAO = new UserDAO();
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public PatientWindow() {
        setTitle("Patient List");
        setSize(800, 500);
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchPatients());
        searchPanel.add(searchButton);

        // Table
        JScrollPane scrollPane = new JScrollPane(patientTable);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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
}
