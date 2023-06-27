package com.example.geektrust;

import java.time.Duration;
import java.time.LocalTime;

public class Bike extends Vehicle {
    private final int regularFee = 60;
    private LocalTime exitTime;
    Lane lane;
    public Bike(String vehicleNumber, LocalTime entryTime, Lane lane) {
        super(vehicleNumber, entryTime);
        this.exitTime = getExitTimeFromEntryTime();
        this.lane = lane;
    }

    public LocalTime getExitTimeFromEntryTime() {
        return super.getEntryTime().plusHours(super.getMinimumBookTimeInHours());
    }

    public LocalTime getExitTime() {
        return exitTime;
    }
    private void setExitTime(LocalTime exitTime) {
        this.exitTime = exitTime;
    }

    public void extendExitTime(LocalTime exitTime) {
        setExitTime(exitTime);
    }

    public boolean isClashingWithNewVehicle(Bike newBike) {
        if ((this.getEntryTime().isAfter(newBike.getEntryTime()) && this.getEntryTime().isBefore(newBike.getExitTime())) || (this.getExitTime().isAfter(newBike.getEntryTime()) && this.getExitTime().isBefore(newBike.getExitTime()))) {
            return true;
        }
        return false;
    }

    public boolean isExitTimeClashing(Bike newBike) {
        if (newBike.lane.getId() == this.lane.getId() && newBike.getExitTime().isAfter(this.getEntryTime()) && newBike.getExitTime().isBefore(this.getExitTime())) {
            return true;
        }
        return false;
    }

    public int getRevenue() {
        int revenue = 0, extraTimeInHours = 0;
        int totalTimeInMinutes = (int) Duration.between(this.getEntryTime(), this.getExitTime()).toMinutes();
        if (totalTimeInMinutes > 195) {
            int extraTimeInMinutes = totalTimeInMinutes - super.getMinimumBookTimeInHours() * super.getMinutesInOneHour();
            extraTimeInHours = (extraTimeInMinutes / super.getMinutesInOneHour()) + (extraTimeInMinutes % super.getMinutesInOneHour() == 0 ? 0 : 1);
        }

        revenue += super.getMinimumBookTimeInHours() * regularFee;
        revenue += extraTimeInHours * this.lane.getExtraTimeChargePerHour();
        return revenue;
    }
}
