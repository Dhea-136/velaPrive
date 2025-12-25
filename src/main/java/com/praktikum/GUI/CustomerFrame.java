package com.praktikum.GUI;

import com.praktikum.Model.*;
import com.praktikum.utils.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class CustomerFrame extends JFrame {
    DataManager dataManager;
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
        setTitle("Vela Privé - Customer Management");
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

        addBtn.addActionListener(e -> addCustomer());
        editBtn.addActionListener(e -> editCustomer());
        deleteBtn.addActionListener(e -> deleteCustomer());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

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