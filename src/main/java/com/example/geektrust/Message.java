package com.example.geektrust;

public enum Message {
    SUCCESS("SUCCESS"),
    RACETRACK_FULL("RACETRACK_FULL"),
    INVALID_ENTRY_TIME("INVALID_ENTRY_TIME"),
    INVALID_EXIT_TIME("INVALID_EXIT_TIME");

    private final String message;

    private Message(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }

}
