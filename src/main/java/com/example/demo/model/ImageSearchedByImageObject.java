package com.example.demo.model;

import java.util.ArrayList;

public class ImageSearchedByImageObject {
    private Integer RecordStartNo;
    private Integer PageRecordNum;
    private String SearchID;
    private String SearchType;
    private String ResultImageDeclare;
    private String ResultFeatureDeclare;
    private ArrayList<Image> image;
    private String QueryString;
    private Integer MaxNumRecordReturn;
    private double Threshold;

    public Integer getRecordStartNo() {
        return RecordStartNo;
    }

    public void setRecordStartNo(Integer recordStartNo) {
        RecordStartNo = recordStartNo;
    }

    public Integer getPageRecordNum() {
        return PageRecordNum;
    }

    public void setPageRecordNum(Integer pageRecordNum) {
        PageRecordNum = pageRecordNum;
    }

    public String getSearchID() {
        return SearchID;
    }

    public void setSearchID(String searchID) {
        SearchID = searchID;
    }

    public String getSearchType() {
        return SearchType;
    }

    public void setSearchType(String searchType) {
        SearchType = searchType;
    }

    public String getResultImageDeclare() {
        return ResultImageDeclare;
    }

    public void setResultImageDeclare(String resultImageDeclare) {
        ResultImageDeclare = resultImageDeclare;
    }

    public String getResultFeatureDeclare() {
        return ResultFeatureDeclare;
    }

    public void setResultFeatureDeclare(String resultFeatureDeclare) {
        ResultFeatureDeclare = resultFeatureDeclare;
    }


    public String getQueryString() {
        return QueryString;
    }

    public void setQueryString(String queryString) {
        QueryString = queryString;
    }

    public Integer getMaxNumRecordReturn() {
        return MaxNumRecordReturn;
    }

    public void setMaxNumRecordReturn(Integer maxNumRecordReturn) {
        MaxNumRecordReturn = maxNumRecordReturn;
    }

    public double getThreshold() {
        return Threshold;
    }

    public void setThreshold(double threshold) {
        Threshold = threshold;
    }

    public ArrayList<Image> getImage() {
        return image;
    }

    public void setImage(ArrayList<Image> image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ImageSearchedByImageObject{" +
                "RecordStartNo=" + RecordStartNo +
                ", PageRecordNum=" + PageRecordNum +
                ", SearchID='" + SearchID + '\'' +
                ", SearchType='" + SearchType + '\'' +
                ", ResultImageDeclare='" + ResultImageDeclare + '\'' +
                ", ResultFeatureDeclare='" + ResultFeatureDeclare + '\'' +
                ", image=" + image +
                ", QueryString='" + QueryString + '\'' +
                ", MaxNumRecordReturn=" + MaxNumRecordReturn +
                ", Threshold=" + Threshold +
                '}';
    }
}
