package com.praktikum.GUI;

import com.praktikum.Model.*;
import com.praktikum.utils.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

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
        setTitle("Vela Privé - Rental Management");
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

        addBtn.addActionListener(e -> addRental());
        completeBtn.addActionListener(e -> completeRental());
        deleteBtn.addActionListener(e -> deleteRental());

        buttonPanel.add(addBtn);
        buttonPanel.add(completeBtn);
        buttonPanel.add(deleteBtn);

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
