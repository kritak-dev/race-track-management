package com.example.geektrust;

import com.sun.tools.javac.util.Pair;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class CarTrack {
    private final int totalRegularTracks = 2;
    private final int totalVipTracks = 1;
    private final HashMap<String, Car> cars;

    public CarTrack() {
        this.cars = new HashMap<>();
    }

    public String bookNewCarIfValidEntryTime(String vehicleNumber, LocalTime entryTime) {
        if (isEntryTimeInvalid(entryTime)) {
            return Message.INVALID_ENTRY_TIME.getMessage();
        }

        return bookNewCar(vehicleNumber, entryTime);
    }

    public boolean isEntryTimeInvalid(LocalTime entryTime) {
        return entryTime.isAfter(LocalTime.parse("17:00:00")) || entryTime.isBefore(LocalTime.parse("13:00:00"));
    }

    public String bookNewCar(String vehicleNumber, LocalTime entryTime) {
        for (int trackId = 1; trackId <= totalRegularTracks; trackId++) {
            if (addCarIfTrackFree(vehicleNumber, entryTime, trackId, false)) {
                return Message.SUCCESS.getMessage();
            }
        }

        for (int trackId = 1; trackId <= totalVipTracks; trackId++) {
            if (addCarIfTrackFree(vehicleNumber, entryTime, trackId, true)) {
                return Message.SUCCESS.getMessage();
            }
        }

        return Message.RACETRACK_FULL.getMessage();
    }

    public boolean addCarIfTrackFree(String vehicleNumber, LocalTime entryTime, int trackId, boolean isVip) {
        Car tempCar = new Car(vehicleNumber, entryTime, new Lane(trackId, isVip));
        boolean isTrackFree = true;
        for (Map.Entry<String, Car> car : cars.entrySet()) {
            if (car.getValue().lane.getIsVip() == isVip && car.getValue().lane.getId() == trackId && car.getValue().isClashingWithNewVehicle(tempCar)) {
                isTrackFree = false;
                break;
            }
        }
        if (isTrackFree) {
            cars.put(vehicleNumber, tempCar);
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
        Car car = cars.get(vehicleNumber);
        if(isTrackFree(car)) {
            car.extendExitTime(exitTime);
            return Message.SUCCESS.getMessage();
        }
        return Message.RACETRACK_FULL.getMessage();
    }

    private boolean isTrackFree(Car car) {
        boolean isTrackFree = true;
        for (Map.Entry<String, Car> carEntry: cars.entrySet()) {
            Car currentCar = carEntry.getValue();
            if (!carEntry.getKey().equals(car.getVehicleNumber()) && currentCar.lane.getId() ==  car.lane.getId() && currentCar.isExitTimeClashing(car)) {
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
        for(Map.Entry<String, Car> carEntry: cars.entrySet()) {
            Car car = carEntry.getValue();
            if (car.lane.getIsVip()) {
                vipRevenue += car.getRevenue();
            }
        }
        return vipRevenue;
    }

    public int getRegularRevenue() {
        int regularRevenue = 0;
        for(Map.Entry<String, Car> carEntry: cars.entrySet()) {
            Car car = carEntry.getValue();
            if (!car.lane.getIsVip()) {
                regularRevenue += car.getRevenue();
            }
        }
        return regularRevenue;
    }

    public boolean doesThisCarExists(String vehicleNumber) {
        return cars.containsKey(vehicleNumber);
    }
}
