package com.praktikum.GUI;

import com.praktikum.Model.*;
import com.praktikum.utils.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private DataManager dataManager;
    private Image backgroundImage;
    private Image logoImage;

    public LoginFrame() {
        dataManager = new DataManager();
        loadImages();
        initComponents();
    }

    private void loadImages() {
        try {
            backgroundImage = new ImageIcon("C:\\Users\\ASUS\\Downloads\\hi\\bgyacht.jpeg").getImage();
            logoImage = new ImageIcon("C:\\Users\\ASUS\\Downloads\\hi\\logo.png").getImage();

            if (backgroundImage == null) {
                System.out.println("Background image not loaded!");
            } else {
                System.out.println("Background loaded successfully");
            }

            if (logoImage == null) {
                System.out.println("Logo image not loaded!");
            } else {
                System.out.println("Logo loaded successfully");
            }
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initComponents() {
        setTitle("Vela Privé - Login");
        setSize(500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, AppColors.NAVY, 0, getHeight(), AppColors.LIGHT_NAVY);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Logo di atas paling atas (MENGGUNAKAN FILE)
        JLabel topLogoLabel = new JLabel();
        if (logoImage != null) {
            ImageIcon scaledLogo = new ImageIcon(logoImage.getScaledInstance(120, 120, Image.SCALE_SMOOTH));
            topLogoLabel.setIcon(scaledLogo);
        } else {
            // Fallback jika logo tidak load
            topLogoLabel.setText("LOGO");
            topLogoLabel.setFont(new Font("Serif", Font.BOLD, 24));
            topLogoLabel.setForeground(AppColors.GOLD);
        }
        topLogoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        mainPanel.add(topLogoLabel, gbc);

        // Tagline
        JLabel taglineLabel = new JLabel("Your Gateway to Exquisite Yachts");
        taglineLabel.setFont(new Font("Serif", Font.ITALIC, 16));
        taglineLabel.setForeground(new Color(255, 255, 255, 230));
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 30, 10);
        mainPanel.add(taglineLabel, gbc);

        // Login Panel dengan transparansi
        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background transparan dengan blur effect
                g2d.setColor(new Color(255, 255, 255, 200)); // Putih semi-transparan
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        loginPanel.setOpaque(false);
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        GridBagConstraints lpGbc = new GridBagConstraints();
        lpGbc.insets = new Insets(10, 10, 10, 10);
        lpGbc.fill = GridBagConstraints.HORIZONTAL;
        lpGbc.gridx = 0;

        // Logo di dalam panel (di atas Welcome Aboard) - MENGGUNAKAN FILE
        JLabel innerLogoLabel = new JLabel();
        if (logoImage != null) {
            ImageIcon scaledInnerLogo = new ImageIcon(logoImage.getScaledInstance(90, 90, Image.SCALE_SMOOTH));
            innerLogoLabel.setIcon(scaledInnerLogo);
        } else {
            // Fallback jika logo tidak load
            innerLogoLabel.setText("LOGO");
            innerLogoLabel.setFont(new Font("Serif", Font.BOLD, 20));
            innerLogoLabel.setForeground(AppColors.NAVY);
        }
        innerLogoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lpGbc.gridy = 0;
        lpGbc.insets = new Insets(5, 10, 10, 10);
        loginPanel.add(innerLogoLabel, lpGbc);

        // Welcome Aboard Title
        JLabel loginTitle = new JLabel("Welcome Aboard", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Serif", Font.BOLD, 32));
        loginTitle.setForeground(new Color(30, 50, 80));
        lpGbc.gridy = 1;
        lpGbc.insets = new Insets(10, 10, 20, 10);
        loginPanel.add(loginTitle, lpGbc);

        // Email Field dengan icon
        JPanel emailPanel = createInputPanel("✉", "Email Address");
        emailField = (JTextField) emailPanel.getComponent(1);
        lpGbc.gridy = 2;
        lpGbc.insets = new Insets(10, 10, 10, 10);
        loginPanel.add(emailPanel, lpGbc);

        // Password Field dengan icon
        JPanel passwordPanel = createPasswordPanel("🔒", "Password");
        passwordField = (JPasswordField) passwordPanel.getComponent(1);
        lpGbc.gridy = 3;
        loginPanel.add(passwordPanel, lpGbc);

        // Forgot Password Link
        JLabel forgotLabel = new JLabel("<html><u>Forgot Password?</u></html>", SwingConstants.CENTER);
        forgotLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        forgotLabel.setForeground(new Color(50, 80, 120));
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lpGbc.gridy = 4;
        lpGbc.insets = new Insets(5, 10, 10, 10);
        loginPanel.add(forgotLabel, lpGbc);

        // Login Button dengan gradient gold
        JButton loginButton = new JButton("LOGIN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, new Color(218, 165, 32),
                        0, getHeight(), new Color(184, 134, 11));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setPreferredSize(new Dimension(200, 45));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lpGbc.gridy = 5;
        lpGbc.insets = new Insets(20, 10, 10, 10);
        loginPanel.add(loginButton, lpGbc);

        loginButton.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        mainPanel.add(loginPanel, gbc);

        add(mainPanel);
    }

    private JPanel createInputPanel(String icon, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panel.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        iconLabel.setForeground(new Color(150, 150, 150));
        panel.add(iconLabel, BorderLayout.WEST);

        JTextField textField = new JTextField();
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setOpaque(false);
        textField.setForeground(new Color(80, 80, 80));

        // Placeholder effect
        textField.setText(placeholder);
        textField.setForeground(new Color(150, 150, 150));
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(new Color(80, 80, 80));
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(new Color(150, 150, 150));
                }
            }
        });

        panel.add(textField, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(300, 45));

        return panel;
    }

    private JPanel createPasswordPanel(String icon, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panel.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        iconLabel.setForeground(new Color(150, 150, 150));
        panel.add(iconLabel, BorderLayout.WEST);

        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createEmptyBorder());
        passField.setOpaque(false);
        passField.setEchoChar((char) 0);
        passField.setText(placeholder);
        passField.setForeground(new Color(150, 150, 150));

        passField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passField.getPassword()).equals(placeholder)) {
                    passField.setText("");
                    passField.setEchoChar('●');
                    passField.setForeground(new Color(80, 80, 80));
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (passField.getPassword().length == 0) {
                    passField.setEchoChar((char) 0);
                    passField.setText(placeholder);
                    passField.setForeground(new Color(150, 150, 150));
                }
            }
        });

        panel.add(passField, BorderLayout.CENTER);

        JLabel lockIcon = new JLabel("🔒");
        lockIcon.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(lockIcon, BorderLayout.EAST);

        panel.setPreferredSize(new Dimension(300, 45));

        return panel;
    }

    private void performLogin() {
        try {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || email.equals("Email Address") ||
                    password.isEmpty() || password.equals("Password")) {
                throw new IllegalArgumentException("Email and password cannot be empty!");
            }

            User user = dataManager.authenticateUser(email, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login Successful!\nWelcome, " + user.getUsername());
                new DashboardFrame(dataManager, user).setVisible(true);
                dispose();
            } else {
                throw new IllegalArgumentException("Invalid email or password!");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}