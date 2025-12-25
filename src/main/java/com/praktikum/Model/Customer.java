package com.praktikum.Model;

public class Customer {
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

