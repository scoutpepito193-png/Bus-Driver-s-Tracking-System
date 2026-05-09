package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import Service.LogInService;
import Service.SuperAdminService;

public class Menu extends JFrame {
    
    private SuperAdminService sas = new SuperAdminService();

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Menu());
    }

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public Menu() {

        setTitle("Trackify");

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setSize(1400, 900);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(true);

        setMinimumSize(new Dimension(800, 600));

        setIconImage(createAppIcon());

        // LOAD MENU PANEL
        showMenuPanel();

        setVisible(true);
    }

    // =========================================================
    // CREATE MAIN PANEL
    // =========================================================
    public JPanel createMainPanel() {

        JPanel mainPanel = new GradientPanel();

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        addComponents(mainPanel);

        return mainPanel;
    }

    // =========================================================
    // SHOW MENU PANEL
    // =========================================================
    public void showMenuPanel() {

        setContentPane(createMainPanel());

        revalidate();

        repaint();
    }

    // =========================================================
    // APP ICON
    // =========================================================
    private Image createAppIcon() {

        BufferedImage icon = new BufferedImage(
                64,
                64,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = icon.createGraphics();

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2d.setColor(new Color(25, 103, 210));

        g2d.fillRoundRect(0, 0, 64, 64, 10, 10);

        g2d.setColor(Color.WHITE);

        g2d.setStroke(new BasicStroke(2));

        g2d.drawRoundRect(10, 15, 44, 28, 4, 4);

        g2d.fillRect(14, 18, 8, 6);
        g2d.fillRect(25, 18, 8, 6);
        g2d.fillRect(36, 18, 8, 6);

        g2d.fillOval(16, 42, 6, 6);
        g2d.fillOval(42, 42, 6, 6);

        g2d.dispose();

        return icon;
    }

    // =========================================================
    // ADD COMPONENTS
    // =========================================================
    private void addComponents(JPanel mainPanel) {

        // =========================
        // NAVBAR
        // =========================
        JPanel navBar = createNavBar();

        navBar.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 70)
        );

        navBar.setPreferredSize(
                new Dimension(Integer.MAX_VALUE, 70)
        );

        mainPanel.add(navBar);

        mainPanel.add(Box.createVerticalStrut(30));

        // =========================
        // LOGO SECTION
        // =========================
        JPanel logoSection = new JPanel();

        logoSection.setOpaque(false);

        logoSection.setLayout(
                new BoxLayout(logoSection, BoxLayout.Y_AXIS)
        );

        logoSection.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 200)
        );

        logoSection.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel logoPanel = createLogoPanel();

        logoPanel.setMaximumSize(new Dimension(150, 150));

        logoPanel.setPreferredSize(new Dimension(150, 150));

        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoSection.add(logoPanel);

        JLabel titleLabel = new JLabel("Trackify");

        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 72)
        );

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoSection.add(Box.createVerticalStrut(10));

        logoSection.add(titleLabel);

        JLabel subtitleLabel = new JLabel(
                "Bus Driver Tracking & Management System"
        );

        subtitleLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 20)
        );

        subtitleLabel.setForeground(
                new Color(200, 220, 240)
        );

        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoSection.add(Box.createVerticalStrut(8));

        logoSection.add(subtitleLabel);

        mainPanel.add(logoSection);

        mainPanel.add(Box.createVerticalStrut(40));

        // =========================
        // BUTTON SECTION
        // =========================
        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setOpaque(false);

        buttonsPanel.setLayout(
                new FlowLayout(FlowLayout.CENTER, 80, 20)
        );

        buttonsPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 150)
        );

        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // LOGIN BUTTON
        RoundedGradientButton loginBtn =
                new RoundedGradientButton(
                        "LOGIN PORTAL",
                        new Color(52, 152, 219),
                        new Color(25, 103, 210)
                );

        loginBtn.setPreferredSize(
                new Dimension(400, 120)
        );

        loginBtn.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        // SWITCH TO LOGIN PANEL
// SWITCH TO LOGIN PANEL
loginBtn.addActionListener(e -> {

    loginBtn.setEnabled(false);

    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {

        @Override
        protected Boolean doInBackground() throws Exception {

            // DATABASE CHECK RUNS IN BACKGROUND
            return sas.checkAccout();
        }

        @Override
        protected void done() {

            try {

                boolean hasSuperAdmin = get();

                if (!hasSuperAdmin) {

                    int choice = JOptionPane.showConfirmDialog(
                            Menu.this,
                            "No SuperAdmin account exists yet. Please create a SuperAdmin first.",
                            "Setup Required",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (choice == JOptionPane.YES_OPTION) {

                        setContentPane(new SuperAdminSignupPanel(Menu.this));

                        revalidate();

                        repaint();
                    }

                } else {

                    setContentPane(new LogInPanel(Menu.this));

                    revalidate();

                    repaint();
                }

            } catch (Exception ex) {

                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                        Menu.this,
                        "Failed to connect to database.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

            } finally {

                loginBtn.setEnabled(true);
            }
        }
    };

    worker.execute();
});

        buttonsPanel.add(loginBtn);

        mainPanel.add(buttonsPanel);

        mainPanel.add(Box.createVerticalGlue());

        // =========================
        // FOOTER
        // =========================
        JPanel footerPanel = new JPanel();

        footerPanel.setOpaque(false);

        footerPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 30)
        );

        footerPanel.setLayout(new BorderLayout());

        JLabel footerLabel = new JLabel(
                "(c) 2026 Trackify. All rights reserved. | Secure Bus Fleet Management"
        );

        footerLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        footerLabel.setForeground(
                new Color(150, 170, 190)
        );

        footerLabel.setHorizontalAlignment(JLabel.CENTER);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        mainPanel.add(footerPanel);
    }

    // =========================================================
    // NAVBAR
    // =========================================================
    private JPanel createNavBar() {

        JPanel navBar = new JPanel();

        navBar.setLayout(new BorderLayout());

        navBar.setBackground(new Color(20, 40, 80));

        navBar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                2,
                                0,
                                new Color(52, 152, 219)
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                20,
                                10,
                                20
                        )
                )
        );

        JLabel logo = new JLabel("Trackify");

        logo.setFont(
                new Font("Segoe UI", Font.BOLD, 22)
        );

        logo.setForeground(
                new Color(52, 152, 219)
        );

        JLabel infoLabel = new JLabel(
                "Real-time Bus Fleet Management System"
        );

        infoLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        infoLabel.setForeground(
                new Color(150, 170, 190)
        );

        navBar.add(logo, BorderLayout.WEST);

        navBar.add(infoLabel, BorderLayout.EAST);

        return navBar;
    }

    // =========================================================
    // LOGO PANEL
    // =========================================================
    private JPanel createLogoPanel() {

        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                int centerX = getWidth() / 2;

                int centerY = getHeight() / 2;

                // OUTER CIRCLE
                g2d.setColor(
                        new Color(52, 152, 219, 100)
                );

                g2d.fillOval(
                        centerX - 60,
                        centerY - 60,
                        120,
                        120
                );

                // BUS BODY
                g2d.setColor(Color.WHITE);

                g2d.setStroke(new BasicStroke(2));

                g2d.drawRoundRect(
                        centerX - 45,
                        centerY - 30,
                        90,
                        45,
                        12,
                        12
                );

                // WINDOWS
                g2d.fillRect(centerX - 38, centerY - 26, 12, 9);
                g2d.fillRect(centerX - 19, centerY - 26, 12, 9);
                g2d.fillRect(centerX, centerY - 26, 12, 9);
                g2d.fillRect(centerX + 19, centerY - 26, 12, 9);

                // DOOR
                g2d.setStroke(new BasicStroke(1));

                g2d.drawRect(
                        centerX + 32,
                        centerY - 20,
                        12,
                        26
                );

                // WHEELS
                g2d.fillOval(centerX - 35, centerY + 13, 12, 12);
                g2d.fillOval(centerX + 23, centerY + 13, 12, 12);

                // GPS SIGNAL
                g2d.setColor(new Color(46, 204, 113));

                g2d.drawOval(centerX + 40, centerY - 40, 6, 6);

                g2d.fillOval(centerX + 42, centerY - 38, 3, 3);

                g2d.drawOval(centerX + 36, centerY - 44, 14, 14);

                g2d.drawOval(centerX + 32, centerY - 48, 22, 22);
            }
        };

        panel.setOpaque(false);

        return panel;
    }

    // =========================================================
    // GRADIENT PANEL
    // =========================================================
    class GradientPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            new Color(15, 60, 120),
                            getWidth(),
                            getHeight(),
                            new Color(40, 120, 200)
                    );

            g2d.setPaint(gradient);

            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(new Color(255, 255, 255, 15));

            g2d.fillOval(-200, -150, 600, 600);

            g2d.fillOval(
                    getWidth() - 100,
                    getHeight() - 400,
                    700,
                    700
            );

            g2d.setColor(new Color(52, 152, 219, 50));

            g2d.setStroke(new BasicStroke(3));

            g2d.drawLine(0, 70, getWidth(), 70);
        }
    }

    // =========================================================
    // CUSTOM BUTTON
    // =========================================================
    class RoundedGradientButton extends JButton {

        private int arc = 25;

        private Color startColor;

        private Color endColor;

        public RoundedGradientButton(
                String text,
                Color startColor,
                Color endColor
        ) {

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

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            Color topColor = startColor;

            Color bottomColor = endColor;

            if (getModel().isPressed()) {

                topColor = topColor.darker();

                bottomColor = bottomColor.darker();

            } else if (getModel().isRollover()) {

                topColor = topColor.brighter();

                bottomColor = bottomColor.brighter();
            }

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            topColor,
                            0,
                            getHeight(),
                            bottomColor
                    );

            g2.setPaint(gradient);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    arc,
                    arc
            );

            super.paintComponent(g);
        }
    }
}