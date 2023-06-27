package com.example.geektrust;

import com.sun.tools.javac.util.Pair;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class BikeTrack {
    private final int totalRegularTracks = 4;
    private final int totalVipTracks = 0;
    private final HashMap<String, Bike> bikes;

    public BikeTrack() {
        this.bikes = new HashMap<>();
    }

    public String bookNewBikeIfValidEntryTime(String vehicleNumber, LocalTime entryTime) {
        if (isEntryTimeInvalid(entryTime)) {
            return Message.INVALID_ENTRY_TIME.getMessage();
        }

        return bookNewBike(vehicleNumber, entryTime);
    }

    public boolean isEntryTimeInvalid(LocalTime entryTime) {
        return entryTime.isAfter(LocalTime.parse("17:00:00")) || entryTime.isBefore(LocalTime.parse("13:00:00"));
    }

    public String bookNewBike(String vehicleNumber, LocalTime entryTime) {
        for (int trackId = 1; trackId <= totalRegularTracks; trackId++) {
            if (addBikeIfTrackFree(vehicleNumber, entryTime, trackId, false)) {
                return Message.SUCCESS.getMessage();
            }
        }

        for (int trackId = 1; trackId <= totalVipTracks; trackId++) {
            if (addBikeIfTrackFree(vehicleNumber, entryTime, trackId, true)) {
                return Message.SUCCESS.getMessage();
            }
        }

        return Message.RACETRACK_FULL.getMessage();
    }

    public boolean addBikeIfTrackFree(String vehicleNumber, LocalTime entryTime, int trackId, boolean isVip) {
        Bike tempBike = new Bike(vehicleNumber, entryTime, new Lane(trackId, isVip));
        boolean isTrackFree = true;
        for (Map.Entry<String, Bike> bikeEntry : bikes.entrySet()) {
            if (bikeEntry.getValue().lane.getIsVip() == isVip && bikeEntry.getValue().lane.getId() == trackId && bikeEntry.getValue().isClashingWithNewVehicle(tempBike)) {
                isTrackFree = false;
                break;
            }
        }
        if (isTrackFree) {
            bikes.put(vehicleNumber, tempBike);
            return true;
        }
        return false;
    }

    public String additionalTime(String vehicleNumber, LocalTime exitTime) {
        if (isExitTimeInvalid(exitTime)) {
            return Message.INVALID_EXIT_TIME.getMessage();
        }
        return extendTime(vehicleNumber, exitTime);
    }

    public boolean isExitTimeInvalid(LocalTime exitTime) {
        return exitTime.isAfter(LocalTime.parse("20:00:00"));
    }

    public String extendTime(String vehicleNumber, LocalTime exitTime) {
        Bike bike = bikes.get(vehicleNumber);
        if (isTrackFree(bike)) {
            bike.extendExitTime(exitTime);
            return Message.SUCCESS.getMessage();
        }
        return Message.RACETRACK_FULL.getMessage();
    }

    private boolean isTrackFree(Bike bike) {
        boolean isTrackFree = true;
        for (Map.Entry<String, Bike> bikeEntry: bikes.entrySet()) {
            Bike currentCar = bikeEntry.getValue();
            if (!bikeEntry.getKey().equals(bike.getVehicleNumber()) && currentCar.lane.getId() ==  bike.lane.getId() && currentCar.isExitTimeClashing(bike)) {
                isTrackFree = false;
                break;
            }
        }
        return isTrackFree;
    }

    public Pair<Integer, Integer> getTotalRevenue() {
        int regularRevenue = getRegularRevenue();
        int vipRevenue = getVipRevenue();
        return new Pair<>(regularRevenue, vipRevenue);
    }

    public int getVipRevenue() {
        int vipRevenue = 0;
        for(Map.Entry<String, Bike> bikeEntry: bikes.entrySet()) {
            Bike bike = bikeEntry.getValue();
            if (bike.lane.getIsVip()) {
                vipRevenue += bike.getRevenue();
            }
        }
        return vipRevenue;
    }

    public int getRegularRevenue() {
        int regularRevenue = 0;
        for(Map.Entry<String, Bike> bikeEntry: bikes.entrySet()) {
            Bike bike = bikeEntry.getValue();
            if (!bike.lane.getIsVip()) {
                regularRevenue += bike.getRevenue();
            }
        }
        return regularRevenue;
    }

    public boolean doesThisBikeExists(String vehicleNumber) {
        return bikes.containsKey(vehicleNumber);
    }
}
