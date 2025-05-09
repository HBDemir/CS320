package gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class MainWindow extends JFrame {
    private static MainWindow instance;
    
    private MainWindow() {
        setTitle("Hospital Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        initUI();
    }

    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton appointmentButton = new JButton("Manage Appointments");
        JButton patientButton = new JButton("Manage Patients");
        JButton doctorButton = new JButton("Manage Doctors");
        JButton exitButton = new JButton("Exit");

        appointmentButton.addActionListener(e -> openAppointmentManager());
        patientButton.addActionListener(e -> openPatientManager());
        doctorButton.addActionListener(e -> openDoctorManager());
        exitButton.addActionListener(e -> confirmExit());

        mainPanel.add(appointmentButton);
        mainPanel.add(patientButton);
        mainPanel.add(doctorButton);
        mainPanel.add(exitButton);

        add(mainPanel);
    }

    private void openAppointmentManager() {
        SwingUtilities.invokeLater(() -> {
            AppointmentManager manager = new AppointmentManager();
            manager.setVisible(true);
        });
    }

    private void openPatientManager() {
        SwingUtilities.invokeLater(() -> {
            PatientManager manager = new PatientManager();
            manager.setVisible(true);
        });
    }

    private void openDoctorManager() {
        SwingUtilities.invokeLater(() -> {
            DoctorManager manager = new DoctorManager();
            manager.setVisible(true);
        });
    }

    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION
        );
        
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = MainWindow.getInstance();
            window.setVisible(true);
        });
    }
}