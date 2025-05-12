package GUI;

import DAO.AppointmentDAO;
import DAO.TreatmentDAO;
import DAO.UserDAO;
import Library.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DoctorWindow extends JFrame {
    private final Doctor doctor;
    private final UserDAO userDAO = new UserDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final TreatmentDAO treatmentDAO = new TreatmentDAO();

    private JTable table;
    private JTextArea detailArea;

    public DoctorWindow(Doctor doctor) {
        this.doctor = doctor;

        setTitle("Doctor Panel - " + doctor.getUserName());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Profile Tab
        JPanel profileTab = new JPanel(new GridLayout(6, 2));
        profileTab.add(new JLabel("Name:")); profileTab.add(new JLabel(doctor.getUserName()));
        profileTab.add(new JLabel("Surname:")); profileTab.add(new JLabel(doctor.getUserSurname()));
        profileTab.add(new JLabel("Email:")); profileTab.add(new JLabel(doctor.getE_mail()));
        profileTab.add(new JLabel("Phone:")); profileTab.add(new JLabel(doctor.getPhone()));
        profileTab.add(new JLabel("Role:")); profileTab.add(new JLabel(doctor.getUserRole()));
        profileTab.add(new JLabel("SSN:")); profileTab.add(new JLabel(doctor.getSsn()));
        tabbedPane.addTab("Profile", profileTab);

        // Work Area Tab
        JPanel workPanel = new JPanel(new BorderLayout());

        // Buton Paneli
        JPanel topPanel = new JPanel();
        JButton viewPatientsBtn = new JButton("View Patients");
        JButton viewAppointmentsBtn = new JButton("View My Appointments");
        JButton viewPrescriptionsBtn = new JButton("View Prescriptions");
        JButton viewOperationsBtn = new JButton("View Operations");
        topPanel.add(viewPatientsBtn);
        topPanel.add(viewAppointmentsBtn);
        topPanel.add(viewPrescriptionsBtn);
        topPanel.add(viewOperationsBtn);

        // Tablo
        table = new JTable();
        JScrollPane tableScroll = new JScrollPane(table);

        // Sağ panel - detay
        detailArea = new JTextArea(10, 25);
        detailArea.setEditable(false);
        JScrollPane detailScroll = new JScrollPane(detailArea);

        // Alt butonlar
        JPanel bottomPanel = new JPanel();
        JButton createPrescriptionBtn = new JButton("Create Prescription");
        JButton createOperationBtn = new JButton("Create Operation");
        bottomPanel.add(createPrescriptionBtn);
        bottomPanel.add(createOperationBtn);

        workPanel.add(topPanel, BorderLayout.NORTH);
        workPanel.add(tableScroll, BorderLayout.CENTER);
        workPanel.add(detailScroll, BorderLayout.EAST);
        workPanel.add(bottomPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Workspace", workPanel);

        add(tabbedPane);

        // Action Listeners
        viewPatientsBtn.addActionListener(e -> showPatients());
        viewAppointmentsBtn.addActionListener(e -> showAppointments());
        viewPrescriptionsBtn.addActionListener(e -> showPrescriptions());
        viewOperationsBtn.addActionListener(e -> showOperations());

        createPrescriptionBtn.addActionListener(e -> new CreatePrescriptionDialog(this, doctor));
        createOperationBtn.addActionListener(e -> new CreateOperationDialog(this, doctor));

        setVisible(true);
    }

    private void showPatients() {
        ArrayList<Patient> patients = new ArrayList<>();
        for (User user : userDAO.getUsersByRole("Patient")) {
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
    }

    private void showAppointments() {
        ArrayList<Appointment> appointments = appointmentDAO.getAppointmentsByDoctor(doctor.getSsn());

        String[] cols = {"Date", "Patient SSN", "Patient Name"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Appointment a : appointments) {
            model.addRow(new Object[]{
                    a.getDate(),
                    a.getPatient().getSsn(),
                    a.getPatient().getUserName() + " " + a.getPatient().getUserSurname()
            });
        }

        table.setModel(model);
        detailArea.setText("Appointments listed.");
    }

    private void showPrescriptions() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient from the table.");
            return;
        }

        String ssn = table.getValueAt(row, 0).toString();
        ArrayList<Prescription> prescriptions = treatmentDAO.getPrescriptionsByPatient(ssn);

        StringBuilder sb = new StringBuilder("Prescriptions:\n");
        for (Prescription p : prescriptions) {
            sb.append("ID: ").append(p.getPrescriptionID())
                    .append(", Start: ").append(p.getStartDate())
                    .append(", Meds: ").append(p.getMedication().size())
                    .append("\n");
        }

        detailArea.setText(sb.toString());
    }

    private void showOperations() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient from the table.");
            return;
        }

        String ssn = table.getValueAt(row, 0).toString();
        ArrayList<Operation> operations = treatmentDAO.getOperationsByPatient(ssn);

        StringBuilder sb = new StringBuilder("Operations:\n");
        for (Operation o : operations) {
            sb.append(o.getOperationName())
                    .append(" (").append(o.getStartDate())
                    .append(" - ").append(o.getEndDate()).append(")\n");
        }

        detailArea.setText(sb.toString());
    }
}