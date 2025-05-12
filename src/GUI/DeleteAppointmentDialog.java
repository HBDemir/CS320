package GUI;

import DAO.AppointmentDAO;
import Library.Nurse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DeleteAppointmentDialog extends JDialog {
    public DeleteAppointmentDialog(JFrame parent, Nurse nurse) {
        super(parent, "Delete Appointment", true);
        setSize(300, 150);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField idField = new JTextField();

        panel.add(new JLabel("Appointment Index (Row Number):"));
        panel.add(idField);

        JButton deleteBtn = new JButton("Delete");
        add(panel, BorderLayout.CENTER);
        add(deleteBtn, BorderLayout.SOUTH);

        deleteBtn.addActionListener((ActionEvent e) -> {
            try {
                int id = Integer.parseInt(idField.getText());
                boolean success = new AppointmentDAO().deleteAppointment(id);
                JOptionPane.showMessageDialog(this, success ? "Deleted" : "Not Found");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        setVisible(true);
    }
}