package org.rentalyacht;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

// ==================== MAIN APPLICATION ====================
public class VelaPriveApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}

// ==================== COLOR SCHEME ====================
class AppColors {
    static final Color NAVY = new Color(0, 32, 63);
    static final Color GOLD = new Color(212, 175, 55);
    static final Color LIGHT_NAVY = new Color(28, 55, 90);
    static final Color CREAM = new Color(255, 250, 240);
    static final Color DEEPNAVY = new Color(15, 30, 60);
    static final Color ROYALGOLD = new Color(184, 134, 11);
    static final Color NAWA= new Color(158, 197, 210, 242);
    static final Color WHITE = Color.WHITE;
    static final Color DAY_SKY_BLUE= new Color(130, 202, 255, 255);
}

// ==================== MODELS ====================
class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public String toFileString() {
        return username + "," + password + "," + role;
    }

    public static User fromFileString(String line) {
        String[] parts = line.split(",");
        return new User(parts[0], parts[1], parts[2]);
    }
}

class Customer {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;

    public Customer(String id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }

    public String toFileString() {
        return id + "," + name + "," + phone + "," + email + "," + address;
    }

    public static Customer fromFileString(String line) {
        String[] parts = line.split(",");
        return new Customer(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }
}

class Yacht {
    private String id;
    private String name;
    private String type;
    private int capacity;
    private double pricePerDay;
    private String status;

    public Yacht(String id, String name, String type, int capacity, double pricePerDay, String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.pricePerDay = pricePerDay;
        this.status = status;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getCapacity() { return capacity; }
    public double getPricePerDay() { return pricePerDay; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return id + "," + name + "," + type + "," + capacity + "," + pricePerDay + "," + status;
    }

    public static Yacht fromFileString(String line) {
        String[] parts = line.split(",");
        return new Yacht(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]),
                Double.parseDouble(parts[4]), parts[5]);
    }
}

class Rental {
    private String id;
    private String customerId;
    private String yachtId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    private String status;

    public Rental(String id, String customerId, String yachtId, LocalDate startDate,
                  LocalDate endDate, double totalPrice, String status) {
        this.id = id;
        this.customerId = customerId;
        this.yachtId = yachtId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getYachtId() { return yachtId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return id + "," + customerId + "," + yachtId + "," + startDate + "," + endDate + "," + totalPrice + "," + status;
    }

    public static Rental fromFileString(String line) {
        String[] parts = line.split(",");
        return new Rental(parts[0], parts[1], parts[2],
                LocalDate.parse(parts[3]), LocalDate.parse(parts[4]),
                Double.parseDouble(parts[5]), parts[6]);
    }
}

// ==================== DATA MANAGER ====================
class DataManager {
    private static final String USERS_FILE = "users.txt";
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String YACHTS_FILE = "yachts.txt";
    private static final String RENTALS_FILE = "rentals.txt";

    private List<User> users;
    private List<Customer> customers;
    private List<Yacht> yachts;
    private List<Rental> rentals;

    public DataManager() {
        users = new ArrayList<>();
        customers = new ArrayList<>();
        yachts = new ArrayList<>();
        rentals = new ArrayList<>();
        loadAllData();
    }

    private void loadAllData() {
        loadUsers();
        loadCustomers();
        loadYachts();
        loadRentals();
    }

    private void loadUsers() {
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                users.add(new User("admin", "admin123", "Admin"));
                saveUsers();
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(User.fromFileString(line));
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading users: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        try {
            File file = new File(CUSTOMERS_FILE);
            if (!file.exists()) return;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                customers.add(Customer.fromFileString(line));
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading customers: " + e.getMessage());
        }
    }

    private void loadYachts() {
        try {
            File file = new File(YACHTS_FILE);
            if (!file.exists()) {
                // Add sample yachts
                yachts.add(new Yacht("Y001", "Azure Dream", "Motor Yacht", 12, 5000, "Available"));
                yachts.add(new Yacht("Y002", "Blue Breeze", "Sailing Yacht", 8, 3500, "Available"));
                yachts.add(new Yacht("Y003", "Ocean Pearl", "Catamaran", 15, 6000, "Available"));
                saveYachts();
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                yachts.add(Yacht.fromFileString(line));
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading yachts: " + e.getMessage());
        }
    }

    private void loadRentals() {
        try {
            File file = new File(RENTALS_FILE);
            if (!file.exists()) return;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                rentals.add(Rental.fromFileString(line));
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading rentals: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE));
            for (User user : users) {
                writer.write(user.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving users: " + e.getMessage());
        }
    }

    public void saveCustomers() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE));
            for (Customer customer : customers) {
                writer.write(customer.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving customers: " + e.getMessage());
        }
    }

    public void saveYachts() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(YACHTS_FILE));
            for (Yacht yacht : yachts) {
                writer.write(yacht.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving yachts: " + e.getMessage());
        }
    }

    public void saveRentals() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(RENTALS_FILE));
            for (Rental rental : rentals) {
                writer.write(rental.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving rentals: " + e.getMessage());
        }
    }

    public User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public List<Customer> getCustomers() { return customers; }
    public List<Yacht> getYachts() { return yachts; }
    public List<Rental> getRentals() { return rentals; }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomers();
    }

    public void updateCustomer(Customer customer) {
        saveCustomers();
    }

    public void deleteCustomer(String id) {
        customers.removeIf(c -> c.getId().equals(id));
        saveCustomers();
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
        saveRentals();
    }

    public void updateRental(Rental rental) {
        saveRentals();
    }

    public void deleteRental(String id) {
        rentals.removeIf(r -> r.getId().equals(id));
        saveRentals();
    }

    public Customer getCustomerById(String id) {
        return customers.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    public Yacht getYachtById(String id) {
        return yachts.stream().filter(y -> y.getId().equals(id)).findFirst().orElse(null);
    }

    public String generateCustomerId() {
        return "C" + String.format("%03d", customers.size() + 1);
    }

    public String generateRentalId() {
        return "R" + String.format("%03d", rentals.size() + 1);
    }
}

// ==================== LOGIN FRAME ====================
class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private DataManager dataManager;

    public LoginFrame() {
        dataManager = new DataManager();
        initComponents();
    }

    private void initComponents() {
        setTitle("Vela Prive - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, AppColors.NAVY, 0, getHeight(), AppColors.LIGHT_NAVY);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Logo/Title
        JLabel titleLabel = new JLabel("VELA PRIVE");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        titleLabel.setForeground(AppColors.GOLD);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Luxury Yacht Rental");
        subtitleLabel.setFont(new Font("SansSerif", Font.ITALIC, 18));
        subtitleLabel.setForeground(AppColors.CREAM);
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);

        // Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(255, 255, 255, 240));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints lpGbc = new GridBagConstraints();
        lpGbc.insets = new Insets(8, 8, 8, 8);
        lpGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("Login");
        loginTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        loginTitle.setForeground(AppColors.NAVY);
        lpGbc.gridx = 0;
        lpGbc.gridy = 0;
        lpGbc.gridwidth = 2;
        loginPanel.add(loginTitle, lpGbc);

        lpGbc.gridwidth = 1;
        lpGbc.gridy = 1;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        loginPanel.add(userLabel, lpGbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lpGbc.gridx = 1;
        loginPanel.add(usernameField, lpGbc);

        lpGbc.gridx = 0;
        lpGbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        loginPanel.add(passLabel, lpGbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lpGbc.gridx = 1;
        loginPanel.add(passwordField, lpGbc);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setBackground(AppColors.GOLD);
        loginButton.setForeground(AppColors.NAVY);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lpGbc.gridx = 0;
        lpGbc.gridy = 3;
        lpGbc.gridwidth = 2;
        lpGbc.insets = new Insets(20, 8, 8, 8);
        loginPanel.add(loginButton, lpGbc);

        loginButton.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        mainPanel.add(loginPanel, gbc);

        JLabel infoLabel = new JLabel("Default: admin / admin123");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        infoLabel.setForeground(AppColors.DEEPNAVY);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(infoLabel, gbc);

        add(mainPanel);
    }

    private void performLogin() {
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("Username and password cannot be empty!");
            }

            User user = dataManager.authenticateUser(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login Successful!\nWelcome, " + user.getUsername());
                new DashboardFrame(dataManager, user).setVisible(true);
                dispose();
            } else {
                throw new IllegalArgumentException("Invalid username or password!");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// ==================== DASHBOARD FRAME ====================
class DashboardFrame extends JFrame {
    private DataManager dataManager;
    private User currentUser;
    private JLabel statsRentalsLabel, statsCustomersLabel, statsYachtsLabel, statsRevenueLabel;
    private String[] yachtImages = {
            "https://www.superyachttimes.com/images/linkedin/yacht-7816.jpg",
            "https://www.benettiyachts.it/sites/default/files/2021-02/BENETTI-FB802-027.jpg",
            "https://www.azimutyachts.com/images/yachts/grande-32m/gallery/Azimut_Grande_32M_ext_01.jpg"
    };

    public DashboardFrame(DataManager dataManager, User user) {
        this.dataManager = dataManager;
        this.currentUser = user;
        initComponents();
        updateStats();
    }

    private void initComponents() {
        setTitle("Vela Prive - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppColors.DEEPNAVY);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.NAVY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("VELA PRIVE");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(AppColors.GOLD);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(AppColors.NAVY);
        JLabel userLabel = new JLabel("Welcome, " + currentUser.getUsername() + " | ");
        userLabel.setForeground(AppColors.DEEPNAVY);
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
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(AppColors.NAWA);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(AppColors.NAWA);

        statsRentalsLabel = new JLabel("0");
        statsCustomersLabel = new JLabel("0");
        statsYachtsLabel = new JLabel("0");
        statsRevenueLabel = new JLabel("$0");

        statsPanel.add(createStatCard("Total Rentals", statsRentalsLabel));
        statsPanel.add(createStatCard("Total Customers", statsCustomersLabel));
        statsPanel.add(createStatCard("Available Yachts", statsYachtsLabel));
        statsPanel.add(createStatCard("Total Revenue", statsRevenueLabel));

        contentPanel.add(statsPanel, BorderLayout.NORTH);

        // Yacht Gallery Panel
        JPanel galleryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        galleryPanel.setBackground(AppColors.NAWA);
        galleryPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Add yacht images
        try {
            galleryPanel.add(createYachtImagePanel("https://images.unsplash.com/photo-1567899378494-47b22a2ae96a?w=500"));
            galleryPanel.add(createYachtImagePanel("https://images.unsplash.com/photo-1605281317010-fe5ffe798166?w=500"));
            galleryPanel.add(createYachtImagePanel("https://images.unsplash.com/photo-1569263979104-865ab7cd8d13?w=500"));
        } catch (Exception e) {
            // Fallback to placeholder panels if images can't load
            for (int i = 0; i < 3; i++) {
                galleryPanel.add(createPlaceholderYachtPanel(i + 1));
            }
        }

        contentPanel.add(galleryPanel, BorderLayout.CENTER);

        // Welcome message
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(AppColors.CREAM);
        JLabel welcomeLabel = new JLabel("Luxury Yacht Rental Management System");
        welcomeLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
        welcomeLabel.setForeground(AppColors.LIGHT_NAVY);
        welcomePanel.add(welcomeLabel);
        contentPanel.add(welcomePanel, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppColors.LIGHT_NAVY);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Dashboard title in sidebar
        JLabel sidebarTitle = new JLabel("  MENU");
        sidebarTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        sidebarTitle.setForeground(AppColors.GOLD);
        sidebarTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarTitle.setBorder(BorderFactory.createEmptyBorder(10, 15, 20, 15));
        sidebar.add(sidebarTitle);

        // Menu items
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
        button.setBackground(AppColors.LIGHT_NAVY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(AppColors.NAVY);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(AppColors.LIGHT_NAVY);
            }
        });

        button.addActionListener(action);
        return button;
    }

    private JPanel createYachtImagePanel(String imageUrl) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.GOLD, 3),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(imageUrl));
            Image image = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            panel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel placeholder = new JLabel("Yacht Image", SwingConstants.CENTER);
            placeholder.setFont(new Font("SansSerif", Font.ITALIC, 14));
            placeholder.setForeground(Color.GRAY);
            panel.add(placeholder, BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createPlaceholderYachtPanel(int number) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppColors.NAVY);
        panel.setBorder(BorderFactory.createLineBorder(AppColors.GOLD, 3));
        panel.setPreferredSize(new Dimension(300, 200));

        JLabel label = new JLabel("LUXURY YACHT " + number, SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 20));
        label.setForeground(AppColors.GOLD);
        panel.add(label, BorderLayout.CENTER);

        JLabel subLabel = new JLabel("Premium Collection", SwingConstants.CENTER);
        subLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        subLabel.setForeground(AppColors.CREAM);
        panel.add(subLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
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

    private JButton createMenuButton(String title, String description, ActionListener action) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(10, 10));
        button.setBackground(AppColors.NAVY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(AppColors.GOLD);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descLabel.setForeground(AppColors.CREAM);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(AppColors.NAVY);
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(descLabel);

        button.add(textPanel, BorderLayout.CENTER);
        button.addActionListener(action);

        return button;
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

// ==================== CUSTOMER FRAME ====================
class CustomerFrame extends JFrame {
    private DataManager dataManager;
    private DashboardFrame dashboard;
    private JTable table;
    private DefaultTableModel tableModel;

    public CustomerFrame(DataManager dataManager, DashboardFrame dashboard) {
        this.dataManager = dataManager;
        this.dashboard = dashboard;
        initComponents();
        loadCustomers();
    }

    private void initComponents() {
        setTitle("Vela Prive - Customer Management");
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(AppColors.CREAM);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.NAVY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("CUSTOMER MANAGEMENT");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(AppColors.GOLD);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backBtn = new JButton("← Back");
        backBtn.setBackground(AppColors.GOLD);
        backBtn.setForeground(AppColors.NAVY);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> dispose());
        headerPanel.add(backBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(AppColors.CREAM);

        JButton addBtn = createButton("Add Customer", AppColors.NAVY);
        JButton editBtn = createButton("Edit Customer", AppColors.LIGHT_NAVY);
        JButton deleteBtn = createButton("Delete Customer", new Color(180, 50, 50));
        JButton refreshBtn = createButton("Refresh", AppColors.GOLD);

        addBtn.addActionListener(e -> addCustomer());
        editBtn.addActionListener(e -> editCustomer());
        deleteBtn.addActionListener(e -> deleteCustomer());
        refreshBtn.addActionListener(e -> loadCustomers());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table
        String[] columns = {"ID", "Name", "Phone", "Email", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(AppColors.NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(AppColors.GOLD);
        table.setSelectionForeground(AppColors.NAVY);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = dataManager.getCustomers();
        customers.sort(Comparator.comparing(Customer::getId));
        for (Customer customer : customers) {
            tableModel.addRow(new Object[]{
                    customer.getId(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getAddress()
            });
        }
    }

    private void addCustomer() {
        JDialog dialog = new JDialog(this, "Add New Customer", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextArea addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(addressArea), gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.setBackground(AppColors.NAVY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);

        cancelBtn.setBackground(Color.GRAY);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);

        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String address = addressArea.getText().trim();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                    throw new IllegalArgumentException("All fields must be filled!");
                }

                if (!email.contains("@")) {
                    throw new IllegalArgumentException("Invalid email format!");
                }

                String id = dataManager.generateCustomerId();
                Customer customer = new Customer(id, name, phone, email, address);
                dataManager.addCustomer(customer);
                loadCustomers();
                dashboard.refreshStats();
                JOptionPane.showMessageDialog(dialog, "Customer added successfully!");
                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void editCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Customer customer = dataManager.getCustomerById(id);

        JDialog dialog = new JDialog(this, "Edit Customer", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(customer.getName(), 20);
        JTextField phoneField = new JTextField(customer.getPhone(), 20);
        JTextField emailField = new JTextField(customer.getEmail(), 20);
        JTextArea addressArea = new JTextArea(customer.getAddress(), 3, 20);
        addressArea.setLineWrap(true);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(addressArea), gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.setBackground(AppColors.NAVY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);

        cancelBtn.setBackground(Color.GRAY);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);

        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String address = addressArea.getText().trim();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                    throw new IllegalArgumentException("All fields must be filled!");
                }

                if (!email.contains("@")) {
                    throw new IllegalArgumentException("Invalid email format!");
                }

                customer.setName(name);
                customer.setPhone(phone);
                customer.setEmail(email);
                customer.setAddress(address);
                dataManager.updateCustomer(customer);
                loadCustomers();
                JOptionPane.showMessageDialog(dialog, "Customer updated successfully!");
                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dataManager.deleteCustomer(id);
                loadCustomers();
                dashboard.refreshStats();
                JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting customer: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

// ==================== YACHT FRAME ====================
class YachtFrame extends JFrame {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;

    public YachtFrame(DataManager dataManager) {
        this.dataManager = dataManager;
        initComponents();
        loadYachts();
    }

    private void initComponents() {
        setTitle("Vela Prive - Yacht Fleet");
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(AppColors.CREAM);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.NAVY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("YACHT FLEET");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(AppColors.GOLD);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backBtn = new JButton("← Back");
        backBtn.setBackground(AppColors.GOLD);
        backBtn.setForeground(AppColors.NAVY);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> dispose());
        headerPanel.add(backBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Yacht Name", "Type", "Capacity", "Price/Day ($)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(AppColors.NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(AppColors.GOLD);
        table.setSelectionForeground(AppColors.NAVY);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadYachts() {
        tableModel.setRowCount(0);
        List<Yacht> yachts = dataManager.getYachts();
        yachts.sort(Comparator.comparing(Yacht::getId));
        for (Yacht yacht : yachts) {
            tableModel.addRow(new Object[]{
                    yacht.getId(),
                    yacht.getName(),
                    yacht.getType(),
                    yacht.getCapacity(),
                    String.format("%.2f", yacht.getPricePerDay()),
                    yacht.getStatus()
            });
        }
    }
}

// ==================== RENTAL FRAME ====================
class RentalFrame extends JFrame {
    private DataManager dataManager;
    private DashboardFrame dashboard;
    private JTable table;
    private DefaultTableModel tableModel;

    public RentalFrame(DataManager dataManager, DashboardFrame dashboard) {
        this.dataManager = dataManager;
        this.dashboard = dashboard;
        initComponents();
        loadRentals();
    }

    private void initComponents() {
        setTitle("Vela Prive - Rental Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(AppColors.CREAM);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.NAVY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("RENTAL MANAGEMENT");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(AppColors.GOLD);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton backBtn = new JButton("← Back");
        backBtn.setBackground(AppColors.GOLD);
        backBtn.setForeground(AppColors.NAVY);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> dispose());
        headerPanel.add(backBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(AppColors.CREAM);

        JButton addBtn = createButton("New Rental", AppColors.NAVY);
        JButton completeBtn = createButton("Complete Rental", new Color(50, 150, 50));
        JButton deleteBtn = createButton("Cancel Rental", new Color(180, 50, 50));
        JButton refreshBtn = createButton("Refresh", AppColors.GOLD);

        addBtn.addActionListener(e -> addRental());
        completeBtn.addActionListener(e -> completeRental());
        deleteBtn.addActionListener(e -> deleteRental());
        refreshBtn.addActionListener(e -> loadRentals());

        buttonPanel.add(addBtn);
        buttonPanel.add(completeBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table
        String[] columns = {"Rental ID", "Customer", "Yacht", "Start Date", "End Date", "Total ($)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(AppColors.NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(AppColors.GOLD);
        table.setSelectionForeground(AppColors.NAVY);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadRentals() {
        tableModel.setRowCount(0);
        List<Rental> rentals = dataManager.getRentals();
        rentals.sort(Comparator.comparing(Rental::getId).reversed());

        for (Rental rental : rentals) {
            Customer customer = dataManager.getCustomerById(rental.getCustomerId());
            Yacht yacht = dataManager.getYachtById(rental.getYachtId());

            tableModel.addRow(new Object[]{
                    rental.getId(),
                    customer != null ? customer.getName() : "Unknown",
                    yacht != null ? yacht.getName() : "Unknown",
                    rental.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    rental.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    String.format("%.2f", rental.getTotalPrice()),
                    rental.getStatus()
            });
        }
    }

    private void addRental() {
        JDialog dialog = new JDialog(this, "New Rental", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Customer selection
        JComboBox<String> customerCombo = new JComboBox<>();
        for (Customer c : dataManager.getCustomers()) {
            customerCombo.addItem(c.getId() + " - " + c.getName());
        }

        // Yacht selection
        JComboBox<String> yachtCombo = new JComboBox<>();
        for (Yacht y : dataManager.getYachts()) {
            if (y.getStatus().equals("Available")) {
                yachtCombo.addItem(y.getId() + " - " + y.getName() + " ($" + y.getPricePerDay() + "/day)");
            }
        }

        // Date fields
        JTextField startDateField = new JTextField(LocalDate.now().toString(), 15);
        JTextField endDateField = new JTextField(LocalDate.now().plusDays(3).toString(), 15);
        JLabel totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(AppColors.NAVY);

        // Calculate total on date change
        ActionListener calculateTotal = e -> {
            try {
                String yachtSelection = (String) yachtCombo.getSelectedItem();
                if (yachtSelection != null) {
                    String yachtId = yachtSelection.split(" - ")[0];
                    Yacht yacht = dataManager.getYachtById(yachtId);

                    LocalDate start = LocalDate.parse(startDateField.getText().trim());
                    LocalDate end = LocalDate.parse(endDateField.getText().trim());

                    long days = ChronoUnit.DAYS.between(start, end);
                    if (days <= 0) {
                        totalLabel.setText("Invalid dates!");
                        return;
                    }

                    double total = yacht.getPricePerDay() * days;
                    totalLabel.setText(String.format("Total: $%.2f (%d days)", total, days));
                }
            } catch (Exception ex) {
                totalLabel.setText("Invalid input!");
            }
        };

        yachtCombo.addActionListener(calculateTotal);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Customer:"), gbc);
        gbc.gridx = 1;
        panel.add(customerCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Yacht:"), gbc);
        gbc.gridx = 1;
        panel.add(yachtCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(startDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(endDateField, gbc);

        JButton calcBtn = new JButton("Calculate Total");
        calcBtn.setBackground(AppColors.GOLD);
        calcBtn.setForeground(AppColors.NAVY);
        calcBtn.setFocusPainted(false);
        calcBtn.addActionListener(calculateTotal);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(calcBtn, gbc);

        gbc.gridy = 5;
        panel.add(totalLabel, gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        JButton saveBtn = new JButton("Create Rental");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.setBackground(AppColors.NAVY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);

        cancelBtn.setBackground(Color.GRAY);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);

        saveBtn.addActionListener(e -> {
            try {
                if (customerCombo.getSelectedItem() == null || yachtCombo.getSelectedItem() == null) {
                    throw new IllegalArgumentException("Please select customer and yacht!");
                }

                String customerId = ((String) customerCombo.getSelectedItem()).split(" - ")[0];
                String yachtId = ((String) yachtCombo.getSelectedItem()).split(" - ")[0];

                LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
                LocalDate endDate = LocalDate.parse(endDateField.getText().trim());

                if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
                    throw new IllegalArgumentException("End date must be after start date!");
                }

                Yacht yacht = dataManager.getYachtById(yachtId);
                long days = ChronoUnit.DAYS.between(startDate, endDate);
                double totalPrice = yacht.getPricePerDay() * days;

                String rentalId = dataManager.generateRentalId();
                Rental rental = new Rental(rentalId, customerId, yachtId, startDate, endDate, totalPrice, "Active");

                yacht.setStatus("Rented");
                dataManager.saveYachts();
                dataManager.addRental(rental);

                loadRentals();
                dashboard.refreshStats();
                JOptionPane.showMessageDialog(dialog, "Rental created successfully!\nTotal: $" + String.format("%.2f", totalPrice));
                dialog.dispose();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        gbc.gridy = 6;
        panel.add(btnPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void completeRental() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a rental to complete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rentalId = (String) tableModel.getValueAt(selectedRow, 0);
        Rental rental = dataManager.getRentals().stream()
                .filter(r -> r.getId().equals(rentalId))
                .findFirst().orElse(null);

        if (rental != null && rental.getStatus().equals("Active")) {
            int confirm = JOptionPane.showConfirmDialog(this, "Complete this rental?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    rental.setStatus("Completed");
                    Yacht yacht = dataManager.getYachtById(rental.getYachtId());
                    yacht.setStatus("Available");
                    dataManager.saveYachts();
                    dataManager.updateRental(rental);
                    loadRentals();
                    dashboard.refreshStats();
                    JOptionPane.showMessageDialog(this, "Rental completed successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "This rental is not active!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteRental() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a rental to cancel!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rentalId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this rental?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Rental rental = dataManager.getRentals().stream()
                        .filter(r -> r.getId().equals(rentalId))
                        .findFirst().orElse(null);

                if (rental != null && rental.getStatus().equals("Active")) {
                    Yacht yacht = dataManager.getYachtById(rental.getYachtId());
                    yacht.setStatus("Available");
                    dataManager.saveYachts();
                }

                dataManager.deleteRental(rentalId);
                loadRentals();
                dashboard.refreshStats();
                JOptionPane.showMessageDialog(this, "Rental cancelled successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}