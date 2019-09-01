package com.example.asus.agents;

public class Items {
    private String Image;
    private String name;

    public Items(){}

    public Items(String Image, String name){
        this.Image = Image;
        this.name = name;
    }

    public String getImage(){
        return Image;
    }
    public String getName(){
        return name;
    }
}
