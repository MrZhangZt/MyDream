package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RegisterObject {
   private String deviceID;

   @JsonProperty(value = "deviceID")
    public String getDeviceID() {
        return deviceID;
    }
    @JsonProperty(value = "deviceID")
    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public String toString() {
        return "RegisterObject{" +
                "DeviceID='" + deviceID + '\'' +
                '}';
    }
}
