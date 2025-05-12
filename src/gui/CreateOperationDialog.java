package gui;

import DAO.UserDAO;
import DAO.TreatmentDAO;
import Library.Doctor;
import Library.Patient;
import Library.Operation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CreateOperationDialog extends JDialog {
    public CreateOperationDialog(JFrame parent, Doctor doctor) {
        super(parent, "Create Operation", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField ssnField = new JTextField();
        JTextField startField = new JTextField();
        JTextField endField = new JTextField();
        JTextField nameField = new JTextField();

        panel.add(new JLabel("Patient SSN:")); panel.add(ssnField);
        panel.add(new JLabel("Start Date:")); panel.add(startField);
        panel.add(new JLabel("End Date:")); panel.add(endField);
        panel.add(new JLabel("Operation Name:")); panel.add(nameField);

        JButton createBtn = new JButton("Create");
        add(panel, BorderLayout.CENTER);
        add(createBtn, BorderLayout.SOUTH);

        createBtn.addActionListener((ActionEvent e) -> {
            try {
                String ssn = ssnField.getText().trim();
                Patient patient = (Patient) new UserDAO().getUserBySSN(ssn);
                if (patient == null) {
                    JOptionPane.showMessageDialog(this, "Patient not found."); return;
                }
                String start = startField.getText().trim();
                String end = endField.getText().trim();
                String name = nameField.getText().trim();

                Operation op = new Operation(start, end, name);
                boolean success = new TreatmentDAO().createOperation(op, ssn);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Created."); dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}