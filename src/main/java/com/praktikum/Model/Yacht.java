package com.praktikum.Model;

public class Yacht {
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
