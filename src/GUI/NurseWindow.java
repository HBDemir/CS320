package GUI;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import Library.Appointment;
import Library.Nurse;
import Library.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class NurseWindow extends JFrame {
    private final Nurse nurse;
    private final UserDAO userDAO = new UserDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    private JTable table;
    private JTextArea detailArea;
    private JTextField searchField;

    private enum ViewMode { NONE, APPOINTMENTS, PATIENTS }
    private ViewMode currentView = ViewMode.NONE;

    public NurseWindow(Nurse nurse) {
        this.nurse = nurse;

        setTitle("Nurse Panel - " + nurse.getUserName());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Profile tab
        JPanel profileTab = new JPanel(new GridLayout(6, 2));
        profileTab.add(new JLabel("Name:")); profileTab.add(new JLabel(nurse.getUserName()));
        profileTab.add(new JLabel("Surname:")); profileTab.add(new JLabel(nurse.getUserSurname()));
        profileTab.add(new JLabel("Email:")); profileTab.add(new JLabel(nurse.getE_mail()));
        profileTab.add(new JLabel("Phone:")); profileTab.add(new JLabel(nurse.getPhone()));
        profileTab.add(new JLabel("Role:")); profileTab.add(new JLabel(nurse.getUserRole()));
        profileTab.add(new JLabel("SSN:")); profileTab.add(new JLabel(nurse.getSsn()));

        tabbedPane.addTab("Profile", profileTab);

        // Workspace tab
        JPanel workPanel = new JPanel(new BorderLayout());

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout());
        JButton viewAllAppointmentsBtn = new JButton("View All Appointments");
        JButton viewPatientsBtn = new JButton("View Patients");
        JButton sendEmailBtn = new JButton("Send Email");

        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search Patient");

        topPanel.add(viewAllAppointmentsBtn);
        topPanel.add(viewPatientsBtn);
        topPanel.add(sendEmailBtn);
        topPanel.add(searchField);
        topPanel.add(searchBtn);

        // Table center
        table = new JTable();
        JScrollPane tableScroll = new JScrollPane(table);

        // Detail area right
        detailArea = new JTextArea(10, 25);
        detailArea.setEditable(false);
        JScrollPane detailScroll = new JScrollPane(detailArea);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        JButton createAppointmentBtn = new JButton("Create Appointment");
        JButton rescheduleAppointmentBtn = new JButton("Reschedule Appointment");
        JButton deleteAppointmentBtn = new JButton("Delete Appointment");
        JButton registerPatientBtn = new JButton("Register New Patient");

        bottomPanel.add(createAppointmentBtn);
        bottomPanel.add(rescheduleAppointmentBtn);
        bottomPanel.add(deleteAppointmentBtn);
        bottomPanel.add(registerPatientBtn);

        workPanel.add(topPanel, BorderLayout.NORTH);
        workPanel.add(tableScroll, BorderLayout.CENTER);
        workPanel.add(detailScroll, BorderLayout.EAST);
        workPanel.add(bottomPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Workspace", workPanel);
        add(tabbedPane);

        // Button actions
        viewAllAppointmentsBtn.addActionListener(e -> refreshAppointmentsTable());
        viewPatientsBtn.addActionListener(e -> refreshPatientsTable());
        sendEmailBtn.addActionListener(e -> sendEmailToPatient());

        createAppointmentBtn.addActionListener(e -> {
            new CreateAppointmentDialog(this, nurse);
            refresh();
        });

        rescheduleAppointmentBtn.addActionListener(e -> {
            new RescheduleAppointmentDialog(this, nurse);
            refresh();
        });

        deleteAppointmentBtn.addActionListener(e -> {
            new DeleteAppointmentDialog(this, nurse);
            refresh();
        });

        registerPatientBtn.addActionListener(e -> {
            new RegisterPatientDialog(this, nurse);
            refresh();
        });

        searchBtn.addActionListener(e -> searchPatientsByKeyword());

        setVisible(true);
    }

    private void refreshAppointmentsTable() {
        ArrayList<Appointment> appointments = appointmentDAO.getAllAppointments();

        String[] cols = {"Date", "Patient Name", "Doctor Name"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Appointment a : appointments) {
            model.addRow(new Object[]{
                    a.getDate(),
                    a.getPatient().getUserName() + " " + a.getPatient().getUserSurname(),
                    a.getDoctor().getUserName() + " " + a.getDoctor().getUserSurname()
            });
        }

        table.setModel(model);
        detailArea.setText("All appointments listed.");
        currentView = ViewMode.APPOINTMENTS;
    }

    private void refreshPatientsTable() {
        ArrayList<Patient> patients = new ArrayList<>();
        for (var user : userDAO.getUsersByRole("Patient")) {
            if (user instanceof Patient p) {
                patients.add(p);
            }
        }

        String[] cols = {"SSN", "Name", "Email"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Patient p : patients) {
            model.addRow(new Object[]{
                    p.getSsn(),
                    p.getUserName() + " " + p.getUserSurname(),
                    p.getE_mail()
            });
        }

        table.setModel(model);
        detailArea.setText("Patients listed.");
        currentView = ViewMode.PATIENTS;
    }

    private void refresh() {
        switch (currentView) {
            case APPOINTMENTS -> refreshAppointmentsTable();
            case PATIENTS -> refreshPatientsTable();
            default -> table.setModel(new DefaultTableModel());
        }
    }

    private void sendEmailToPatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row with appointment info.");
            return;
        }

        String email = JOptionPane.showInputDialog(this, "Enter patient's email to notify:");

        if (email != null && !email.isBlank()) {
            ArrayList<Appointment> appointments = appointmentDAO.getAllAppointments();
            if (row < appointments.size()) {
                Appointment selected = appointments.get(row);
                nurse.sendEmail(email, selected);
                detailArea.setText("Email sent to: " + email);
            } else {
                JOptionPane.showMessageDialog(this, "Appointment selection mismatch.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email.");
        }
    }

    private void searchPatientsByKeyword() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a keyword.");
            return;
        }

        ArrayList<Patient> patients = userDAO.searchPatients(keyword);
        String[] cols = {"SSN", "Name", "Email"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Patient p : patients) {
            model.addRow(new Object[]{
                    p.getSsn(),
                    p.getUserName() + " " + p.getUserSurname(),
                    p.getE_mail()
            });
        }

        table.setModel(model);
        detailArea.setText("Search results for: " + keyword);
        currentView = ViewMode.PATIENTS;
    }
}
