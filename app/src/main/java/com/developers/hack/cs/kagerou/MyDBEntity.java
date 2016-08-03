package com.developers.hack.cs.kagerou;

/**
 * Created by Wataru on 16/08/02.
 */
public class MyDBEntity {
    private int rowId;

    private double latValue;
    private double lngValue;
    private int radiusValue;
    private String iColorValue;
    private String oColorValue;

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getRowId() {
        return rowId;
    }

    public void setLatValue(double value) {
        this.latValue = value;
    }

    public double getLatValue() {
        return latValue;
    }
    public void setLngValue(double value) {
        this.lngValue = value;
    }

    public double getLngValue() {
        return lngValue;
    }
    public void setRadiusValue(int value) {
        this.radiusValue = value;
    }

    public int getRadiusValue() {
        return radiusValue;
    }
    public void setIColorValue(String value) {
        this.iColorValue = value;
    }

    public String getIColorValue() {
        return iColorValue;
    }
    public void setOColorValue(String value) {
        this.oColorValue = value;
    }

    public String getOColorValue() {
        return oColorValue;
    }
}
