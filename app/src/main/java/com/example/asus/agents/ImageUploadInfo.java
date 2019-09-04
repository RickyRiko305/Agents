package com.example.asus.agents;

public class ImageUploadInfo {

    public String name;

    public String Image;

    public String lead;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String Image, String lead) {

        this.name = name;
        this.Image= Image;
        this.lead = lead;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return Image;
    }

    public String getLead(){
        return lead;
    }
}
