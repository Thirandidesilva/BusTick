package com.example.proj;

public class Seat {
    private int busNumber;
    private int seatNumber;
    private String status;

    // Constructor
    public Seat(int busNumber, int seatNumber, String status) {
        this.busNumber = busNumber;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    // Getters and Setters

    public int getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
