package gui;

import DAO.UserDAO;
import Library.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DoctorWindow extends JFrame {

    private final UserDAO userDAO = new UserDAO();
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public DoctorWindow() {
        setTitle("Doctor List");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        layoutComponents();
        refreshDoctorTable();
    }

    private void initComponents() {
        tableModel = new DefaultTableModel(
            new Object[]{"SSN", "Name", "Surname", "Email", "Phone"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // sadece görüntüleme
            }
        };

        doctorTable = new JTable(tableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        searchField = new JTextField(20);
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchDoctors());
        searchPanel.add(searchButton);

        // Table inside scroll
        JScrollPane scrollPane = new JScrollPane(doctorTable);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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

    private void searchDoctors() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        ArrayList<User> doctors = userDAO.getUsersByRole("Doctor");
        for (User doctor : doctors) {
            if (doctor.getSsn().toLowerCase().contains(searchTerm) ||
                doctor.getUserName().toLowerCase().contains(searchTerm) ||
                doctor.getUserSurname().toLowerCase().contains(searchTerm) ||
                (doctor.getE_mail() != null && doctor.getE_mail().toLowerCase().contains(searchTerm)) ||
                (doctor.getPhone() != null && doctor.getPhone().toLowerCase().contains(searchTerm))) {

                tableModel.addRow(new Object[]{
                    doctor.getSsn(),
                    doctor.getUserName(),
                    doctor.getUserSurname(),
                    doctor.getE_mail(),
                    doctor.getPhone()
                });
            }
        }
    }
}
