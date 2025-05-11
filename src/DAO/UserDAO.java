package DAO;

import DBConnection.DBConnection;
import Library.User;
import Library.Doctor;
import Library.Nurse;
import Library.Patient;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO {

    public boolean createUser(User user) {
        String sql = "INSERT INTO Users (ssn, userName, userSurname, userRole, e_mail, phone, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getSsn());
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getUserSurname());
            pstmt.setString(4, user.getUserRole());
            pstmt.setString(5, user.getE_mail());
            pstmt.setString(6, user.getPhone());
            pstmt.setString(7, HashUtil.sha256(user.getPassword()));

            int rowsAffected = pstmt.executeUpdate();

            // If the user is a doctor or nurse, we need additional entries
            if (rowsAffected > 0) {
                if (user.getUserRole().equals("Doctor")) {
                    String doctorSql = "INSERT INTO Doctors (ssn) VALUES (?)";
                    try (PreparedStatement doctorStmt = conn.prepareStatement(doctorSql)) {
                        doctorStmt.setString(1, user.getSsn());
                        doctorStmt.executeUpdate();
                    }
                } else if (user.getUserRole().equals("Nurse")) {
                    String nurseSql = "INSERT INTO Nurses (ssn) VALUES (?)";
                    try (PreparedStatement nurseStmt = conn.prepareStatement(nurseSql)) {
                        nurseStmt.setString(1, user.getSsn());
                        nurseStmt.executeUpdate();
                    }
                } else if (user.getUserRole().equals("Patient")) {
                    String patientSql = "INSERT INTO Patients (ssn) VALUES (?)";
                    try (PreparedStatement patientStmt = conn.prepareStatement(patientSql)) {
                        patientStmt.setString(1, user.getSsn());
                        patientStmt.executeUpdate();
                    }
                }
            }

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public User getUserBySSN(String ssn) {
        String sql = "SELECT * FROM Users WHERE ssn = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ssn);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String userRole = rs.getString("userRole");
                String userName = rs.getString("userName");
                String userSurname = rs.getString("userSurname");
                String email = rs.getString("e_mail");
                String phone = rs.getString("phone");
                String password = rs.getString("password");

                if (userRole.equals("Doctor")) {
                    return new Doctor(ssn, userName, userSurname, userRole, email, phone, password);
                } else if (userRole.equals("Nurse")) {
                    return new Nurse(ssn, userName, userSurname, userRole, email, phone, password);
                } else if (userRole.equals("Patient")) {
                    return new Patient(ssn, userName, userSurname, userRole, email, phone);
                }
            }

            return null;

        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET userName = ?, userSurname = ?, userRole = ?, " +
                "e_mail = ?, phone = ?, password = ? WHERE ssn = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getUserSurname());
            pstmt.setString(3, user.getUserRole());
            pstmt.setString(4, user.getE_mail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, HashUtil.sha256(user.getPassword()));
            pstmt.setString(7, user.getSsn());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String ssn) {
        User user = getUserBySSN(ssn);
        if (user == null) {
            return false;
        }

        String userRole = user.getUserRole();
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Delete from role-specific table first
            if (userRole.equals("Doctor")) {
                String doctorSql = "DELETE FROM Doctors WHERE ssn = ?";
                try (PreparedStatement stmt = conn.prepareStatement(doctorSql)) {
                    stmt.setString(1, ssn);
                    stmt.executeUpdate();
                }
            } else if (userRole.equals("Nurse")) {
                String nurseSql = "DELETE FROM Nurses WHERE ssn = ?";
                try (PreparedStatement stmt = conn.prepareStatement(nurseSql)) {
                    stmt.setString(1, ssn);
                    stmt.executeUpdate();
                }
            } else if (userRole.equals("Patient")) {
                String patientSql = "DELETE FROM Patients WHERE ssn = ?";
                try (PreparedStatement stmt = conn.prepareStatement(patientSql)) {
                    stmt.setString(1, ssn);
                    stmt.executeUpdate();
                }
            }

            // Then delete from Users table
            String userSql = "DELETE FROM Users WHERE ssn = ?";
            try (PreparedStatement stmt = conn.prepareStatement(userSql)) {
                stmt.setString(1, ssn);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit(); // Commit transaction
                    return true;
                } else {
                    conn.rollback(); // Rollback if no rows affected
                    return false;
                }
            }

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback on error
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }

            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                }
            } catch (SQLException autoCommitEx) {
                System.err.println("Error resetting auto-commit: " + autoCommitEx.getMessage());
            }
        }
    }

    public ArrayList<User> getUsersByRole(String role) {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE userRole = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String ssn = rs.getString("ssn");
                String userName = rs.getString("userName");
                String userSurname = rs.getString("userSurname");
                String email = rs.getString("e_mail");
                String phone = rs.getString("phone");
                String password = rs.getString("password");

                if (role.equals("Doctor")) {
                    users.add(new Doctor(ssn, userName, userSurname, role, email, phone, password));
                } else if (role.equals("Nurse")) {
                    users.add(new Nurse(ssn, userName, userSurname, role, email, phone, password));
                } else if (role.equals("Patient")) {
                    users.add(new Patient(ssn, userName, userSurname, role, email, phone));
                }
            }

            return users;

        } catch (SQLException e) {
            System.err.println("Error retrieving users by role: " + e.getMessage());
            e.printStackTrace();
            return users;
        }
    }

    public User authenticateUser(String ssn, String password) {
        String sql = "SELECT * FROM Users WHERE ssn = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ssn);
            pstmt.setString(2, HashUtil.sha256(password));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String userRole = rs.getString("userRole");
                String userName = rs.getString("userName");
                String userSurname = rs.getString("userSurname");
                String email = rs.getString("e_mail");
                String phone = rs.getString("phone");

                if (userRole.equals("Doctor")) {
                    return new Doctor(ssn, userName, userSurname, userRole, email, phone, password);
                } else if (userRole.equals("Nurse")) {
                    return new Nurse(ssn, userName, userSurname, userRole, email, phone, password);
                } else if (userRole.equals("Patient")) {
                    return new Patient(ssn, userName, userSurname, userRole, email, phone);
                }
            }

            return null;

        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<Patient> searchPatients(String keyword) {
        ArrayList<Patient> result = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE userRole = 'Patient' AND " +
                "(ssn ILIKE ? OR userName ILIKE ? OR userSurname ILIKE ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String likeTerm = "%" + keyword + "%";
            pstmt.setString(1, likeTerm);
            pstmt.setString(2, likeTerm);
            pstmt.setString(3, likeTerm);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(new Patient(
                        rs.getString("ssn"),
                        rs.getString("userName"),
                        rs.getString("userSurname"),
                        rs.getString("userRole"),
                        rs.getString("e_mail"),
                        rs.getString("phone")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error searching patients: " + e.getMessage());
        }

        return result;
    }
}