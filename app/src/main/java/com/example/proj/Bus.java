package com.example.proj;

public class Bus {
    private int busId;
    private String busNumber;
    private String startLocation;
    private String endLocation;
    private String route;
    private String driver;
    private int seats;

    // Constructor
    public Bus(int busId, String busNumber, String startLocation, String endLocation, String route, String driver, int seats) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.route = route;
        this.driver = driver;
        this.seats = seats;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
