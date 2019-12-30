package com.example.demo.model;

public class Image {
    private String type;
    private String FileFormat;
    private String ImageID;
    private String Data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileFormat() {
        return FileFormat;
    }

    public void setFileFormat(String fileFormat) {
        FileFormat = fileFormat;
    }

    public String getImageID() {
        return ImageID;
    }


    public void setImageID(String imageID) {
        ImageID = imageID;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "Image{" +
                "type='" + type + '\'' +
                ", FileFormat='" + FileFormat + '\'' +
                ", ImageID='" + ImageID + '\'' +
                ", Data='" + Data + '\'' +
                '}';
    }
}
