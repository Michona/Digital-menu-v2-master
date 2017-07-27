package com.example.terz99.digitalmenuv2;



public class OrderItem {

    private String mName;
    private double mPrice;
    private int mQuantity;
    private int mImageId;

    public OrderItem(String mName, double mPrice, int mQuantity, int mImageId) {
        this.mName = mName;
        this.mPrice = mPrice;
        this.mQuantity = mQuantity;
        this.mImageId = mImageId;
    }

    public String getmName() {
        return mName;
    }

    public double getmPrice() {
        return mPrice;
    }

    public int getmQuantity() {
        return mQuantity;
    }

    public int getmImageId() {
        return mImageId;
    }
}
