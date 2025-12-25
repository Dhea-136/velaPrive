package com.praktikum.utils;

import com.praktikum.Model.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
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
                yachts.add(new Yacht("Y001", "Azure Dream", "Motor Yacht", 1, 5000, "Available"));
                yachts.add(new Yacht("Y002", "Blue Breeze", "Sailing Yacht", 1, 3500, "Available"));
                yachts.add(new Yacht("Y003", "Ocean Pearl", "Catamaran", 1, 6000, "Available"));
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
