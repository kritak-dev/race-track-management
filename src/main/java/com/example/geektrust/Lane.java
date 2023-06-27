package com.example.geektrust;

public class Lane {
    private int id;
    private boolean isVip;
    private final int extraTimeChargePerHour = 50;
    private final int freeExtendedTimeInMinutes = 15;

    public Lane(int id, boolean isVip) {
        this.id = id;
        this.isVip = isVip;
    }

    public int getId() {
        return id;
    }

    public boolean getIsVip() {
        return isVip;
    }

    public int getExtraTimeChargePerHour() { return extraTimeChargePerHour; }

    public int getFreeExtendedTimeInMinutes() { return freeExtendedTimeInMinutes; }

}
