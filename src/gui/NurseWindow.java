package gui;

import javax.swing.*;
import java.awt.*;

public class NurseWindow extends JFrame {

    public NurseWindow() {
        setTitle("Nurse Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Welcome, Nurse!");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        add(label);
    }
}
