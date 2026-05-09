package GUI;

import javax.swing.*;
import java.awt.*;
import Service.LogInService;
import Model.AuthResult;
import Model.SuperAdmin;
import Model.SubAdmin;
import Model.Driver;

public class LogInPanel extends JPanel {

    private JFrame parentFrame;
    private LogInService logs = new LogInService();

    public LogInPanel(JFrame parentFrame) {

        this.parentFrame = parentFrame;

        setLayout(new BorderLayout());
        setOpaque(false);

        add(createNavBar(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    // =========================
    // NAVBAR
    // =========================
    private JPanel createNavBar() {

        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(20, 40, 80));

        navBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0,
                        new Color(52, 152, 219)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JLabel logo = new JLabel("Trackify");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(new Color(52, 152, 219));

        JLabel info = new JLabel("Unified Login Portal");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.setForeground(new Color(150, 170, 190));

        navBar.add(logo, BorderLayout.WEST);
        navBar.add(info, BorderLayout.EAST);

        return navBar;
    }

    // =========================
    // CENTER PANEL
    // =========================
    private JPanel createCenterPanel() {

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JPanel loginCard = new JPanel();
        loginCard.setPreferredSize(new Dimension(500, 450));
        loginCard.setBackground(new Color(255, 255, 255, 30));

        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1, true),
                BorderFactory.createEmptyBorder(35, 40, 35, 40)
        ));

        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));

        // =========================
        // TITLE
        // =========================
        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 42));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Access your Trackify account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(210, 220, 235));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginCard.add(title);
        loginCard.add(Box.createVerticalStrut(8));
        loginCard.add(subtitle);
        loginCard.add(Box.createVerticalStrut(35));

        // =========================
        // USERNAME
        // =========================
        JLabel usernameLabel = createFieldLabel("Username");
        JTextField usernameField = new JTextField();
        styleTextField(usernameField);

        // =========================
        // PASSWORD
        // =========================
        JLabel passwordLabel = createFieldLabel("Password");
        JPasswordField passwordField = new JPasswordField();
        styleTextField(passwordField);

        // =========================
        // LOGIN BUTTON
        // =========================
        RoundedGradientButton loginBtn =
                new RoundedGradientButton(
                        "LOGIN",
                        new Color(52, 152, 219),
                        new Color(25, 103, 210)
                );

        loginBtn.setPreferredSize(new Dimension(300, 55));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));

        // =========================
        // LOGIN ACTION (ROLE ROUTING)
        // =========================
        loginBtn.addActionListener(e -> {

            String id = usernameField.getText();
            String password = new String(passwordField.getPassword());

            AuthResult result = logs.logIn(id, password);

            if (result == null) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Invalid ID or password");
                return;
            }
            
            JPanel nextPanel = null;

            switch (result.getRole())
            {

                case SUPER_ADMIN ->
                {
                    SuperAdmin admin = (SuperAdmin) result.getUser();
                    nextPanel = new SuperAdminDashboard(parentFrame, admin);
                }

                case SUB_ADMIN ->
                {
                    SubAdmin subAdmin = (SubAdmin) result.getUser();
                    nextPanel = new SubAdminDashboard(parentFrame, subAdmin);
                }

                case DRIVER ->
                {
                    Driver driver = (Driver) result.getUser();
                    nextPanel = new DriverDashboard(parentFrame, driver);
                }
            }

            parentFrame.setContentPane(nextPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        // =========================
        // BACK BUTTON
        // =========================
        JButton backBtn = new JButton("← Back to Menu");

        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);

        backBtn.setForeground(new Color(180, 220, 255));
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        backBtn.addActionListener(e -> {

            parentFrame.setContentPane(new Menu().createMainPanel());
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        // =========================
        // ADD COMPONENTS
        // =========================
        loginCard.add(usernameLabel);
        loginCard.add(Box.createVerticalStrut(8));
        loginCard.add(usernameField);

        loginCard.add(Box.createVerticalStrut(20));

        loginCard.add(passwordLabel);
        loginCard.add(Box.createVerticalStrut(8));
        loginCard.add(passwordField);

        loginCard.add(Box.createVerticalStrut(35));

        loginCard.add(loginBtn);
        loginCard.add(Box.createVerticalStrut(15));
        loginCard.add(backBtn);

        wrapper.add(loginCard);

        return wrapper;
    }

    // =========================
    // FOOTER
    // =========================
    private JPanel createFooter() {

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JLabel label = new JLabel("(c) 2026 Trackify");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(new Color(170, 190, 210));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        footer.add(label, BorderLayout.CENTER);

        return footer;
    }

    // =========================
    // HELPERS
    // =========================
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        return label;
    }

    private void styleTextField(JTextField field) {

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 230), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }

    // =========================
    // BUTTON CLASS
    // =========================
    class RoundedGradientButton extends JButton {

        private int arc = 25;
        private Color startColor;
        private Color endColor;

        public RoundedGradientButton(String text, Color startColor, Color endColor) {

            super(text);

            this.startColor = startColor;
            this.endColor = endColor;

            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Color top = startColor;
            Color bottom = endColor;

            if (getModel().isPressed()) {
                top = top.darker();
                bottom = bottom.darker();
            } else if (getModel().isRollover()) {
                top = top.brighter();
                bottom = bottom.brighter();
            }

            GradientPaint gp = new GradientPaint(
                    0, 0, top,
                    0, getHeight(), bottom
            );

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

            super.paintComponent(g);
        }
    }
}