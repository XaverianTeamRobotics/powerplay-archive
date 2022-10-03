package org.firstinspires.ftc.teamcode.utils;

public class ParkingManager {
    private static int parkingPosition = 1;

    public static void setParkingPosition(int position) {
        parkingPosition = position;
    }

    public static int getParkingPosition() {
        return parkingPosition;
    }
}
