package com.example.geektrust;

import com.sun.tools.javac.util.Pair;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class SuvTrack {
    private final int totalRegularTracks = 2;
    private final int totalVipTracks = 1;
    private final HashMap<String, Suv> suvs;

    public SuvTrack() {
        this.suvs = new HashMap<>();
    }

    public String bookNewCarIfValidEntryTime(String vehicleNumber, LocalTime entryTime) {
        if (isEntryTimeInvalid(entryTime)) {
            return Message.INVALID_ENTRY_TIME.getMessage();
        }

        return bookNewSuv(vehicleNumber, entryTime);
    }

    public boolean isEntryTimeInvalid(LocalTime entryTime) {
        return entryTime.isAfter(LocalTime.parse("17:00:00")) || entryTime.isBefore(LocalTime.parse("13:00:00"));
    }

    public String bookNewSuv(String vehicleNumber, LocalTime entryTime) {
        for (int trackId = 1; trackId <= totalRegularTracks; trackId++) {
            if (addSuvIfTrackFree(vehicleNumber, entryTime, trackId, false)) {
                return Message.SUCCESS.getMessage();
            }
        }

        for (int trackId = 1; trackId <= totalVipTracks; trackId++) {
            if (addSuvIfTrackFree(vehicleNumber, entryTime, trackId, true)) {
                return Message.SUCCESS.getMessage();
            }
        }

        return Message.RACETRACK_FULL.getMessage();
    }

    public boolean addSuvIfTrackFree(String vehicleNumber, LocalTime entryTime, int trackId, boolean isVip) {
        Suv tempSuv = new Suv(vehicleNumber, entryTime, new Lane(trackId, isVip));
        boolean isTrackFree = true;
        for (Map.Entry<String, Suv> suvEntry : suvs.entrySet()) {
            if (suvEntry.getValue().lane.getIsVip() == isVip && suvEntry.getValue().lane.getId() == trackId && suvEntry.getValue().isClashingWithNewVehicle(tempSuv)) {
                isTrackFree = false;
                break;
            }
        }
        if (isTrackFree) {
            suvs.put(vehicleNumber, tempSuv);
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
        Suv suv = suvs.get(vehicleNumber);
        if (isTrackFree(suv)) {
            suv.extendExitTime(exitTime);
            return Message.SUCCESS.getMessage();
        }
        return Message.RACETRACK_FULL.getMessage();
    }

    private boolean isTrackFree(Suv suv) {
        boolean isTrackFree = true;
        for (Map.Entry<String, Suv> suvEntry: suvs.entrySet()) {
            Suv currentSuv = suvEntry.getValue();
            if (!suvEntry.getKey().equals(suv.getVehicleNumber()) && currentSuv.lane.getId() ==  suv.lane.getId() && currentSuv.isExitTimeClashing(suv)) {
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
        for(Map.Entry<String, Suv> suvEntry: suvs.entrySet()) {
            Suv suv = suvEntry.getValue();
            if (suv.lane.getIsVip()) {
                vipRevenue += suv.getRevenue();
            }
        }
        return vipRevenue;
    }

    public int getRegularRevenue() {
        int regularRevenue = 0;
        for(Map.Entry<String, Suv> suvEntry: suvs.entrySet()) {
            Suv suv = suvEntry.getValue();
            if (!suv.lane.getIsVip()) {
                regularRevenue += suv.getRevenue();
            }
        }
        return regularRevenue;
    }

    public boolean doesThisSuvExists(String vehicleNumber) {
        return suvs.containsKey(vehicleNumber);
    }
}
