package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;

import DAO.UserDAO;
import Library.Doctor;
import Library.Nurse;
import Library.User;
import GUI.LoginPanel;
import GUI.DoctorWindow;
import GUI.NurseWindow;

/**
 * Simple test case to verify login functionality of the HMS
 */
public class LoginPanelTest {

    private LoginPanel loginPanel;
    private UserDAO userDAO;
    private Doctor testDoctor;
    private Nurse testNurse;

    @BeforeEach
    public void setUp() {

        userDAO = new UserDAO();


        testDoctor = new Doctor("D12345", "John", "Smith", "Doctor",
                "john.smith@hospital.com", "555-1234", "password123");
        userDAO.createUser(testDoctor);


        testNurse = new Nurse("N67890", "Jane", "Doe", "Nurse",
                "jane.doe@hospital.com", "555-5678", "password456");
        userDAO.createUser(testNurse);


        loginPanel = new LoginPanel();
    }

    @Test
    public void testSuccessfulDoctorLogin() {

        JTextField ssnField = getPrivateField(loginPanel, "ssnField");
        JPasswordField passwordField = getPrivateField(loginPanel, "passwordField");


        ssnField.setText(testDoctor.getSsn());
        passwordField.setText("password123");


        JButton loginButton = findButtonByText(loginPanel, "Login");
        assertNotNull(loginButton, "Login button should exist");


        loginButton.doClick();


        pause(500);


        assertFalse(loginPanel.isVisible(), "Login panel should be disposed after successful login");


    }

    @Test
    public void testSuccessfulNurseLogin() {

        JTextField ssnField = getPrivateField(loginPanel, "ssnField");
        JPasswordField passwordField = getPrivateField(loginPanel, "passwordField");


        ssnField.setText(testNurse.getSsn());
        passwordField.setText("password456");


        JButton loginButton = findButtonByText(loginPanel, "Login");
        assertNotNull(loginButton, "Login button should exist");


        loginButton.doClick();


        pause(500);


        assertFalse(loginPanel.isVisible(), "Login panel should be disposed after successful login");
    }

    @Test
    public void testFailedLogin() {

        JTextField ssnField = getPrivateField(loginPanel, "ssnField");
        JPasswordField passwordField = getPrivateField(loginPanel, "passwordField");


        ssnField.setText("invalid_ssn");
        passwordField.setText("wrong_password");


        JButton loginButton = findButtonByText(loginPanel, "Login");
        assertNotNull(loginButton, "Login button should exist");


        loginButton.doClick();


        pause(500);


        assertTrue(loginPanel.isVisible(), "Login panel should remain visible after failed login");
    }



    private JButton findButtonByText(Container container, String text) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof JButton && ((JButton) component).getText().equals(text)) {
                return (JButton) component;
            } else if (component instanceof Container) {
                JButton button = findButtonByText((Container) component, text);
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            fail("Could not access field: " + fieldName + ", error: " + e.getMessage());
            return null;
        }
    }


    private void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterEach
    public void tearDown() {

        if (loginPanel != null) {
            loginPanel.dispose();
        }


        userDAO.deleteUser(testDoctor.getSsn());
        userDAO.deleteUser(testNurse.getSsn());


        for (Window window : Window.getWindows()) {
            window.dispose();
        }
    }
}