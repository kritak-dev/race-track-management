package com.example.geektrust;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(args[0]);
            Scanner sc = new Scanner(fis);
            RaceTrackManager raceTrackManager = new RaceTrackManager();
            while (sc.hasNextLine()) {
                String action = sc.nextLine();
                raceTrackManager.performAction(action);
            }
            sc.close(); // closes the scanner
        } catch (IOException e) {
        }
    }
}
