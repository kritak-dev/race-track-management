package com.example.geektrust;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class Vehicle {

    private final int minimumBookTimeInHours = 3;
    private final int minutesInOneHour = 60;
    private final int freeExtraTimeInMinutes = 195;
    private String vehicleNumber;
    private LocalTime entryTime;

    public Vehicle(String vehicleNumber, LocalTime entryTime) {
        this.vehicleNumber = vehicleNumber;
        this.entryTime = entryTime;
    }

    public String getVehicleNumber() { return vehicleNumber; }

    public LocalTime getEntryTime() { return entryTime; }

    public int getMinimumBookTimeInHours() { return minimumBookTimeInHours; }

    public int getFreeExtraTimeInMinutes() { return freeExtraTimeInMinutes; }

    public int getMinutesInOneHour() { return minutesInOneHour; }

    public abstract int getRevenue();

}
