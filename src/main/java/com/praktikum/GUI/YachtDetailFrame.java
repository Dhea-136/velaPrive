package com.praktikum.GUI;

import javax.swing.*;
import java.awt.*;

public class YachtDetailFrame extends JFrame {
    private String yachtName, length, price, imagePath;
    private int yachtId;
    private Image backgroundImage;

    public YachtDetailFrame(int yachtId, String name, String length, String price, String imagePath) {
        this.yachtId = yachtId;
        this.yachtName = name;
        this.length = length;
        this.price = price;
        this.imagePath = imagePath;
        loadBackgroundImage();
        initComponents();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = new ImageIcon("background.jpg").getImage();
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
    }

    private void initComponents() {
        setTitle("Yacht Details - " + yachtName);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g2d.setColor(new Color(0, 0, 0, 150));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(30, 50, 80, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(yachtName);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(AppColors.GOLD);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton closeBtn = new JButton("✕") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(184, 134, 11));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(238, 180, 34));
                } else {
                    g2d.setColor(AppColors.GOLD);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setPreferredSize(new Dimension(45, 45));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        headerPanel.add(closeBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Image Panel - Load dari file lokal
        JPanel imagePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 250));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        imagePanel.setOpaque(false);
        imagePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.GOLD, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        try {
            // Load dari file lokal
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(680, 330, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel placeholder = new JLabel("Yacht Image", SwingConstants.CENTER);
            placeholder.setFont(new Font("SansSerif", Font.BOLD, 24));
            placeholder.setForeground(Color.GRAY);
            imagePanel.add(placeholder, BorderLayout.CENTER);
        }

        mainPanel.add(imagePanel, BorderLayout.CENTER);

        // Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.GOLD, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        addDetailRow(detailsPanel, "Length:", length);
        addDetailRow(detailsPanel, "Price:", price);
        addDetailRow(detailsPanel, "Status:", "Available");
        addDetailRow(detailsPanel, "Capacity:", "12 Guests");

        mainPanel.add(detailsPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblLabel.setForeground(AppColors.NAVY);

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        valLabel.setForeground(new Color(80, 80, 80));

        panel.add(lblLabel);
        panel.add(valLabel);
    }
}