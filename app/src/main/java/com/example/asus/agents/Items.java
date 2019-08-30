package com.example.asus.agents;

public class Items {
    private String mImageURL;
    private String mProductName;

    public Items(String image, String poductName){
        mImageURL = image;
        mProductName = poductName;
    }

    public String getmImageURL(){
        return mImageURL;
    }
    public String getmProductName(){
        return mProductName;
    }
}
