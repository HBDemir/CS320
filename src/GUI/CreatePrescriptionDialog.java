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
// Import SimpleDateFormat for a potentially more robust check, or just use regex
import java.text.ParseException;
import java.text.SimpleDateFormat;


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
        panel.add(new JLabel("Start Date (YYYY-MM-DD):")); panel.add(startField); // Improved label
        panel.add(new JLabel("End Date (YYYY-MM-DD, optional):")); panel.add(endField); // Improved label
        panel.add(new JLabel("Prescription ID (Number):")); panel.add(idField); // Added hint
        panel.add(new JLabel("Medications (comma separated):")); panel.add(medsField); // Added hint
        panel.add(new JLabel("Dosages (comma separated):")); panel.add(dosesField); // Added hint

        JButton createBtn = new JButton("Create");
        add(panel, BorderLayout.CENTER);
        add(createBtn, BorderLayout.SOUTH);

        createBtn.addActionListener((ActionEvent e) -> {
            try {
                String ssn = ssnField.getText().trim();
                String start = startField.getText().trim(); // Get date strings
                String end = endField.getText().trim();     // Get date strings

                // --- START OF DATE VALIDATION FIX ---
                // Regex for YYYY-MM-DD format
                String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";

                if (start.isEmpty()) { // Basic required check
                    JOptionPane.showMessageDialog(this, "Start Date is required.");
                    return;
                }
                if (!start.matches(dateRegex)) {
                    JOptionPane.showMessageDialog(this, "Invalid Start Date format. Please use YYYY-MM-DD.");
                    return;
                }
                // Check end date only if not empty
                if (!end.isEmpty() && !end.matches(dateRegex)) {
                    JOptionPane.showMessageDialog(this, "Invalid End Date format. Please use YYYY-MM-DD or leave empty.");
                    return;
                }
                // --- END OF DATE VALIDATION FIX ---


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

                double pid;
                try {
                    pid = Double.parseDouble(idField.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Prescription ID. Please enter a number.");
                    return;
                }

                String[] meds = medsField.getText().split(",");
                String[] doses = dosesField.getText().split(",");

                if (meds.length != doses.length) {
                    JOptionPane.showMessageDialog(this, "Mismatch in number of medications and dosages.");
                    return;
                }

                ArrayList<String> m = new ArrayList<>();
                ArrayList<String> d = new ArrayList<>();
                boolean hasMedications = false;
                for (int i = 0; i < meds.length; i++) {
                    String medName = meds[i].trim();
                    String dosageVal = doses[i].trim();
                    // Only add non-empty medication/dosage pairs
                    if (!medName.isEmpty() && !dosageVal.isEmpty()) {
                        m.add(medName);
                        d.add(dosageVal);
                        hasMedications = true;
                    }
                }

                if (!hasMedications) {
                    JOptionPane.showMessageDialog(this, "Please enter at least one medication and its dosage.");
                    return;
                }


                // Now that dates are validated, create the Prescription object
                Prescription p = new Prescription(start, end, pid, m, d);

                // Call the DAO method
                boolean success = new TreatmentDAO().createPrescription(p, ssn);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Prescription created successfully.");
                    dispose();
                } else {
                    // The DAO method should print its own error details
                    JOptionPane.showMessageDialog(this, "Failed to create prescription. Check console for database errors.");
                }
            } catch (Exception ex) {
                // Catch any other unexpected exceptions and show their message
                System.err.println("An unexpected exception occurred during prescription creation:");
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}