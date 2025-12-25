package com.praktikum.Model;

import java.time.LocalDate;

public class Rental {
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
