package GUI;

import javax.swing.*;
import java.awt.*;

public class ErrorPanel extends JFrame {

    public ErrorPanel(String msg) {
        setTitle("Error");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel label = new JLabel(msg, SwingConstants.CENTER);
        label.setBounds(50, 40, 300, 30);
        label.setFont(new Font("Arial", Font.BOLD, 18));

        JButton ok = new JButton("OK");
        ok.setBounds(150, 100, 100, 30);
        ok.addActionListener(e -> dispose());

        add(label);
        add(ok);

        setVisible(true);
    }
}