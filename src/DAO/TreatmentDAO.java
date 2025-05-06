package DAO;

import DBConnection.DBConnection;
import Library.Operation;
import Library.Patient;
import Library.Prescription;
import Library.Treatment;

import java.sql.*;
import java.util.ArrayList;

public class TreatmentDAO {

    private UserDAO userDAO = new UserDAO();

    // Create a new operation
    public boolean createOperation(Operation operation, String patientSsn) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First, create the base treatment
            int treatmentId = createBaseTreatment(conn, operation.getStartDate(), operation.getEndDate(), "Operation");

            if (treatmentId == -1) {
                conn.rollback();
                return false;
            }

            // Now create the operation
            String operationSql = "INSERT INTO Operations (treatment_id, operationName) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(operationSql)) {
                pstmt.setInt(1, treatmentId);
                pstmt.setString(2, operation.getOperationName());
                pstmt.executeUpdate();
            }

            // Link to patient
            boolean linked = linkTreatmentToPatient(conn, treatmentId, patientSsn);

            if (!linked) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }

            System.err.println("Error creating operation: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException autoCommitEx) {
                System.err.println("Error resetting auto-commit: " + autoCommitEx.getMessage());
            }
        }
    }

    // Create a new prescription
    public boolean createPrescription(Prescription prescription, String patientSsn) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First, create the base treatment
            int treatmentId = createBaseTreatment(conn, prescription.getStartDate(), prescription.getEndDate(), "Prescription");

            if (treatmentId == -1) {
                conn.rollback();
                return false;
            }

            // Now create the prescription
            String prescriptionSql = "INSERT INTO Prescriptions (treatment_id, prescriptionID) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(prescriptionSql)) {
                pstmt.setInt(1, treatmentId);
                pstmt.setDouble(2, prescription.getPrescriptionID());
                pstmt.executeUpdate();
            }

            // Add medications
            ArrayList<String[]> medications = prescription.getMedication();
            String medicationSql = "INSERT INTO Medications (treatment_id, medicationName, dosage) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(medicationSql)) {
                for (String[] med : medications) {
                    pstmt.setInt(1, treatmentId);
                    pstmt.setString(2, med[0]); // medication name
                    pstmt.setString(3, med[1]); // dosage
                    pstmt.executeUpdate();
                }
            }

            // Link to patient
            boolean linked = linkTreatmentToPatient(conn, treatmentId, patientSsn);

            if (!linked) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }

            System.err.println("Error creating prescription: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException autoCommitEx) {
                System.err.println("Error resetting auto-commit: " + autoCommitEx.getMessage());
            }
        }
    }

    // Helper method to create base treatment and return its ID
    private int createBaseTreatment(Connection conn, String startDate, String endDate, String treatmentType) throws SQLException {
        String sql = "INSERT INTO Treatments (startDate, endDate, treatment_type) VALUES (?, ?, ?) RETURNING treatment_id";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(startDate));

            if (endDate != null && !endDate.isEmpty()) {
                pstmt.setDate(2, Date.valueOf(endDate));
            } else {
                pstmt.setNull(2, Types.DATE);
            }

            pstmt.setString(3, treatmentType);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        }
    }

    // Helper method to link treatment to patient
    private boolean linkTreatmentToPatient(Connection conn, int treatmentId, String patientSsn) throws SQLException {
        String sql = "INSERT INTO PatientTreatments (patient_ssn, treatment_id) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patientSsn);
            pstmt.setInt(2, treatmentId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Get all operations for a patient
    public ArrayList<Operation> getOperationsByPatient(String patientSsn) {
        ArrayList<Operation> operations = new ArrayList<>();

        String sql = "SELECT t.treatment_id, t.startDate, t.endDate, o.operationName " +
                "FROM Treatments t " +
                "JOIN Operations o ON t.treatment_id = o.treatment_id " +
                "JOIN PatientTreatments pt ON t.treatment_id = pt.treatment_id " +
                "WHERE pt.patient_ssn = ? " +
                "ORDER BY t.startDate DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patientSsn);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String startDate = rs.getDate("startDate").toString();
                String endDate = rs.getDate("endDate") != null ? rs.getDate("endDate").toString() : null;
                String operationName = rs.getString("operationName");

                operations.add(new Operation(startDate, endDate, operationName));
            }

            return operations;

        } catch (SQLException e) {
            System.err.println("Error retrieving patient's operations: " + e.getMessage());
            e.printStackTrace();
            return operations;
        }
    }

    // Get all prescriptions for a patient
    public ArrayList<Prescription> getPrescriptionsByPatient(String patientSsn) {
        ArrayList<Prescription> prescriptions = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            // First, get the prescription base info
            String sql = "SELECT t.treatment_id, t.startDate, t.endDate, p.prescriptionID " +
                    "FROM Treatments t " +
                    "JOIN Prescriptions p ON t.treatment_id = p.treatment_id " +
                    "JOIN PatientTreatments pt ON t.treatment_id = pt.treatment_id " +
                    "WHERE pt.patient_ssn = ? " +
                    "ORDER BY t.startDate DESC";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, patientSsn);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int treatmentId = rs.getInt("treatment_id");
                    String startDate = rs.getDate("startDate").toString();
                    String endDate = rs.getDate("endDate") != null ? rs.getDate("endDate").toString() : null;
                    double prescriptionID = rs.getDouble("prescriptionID");

                    // For each prescription, get its medications
                    ArrayList<String> medicationNames = new ArrayList<>();
                    ArrayList<String> dosages = new ArrayList<>();

                    String medSql = "SELECT medicationName, dosage FROM Medications WHERE treatment_id = ?";
                    try (PreparedStatement medStmt = conn.prepareStatement(medSql)) {
                        medStmt.setInt(1, treatmentId);
                        ResultSet medRs = medStmt.executeQuery();

                        while (medRs.next()) {
                            medicationNames.add(medRs.getString("medicationName"));
                            dosages.add(medRs.getString("dosage"));
                        }
                    }

                    prescriptions.add(new Prescription(startDate, endDate, prescriptionID, medicationNames, dosages));
                }
            }

            return prescriptions;

        } catch (SQLException e) {
            System.err.println("Error retrieving patient's prescriptions: " + e.getMessage());
            e.printStackTrace();
            return prescriptions;
        }
    }

    // Update treatment dates
    public boolean updateTreatmentDates(int treatmentId, String startDate, String endDate) {
        String sql = "UPDATE Treatments SET startDate = ?, endDate = ? WHERE treatment_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(startDate));

            if (endDate != null && !endDate.isEmpty()) {
                pstmt.setDate(2, Date.valueOf(endDate));
            } else {
                pstmt.setNull(2, Types.DATE);
            }

            pstmt.setInt(3, treatmentId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating treatment dates: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get all treatments for a patient
    public ArrayList<Treatment> getAllTreatmentsByPatient(String patientSsn) {
        ArrayList<Treatment> treatments = new ArrayList<>();

        String sql = "SELECT t.treatment_id, t.startDate, t.endDate, t.treatment_type " +
                "FROM Treatments t " +
                "JOIN PatientTreatments pt ON t.treatment_id = pt.treatment_id " +
                "WHERE pt.patient_ssn = ? " +
                "ORDER BY t.startDate DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patientSsn);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int treatmentId = rs.getInt("treatment_id");
                String startDate = rs.getDate("startDate").toString();
                String endDate = rs.getDate("endDate") != null ? rs.getDate("endDate").toString() : null;
                String treatmentType = rs.getString("treatment_type");

                Treatment treatment = new Treatment();
                treatment.setStartDate(startDate);
                treatment.setEndDate(endDate);

                treatments.add(treatment);
            }

            return treatments;

        } catch (SQLException e) {
            System.err.println("Error retrieving patient's treatments: " + e.getMessage());
            e.printStackTrace();
            return treatments;
        }
    }

    // Delete a treatment
    public boolean deleteTreatment(int treatmentId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First, find out the treatment type
            String typeSql = "SELECT treatment_type FROM Treatments WHERE treatment_id = ?";
            String treatmentType = null;

            try (PreparedStatement pstmt = conn.prepareStatement(typeSql)) {
                pstmt.setInt(1, treatmentId);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    treatmentType = rs.getString("treatment_type");
                } else {
                    // Treatment not found
                    conn.rollback();
                    return false;
                }
            }

            // Delete from specific treatment table first
            if ("Prescription".equals(treatmentType)) {
                // First delete from Medications
                String medSql = "DELETE FROM Medications WHERE treatment_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(medSql)) {
                    pstmt.setInt(1, treatmentId);
                    pstmt.executeUpdate();
                }

                // Then delete from Prescriptions
                String prescSql = "DELETE FROM Prescriptions WHERE treatment_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(prescSql)) {
                    pstmt.setInt(1, treatmentId);
                    pstmt.executeUpdate();
                }
            } else if ("Operation".equals(treatmentType)) {
                // Delete from Operations
                String opSql = "DELETE FROM Operations WHERE treatment_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(opSql)) {
                    pstmt.setInt(1, treatmentId);
                    pstmt.executeUpdate();
                }
            }

            // Delete from PatientTreatments
            String ptSql = "DELETE FROM PatientTreatments WHERE treatment_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(ptSql)) {
                pstmt.setInt(1, treatmentId);
                pstmt.executeUpdate();
            }

            // Finally, delete from base Treatments table
            String treatSql = "DELETE FROM Treatments WHERE treatment_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(treatSql)) {
                pstmt.setInt(1, treatmentId);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }

            System.err.println("Error deleting treatment: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException autoCommitEx) {
                System.err.println("Error resetting auto-commit: " + autoCommitEx.getMessage());
            }
        }
    }
}