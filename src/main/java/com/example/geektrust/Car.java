package com.example.geektrust;

import java.time.Duration;
import java.time.LocalTime;

public class Car extends Vehicle {

    private final int regularFee = 120;
    private final int vipFee = 250;
    Lane lane;
    private LocalTime exitTime;
    public Car(String vehicleNumber, LocalTime entryTime, Lane lane) {
        super(vehicleNumber, entryTime);
        this.exitTime = getExitTimeFromEntryTime();
        this.lane = lane;
    }

    public LocalTime getExitTimeFromEntryTime() {
        return super.getEntryTime().plusHours(super.getMinimumBookTimeInHours());
    }

    public LocalTime getExitTime() { return exitTime; }

    private void setExitTime(LocalTime exitTime) { this.exitTime = exitTime; }

    public void extendExitTime(LocalTime exitTime) {
        setExitTime(exitTime);
    }

    public boolean isClashingWithNewVehicle(Car newCar) {
        if ((this.getEntryTime().isAfter(newCar.getEntryTime()) && this.getEntryTime().isBefore(newCar.getExitTime())) || (this.getExitTime().isAfter(newCar.getEntryTime()) && this.getExitTime().isBefore(newCar.getExitTime()))) {
            return true;
        }
        return false;
    }

    public boolean isExitTimeClashing(Car newCar) {
        if (newCar.lane.getId() == this.lane.getId() && newCar.getExitTime().isAfter(this.getEntryTime()) && newCar.getExitTime().isBefore(this.getExitTime())) {
            return true;
        }
        return false;
    }

    public int getRevenue() {
        int extraTimeInHours = getExtraHours();
        int revenue = getMinimumBookTimeRevenue();
        revenue += getAdditionalTimeRevenue(extraTimeInHours);
        return revenue;
    }

    public int getExtraHours() {
        int extraTimeInHours = 0;
        int totalTimeInMinutes = (int)Duration.between(this.getEntryTime(), this.getExitTime()).toMinutes();
        if (totalTimeInMinutes > super.getFreeExtraTimeInMinutes()) {
            int extraTimeInMinutes = totalTimeInMinutes - super.getMinimumBookTimeInHours() * super.getMinutesInOneHour();
            extraTimeInHours = (extraTimeInMinutes / super.getMinutesInOneHour()) + (extraTimeInMinutes % super.getMinutesInOneHour() == 0 ? 0 : 1);
        }
        return extraTimeInHours;
    }

    public int getMinimumBookTimeRevenue() {
        int revenue = 0;
        if (this.lane.getIsVip()) {
            revenue += super.getMinimumBookTimeInHours() * vipFee;
        } else {
            revenue += super.getMinimumBookTimeInHours() * regularFee;
        }
        return revenue;
    }

    public int getAdditionalTimeRevenue(int extraTimeInHours) {
        return extraTimeInHours * this.lane.getExtraTimeChargePerHour();
    }

}
