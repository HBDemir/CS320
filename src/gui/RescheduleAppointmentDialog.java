package gui;

import DAO.AppointmentDAO;
import Library.Appointment;
import Library.Nurse;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RescheduleAppointmentDialog extends JDialog {
    public RescheduleAppointmentDialog(JFrame parent, Nurse nurse) {
        super(parent, "Reschedule Appointment", true);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        ArrayList<Appointment> appointments = appointmentDAO.getAllAppointments();

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JComboBox<String> appointmentBox = new JComboBox<>();
        for (Appointment a : appointments) {
            String label = "ID: " + a.getAppointmentId() + " | "
                    + a.getDate() + " | "
                    + a.getPatient().getUserName();
            appointmentBox.addItem(label);
        }

        JTextField newDateField = new JTextField("2025-05-20 10:00:00");

        panel.add(new JLabel("Select Appointment:"));
        panel.add(appointmentBox);
        panel.add(new JLabel("New Date (yyyy-MM-dd HH:mm:ss):"));
        panel.add(newDateField);

        JButton updateBtn = new JButton("Reschedule");
        panel.add(new JLabel());  // boşluk
        panel.add(updateBtn);

        add(panel);

        updateBtn.addActionListener(e -> {
            int index = appointmentBox.getSelectedIndex();
            if (index < 0) {
                JOptionPane.showMessageDialog(this, "Please select an appointment.");
                return;
            }

            Appointment selected = appointments.get(index);
            String newDate = newDateField.getText().trim();

            if (newDate.isBlank()) {
                JOptionPane.showMessageDialog(this, "New date cannot be empty.");
                return;
            }

            boolean success = appointmentDAO.updateAppointmentDate(selected.getAppointmentId(), newDate);
            JOptionPane.showMessageDialog(this,
                    success ? "Appointment rescheduled." : "Failed to reschedule appointment.");

            if (success) dispose();
        });

        setVisible(true);
    }
}