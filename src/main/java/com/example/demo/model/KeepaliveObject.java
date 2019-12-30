package com.example.demo.model;

public class KeepaliveObject {
    private String DeviceID;

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    @Override
    public String toString() {
        return "KeepaliveObject{" +
                "DeviceID='" + DeviceID + '\'' +
                '}';
    }
}
