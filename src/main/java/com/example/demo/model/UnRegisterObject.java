package com.example.demo.model;

public class UnRegisterObject {
    private String deviceID;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public String toString() {
        return "UnRegisterObject{" +
                "DeviceID='" + deviceID + '\'' +
                '}';
    }
}
