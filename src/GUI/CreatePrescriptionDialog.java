package GUI;

import DAO.UserDAO;
import DAO.TreatmentDAO;
import Library.Doctor;
import Library.Patient;
import Library.Prescription;
import Library.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class CreatePrescriptionDialog extends JDialog {
    public CreatePrescriptionDialog(JFrame parent, Doctor doctor) {
        super(parent, "Create Prescription", true);
        setSize(400, 400);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(6, 2));
        JTextField ssnField = new JTextField();
        JTextField startField = new JTextField();
        JTextField endField = new JTextField();
        JTextField idField = new JTextField();
        JTextField medsField = new JTextField();
        JTextField dosesField = new JTextField();

        panel.add(new JLabel("Patient SSN:")); panel.add(ssnField);
        panel.add(new JLabel("Start Date:")); panel.add(startField);
        panel.add(new JLabel("End Date:")); panel.add(endField);
        panel.add(new JLabel("Prescription ID:")); panel.add(idField);
        panel.add(new JLabel("Medications (comma):")); panel.add(medsField);
        panel.add(new JLabel("Dosages (comma):")); panel.add(dosesField);

        JButton createBtn = new JButton("Create");
        add(panel, BorderLayout.CENTER);
        add(createBtn, BorderLayout.SOUTH);

        createBtn.addActionListener((ActionEvent e) -> {
            try {
                String ssn = ssnField.getText().trim();
                User user = new UserDAO().getUserBySSN(ssn);
                if (user == null) {
                    JOptionPane.showMessageDialog(this, "User not found.");
                    return;
                }
                if (!(user instanceof Patient)) {
                    JOptionPane.showMessageDialog(this, "The SSN belongs to a " + user.getUserRole() + ", not a Patient.");
                    return;
                }
                Patient patient = (Patient) user;
                if (patient == null) {
                    JOptionPane.showMessageDialog(this, "Patient not found."); return;
                }
                String start = startField.getText().trim();
                String end = endField.getText().trim();
                double pid = Double.parseDouble(idField.getText().trim());

                String[] meds = medsField.getText().split(",");
                String[] doses = dosesField.getText().split(",");

                if (meds.length != doses.length) {
                    JOptionPane.showMessageDialog(this, "Mismatch in meds/doses count."); return;
                }

                ArrayList<String> m = new ArrayList<>();
                ArrayList<String> d = new ArrayList<>();
                for (int i = 0; i < meds.length; i++) {
                    m.add(meds[i].trim());
                    d.add(doses[i].trim());
                }

                Prescription p = new Prescription(start, end, pid, m, d);
                boolean success = new TreatmentDAO().createPrescription(p, ssn);
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