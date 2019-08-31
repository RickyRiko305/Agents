package com.example.asus.agents;

public class Items {
    private String mImageURL;
    private String mProductName;

    public Items(String image, String productName){
        mImageURL = image;
        mProductName = productName;
    }

    public String getmImageURL(){
        return mImageURL;
    }
    public String getmProductName(){
        return mProductName;
    }
}
