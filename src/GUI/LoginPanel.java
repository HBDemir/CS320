package GUI;

import DAO.UserDAO;
import Library.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JFrame {

    private JTextField ssnField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPanel() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Ekranın ortasında aç

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel ssnLabel = new JLabel("SSN:");
        ssnField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");

        panel.add(ssnLabel);
        panel.add(ssnField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // boşluk için
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ssn = ssnField.getText();
                String password = new String(passwordField.getPassword());

                UserDAO userDAO = new UserDAO();
                User user = userDAO.authenticateUser(ssn, password);

                if (user != null) {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Giriş başarılı! Hoş geldin, " + user.getUserName() +
                                    " (" + user.getUserRole() + ")",
                            "Başarılı Giriş",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Buraya role göre farklı panel açma işlemi eklenebilir.
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "SSN veya şifre hatalı.",
                            "Giriş Hatası",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPanel().setVisible(true);
        });
    }
}
