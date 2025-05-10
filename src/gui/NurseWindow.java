package gui;

import DAO.UserDAO;
import Library.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class NurseWindow extends JFrame {

    private final UserDAO userDAO = new UserDAO();
    private JTable nurseTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public NurseWindow() {
        setTitle("Nurse List");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        layoutComponents();
        refreshNurseTable();
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

        nurseTable = new JTable(tableModel);
        nurseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        searchButton.addActionListener(e -> searchNurses());
        searchPanel.add(searchButton);

        // Table inside scroll
        JScrollPane scrollPane = new JScrollPane(nurseTable);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void refreshNurseTable() {
        tableModel.setRowCount(0);
        ArrayList<User> nurses = userDAO.getUsersByRole("Nurse");

        for (User nurse : nurses) {
            tableModel.addRow(new Object[]{
                nurse.getSsn(),
                nurse.getUserName(),
                nurse.getUserSurname(),
                nurse.getE_mail(),
                nurse.getPhone()
            });
        }
    }

    private void searchNurses() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        ArrayList<User> nurses = userDAO.getUsersByRole("Nurse");
        for (User nurse : nurses) {
            if (nurse.getSsn().toLowerCase().contains(searchTerm) ||
                nurse.getUserName().toLowerCase().contains(searchTerm) ||
                nurse.getUserSurname().toLowerCase().contains(searchTerm) ||
                (nurse.getE_mail() != null && nurse.getE_mail().toLowerCase().contains(searchTerm)) ||
                (nurse.getPhone() != null && nurse.getPhone().toLowerCase().contains(searchTerm))) {

                tableModel.addRow(new Object[]{
                    nurse.getSsn(),
                    nurse.getUserName(),
                    nurse.getUserSurname(),
                    nurse.getE_mail(),
                    nurse.getPhone()
                });
            }
        }
    }
}
