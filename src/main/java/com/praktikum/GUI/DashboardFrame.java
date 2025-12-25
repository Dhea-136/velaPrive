package com.praktikum.GUI;

import com.praktikum.Model.*;
import com.praktikum.utils.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DashboardFrame extends JFrame {
    private DataManager dataManager;
    private User currentUser;
    private JLabel statsRentalsLabel, statsCustomersLabel, statsYachtsLabel, statsRevenueLabel;
    private Image backgroundImage;
    private Image logoImage;
    // Ganti dengan path file gambar lokal
    private String[] yachtImages = {
            "C:\\Users\\ASUS\\Downloads\\hi\\azure.jpg",  // File gambar yacht 1
            "C:\\Users\\ASUS\\Downloads\\hi\\serenity1.jpg",  // File gambar yacht 2
            "C:\\Users\\ASUS\\Downloads\\hi\\oceanpearl.jpeg"   // File gambar yacht 3
    };

    public DashboardFrame(DataManager dataManager, User user) {
        this.dataManager = dataManager;
        this.currentUser = user;
        loadImages();
        initComponents();
        updateStats();
    }

    private void loadImages() {
        try {
            backgroundImage = new ImageIcon("C:\\Users\\ASUS\\Downloads\\hi\\laut.jpeg").getImage();
            logoImage = new ImageIcon("C:\\Users\\ASUS\\Downloads\\hi\\logo.png").getImage();
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }
    }

    private void initComponents() {
        setTitle("Vela Privé - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                if (backgroundImage != null) {
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g2d.setColor(new Color(0, 0, 0, 120));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g2d.setColor(AppColors.DEEPNAVY);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setOpaque(false);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(20, 40, 80, 230));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel logoTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        logoTitlePanel.setOpaque(false);

        if (logoImage != null) {
            JLabel logoLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    int size = 50;
                    g2d.drawImage(logoImage, 0, 0, size, size, this);
                }
            };
            logoLabel.setPreferredSize(new Dimension(50, 50));
            logoTitlePanel.add(logoLabel);
        }

        headerPanel.add(logoTitlePanel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Welcome, " + currentUser.getUsername() + " | ");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userPanel.add(userLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(AppColors.GOLD);
        logoutBtn.setForeground(AppColors.NAVY);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());
        userPanel.add(logoutBtn);
        headerPanel.add(userPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        statsRentalsLabel = new JLabel("0");
        statsCustomersLabel = new JLabel("0");
        statsYachtsLabel = new JLabel("0");
        statsRevenueLabel = new JLabel("$0");

        statsPanel.add(createStatCard("Total Rentals", statsRentalsLabel));
        statsPanel.add(createStatCard("Total Customers", statsCustomersLabel));
        statsPanel.add(createStatCard("Available Yachts", statsYachtsLabel));
        statsPanel.add(createStatCard("Total Revenue", statsRevenueLabel));

        contentPanel.add(statsPanel, BorderLayout.NORTH);

        // Yacht Gallery Panel - Menggunakan file lokal
        JPanel galleryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        galleryPanel.setOpaque(false);
        galleryPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Tambahkan yacht images dari file lokal
        galleryPanel.add(createYachtImagePanel(yachtImages[0], "Luxury Yacht Azure", "150ft", "$25,000/day", 0));
        galleryPanel.add(createYachtImagePanel(yachtImages[1], "Royal Serenity", "180ft", "$35,000/day", 1));
        galleryPanel.add(createYachtImagePanel(yachtImages[2], "Ocean Pearl", "200ft", "$45,000/day", 2));

        contentPanel.add(galleryPanel, BorderLayout.CENTER);

        // Welcome message
        JPanel welcomePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        welcomePanel.setOpaque(false);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(30, 50, 90, 240));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setOpaque(false);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel sidebarTitle = new JLabel("  MENU");
        sidebarTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sidebarTitle.setForeground(AppColors.GOLD);
        sidebarTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarTitle.setBorder(BorderFactory.createEmptyBorder(10, 15, 20, 15));
        sidebar.add(sidebarTitle);

        sidebar.add(createSidebarButton("🏠 Dashboard", e -> {}));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createSidebarButton("👥 Customers", e -> openCustomers()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createSidebarButton("🛥️ Yachts", e -> openYachts()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(createSidebarButton("💼 Rentals", e -> openRentals()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createSidebarButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(220, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBackground(new Color(30, 50, 90, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(218, 165, 32)); // gold solid
                button.setForeground(AppColors.NAVY);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(30, 50, 90)); // navy solid
                button.setForeground(Color.WHITE);
            }
        });


        button.addActionListener(action);
        return button;
    }

    private JPanel createYachtImagePanel(String imagePath, String name, String length, String price, int yachtId) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            // Load dari file lokal
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(280, 180, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppColors.GOLD, 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            card.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel placeholder = new JLabel("Yacht Image", SwingConstants.CENTER);
            placeholder.setFont(new Font("SansSerif", Font.ITALIC, 14));
            placeholder.setForeground(Color.GRAY);
            card.add(placeholder, BorderLayout.CENTER);
        }

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        nameLabel.setForeground(AppColors.NAVY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lengthLabel = new JLabel(length + " • " + price);
        lengthLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lengthLabel.setForeground(new Color(100, 100, 100));
        lengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lengthLabel);

        card.add(infoPanel, BorderLayout.SOUTH);

        // View Details Button
        JButton detailBtn = new JButton("View Details") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(184, 134, 11));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(238, 180, 34));
                } else {
                    g2d.setColor(new Color(218, 165, 32));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        detailBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        detailBtn.setForeground(Color.WHITE);
        detailBtn.setFocusPainted(false);
        detailBtn.setBorderPainted(false);
        detailBtn.setContentAreaFilled(false);
        detailBtn.setPreferredSize(new Dimension(120, 35));
        detailBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailBtn.addActionListener(e -> openYachtDetail(yachtId, name, length, price, imagePath));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(detailBtn);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);
        containerPanel.add(card, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(containerPanel);

        return mainPanel;
    }

    private void openYachtDetail(int yachtId, String name, String length, String price, String imagePath) {
        new YachtDetailFrame(yachtId, name, length, price, imagePath).setVisible(true);
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.GOLD, 2),
                BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titleLbl.setForeground(AppColors.NAVY);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        valueLabel.setForeground(AppColors.GOLD);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private void updateStats() {
        statsRentalsLabel.setText(String.valueOf(dataManager.getRentals().size()));
        statsCustomersLabel.setText(String.valueOf(dataManager.getCustomers().size()));

        long availableYachts = dataManager.getYachts().stream()
                .filter(y -> y.getStatus().equals("Available")).count();
        statsYachtsLabel.setText(String.valueOf(availableYachts));

        double totalRevenue = dataManager.getRentals().stream()
                .mapToDouble(Rental::getTotalPrice).sum();
        statsRevenueLabel.setText(String.format("$%.2f", totalRevenue));
    }

    private void openCustomers() {
        new CustomerFrame(dataManager, this).setVisible(true);
    }

    private void openYachts() {
        new YachtFrame(dataManager).setVisible(true);
    }

    private void openRentals() {
        new RentalFrame(dataManager, this).setVisible(true);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }

    public void refreshStats() {
        updateStats();
    }
}