package gui;
import DAO.UserDAO;
import Library.Nurse;
import Library.User;
import com.sun.tools.javac.Main;

import javax.print.Doc;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class AuthenticationWindow {

    public static void main(String[] args) {


        UserDAO userDAO = new UserDAO();
        JFrame frame = new JFrame("Authentication");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        JLabel ssnLabel = new JLabel("SSN:");
        JTextField ssnField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(ssnLabel, gbc);

        gbc.gridx = 1;
        frame.add(ssnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(passwordLabel, gbc);

        gbc.gridx = 1;
        frame.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ssn = ssnField.getText();
                String password = new String(passwordField.getPassword());
                User user = userDAO.authenticateUser(ssn,password);
                if (Objects.equals(user.getUserRole(), "Doctor")){
                    DoctorWindow doctorWindow = new DoctorWindow();
                    doctorWindow.setVisible(true);

                }
                else if (Objects.equals(user.getUserRole(), "Nurse")){
                    NurseWindow nurseWindow = new NurseWindow();
                    nurseWindow.setVisible(true);
                }



            }
        });

        frame.setVisible(true);
    }
}