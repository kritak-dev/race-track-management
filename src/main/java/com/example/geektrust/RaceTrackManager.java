package com.example.geektrust;

import com.sun.tools.javac.util.Pair;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RaceTrackManager {

    private CarTrack carTrack;
    private BikeTrack bikeTrack;
    private SuvTrack suvTrack;

    public RaceTrackManager() {
        this.carTrack = new CarTrack();
        this.bikeTrack = new BikeTrack();
        this.suvTrack = new SuvTrack();
    }

    public void performAction(String action) {
        String[] actionDetails = action.split(" ");
        switch (actionDetails[0]) {
            case "BOOK":
                bookNewTrack(actionDetails[1], actionDetails[2], actionDetails[3]);
                break;
            case "ADDITIONAL":
                addAdditionalTime(actionDetails[1], actionDetails[2]);
                break;
            case "REVENUE":
                getRevenue();
                break;
            default:
                invalidAction();
                break;
        }
    }

    public void bookNewTrack(String vehicleType, String vehicleNumber, String entryTime) {
        String message = "";
//        System.out.println(vehicleType);
        if (vehicleType.equals("BIKE")) {
            message = bikeTrack.bookNewBikeIfValidEntryTime(vehicleNumber, convertStringTimeToDate(entryTime));
        } else if (vehicleType.equals("CAR")) {
            message = carTrack.bookNewCarIfValidEntryTime(vehicleNumber, convertStringTimeToDate(entryTime));
        } else {
            message = suvTrack.bookNewCarIfValidEntryTime(vehicleNumber, convertStringTimeToDate(entryTime));
        }
        System.out.println(message);
    }

    public void addAdditionalTime(String vehicleNumber, String exitTime) {
        String message = "";
        if (bikeTrack.doesThisBikeExists(vehicleNumber)) {
            message = bikeTrack.additionalTime(vehicleNumber, convertStringTimeToDate(exitTime));
        } else if (carTrack.doesThisCarExists(vehicleNumber)) {
            message = carTrack.additionalTime(vehicleNumber, convertStringTimeToDate(exitTime));
        } else {
            message = suvTrack.additionalTime(vehicleNumber, convertStringTimeToDate(exitTime));
        }
        System.out.println(message);
    }

    public void getRevenue() {
        int vipRevenue = 0, regularRevenue = 0;
        Pair<Integer, Integer> carRevenue = carTrack.getTotalRevenue();
        Pair<Integer, Integer> bikeRevenue = bikeTrack.getTotalRevenue();
        Pair<Integer, Integer> suvRevenue = suvTrack.getTotalRevenue();
        regularRevenue += carRevenue.fst + bikeRevenue.fst + suvRevenue.fst;
        vipRevenue += carRevenue.snd + bikeRevenue.snd + suvRevenue.snd;
        System.out.println(regularRevenue + " " + vipRevenue);
    }

    public void invalidAction() {
        System.out.println("You have give invalid action");
    }

    public LocalTime convertStringTimeToDate(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time, formatter);
    }
}
