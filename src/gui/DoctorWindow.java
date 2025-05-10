package gui;

import javax.swing.*;
import java.awt.*;

public class DoctorWindow extends JFrame {

    public DoctorWindow() {
        setTitle("Doctor Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Welcome, Doctor!");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        add(label);
    }
}
