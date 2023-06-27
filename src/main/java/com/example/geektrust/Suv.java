package com.example.geektrust;

import java.time.Duration;
import java.time.LocalTime;

public class Suv extends Vehicle {
    private final int regularFee = 200;
    private final int vipFee = 300;
    private LocalTime exitTime;
    Lane lane;
    public Suv(String vehicleNumber, LocalTime entryTime, Lane lane) {
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

    public boolean isClashingWithNewVehicle(Suv newSuv) {
        if ((this.getEntryTime().isAfter(newSuv.getEntryTime()) && this.getEntryTime().isBefore(newSuv.getExitTime())) || (this.getExitTime().isAfter(newSuv.getEntryTime()) && this.getExitTime().isBefore(newSuv.getExitTime()))) {
            return true;
        }
        return false;
    }

    public boolean isExitTimeClashing(Suv newSuv) {
        if (newSuv.lane.getId() == this.lane.getId() && newSuv.getExitTime().isAfter(this.getEntryTime()) && newSuv.getExitTime().isBefore(this.getExitTime())) {
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
        if (this.lane.getIsVip()) {
            revenue += super.getMinimumBookTimeInHours() * vipFee;
        } else {
            revenue += super.getMinimumBookTimeInHours() * regularFee;
        }

        revenue += extraTimeInHours * this.lane.getExtraTimeChargePerHour();
        return revenue;
    }
}
