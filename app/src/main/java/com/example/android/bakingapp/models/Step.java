package com.example.android.bakingapp.models;

import java.io.Serializable;

/**
 * Created by eslam on 27-Oct-17.
 */

public class Step  implements Serializable{
    private double id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public void setId(double id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setVideoURL(String videoURL)
    {
        this.videoURL=videoURL;
    }
    public void setThumbnailURL(String thumbnailURL)
    {
        this.thumbnailURL=thumbnailURL;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    public double getId()
    {
        return id;
    }
    public String getShortDescription()
    {
        return shortDescription;
    }
    public String getDescription()
    {
        return description;
    }
    public String getVideoURL()
    {
        return  videoURL;
    }
    public String getThumbnailURL()
    {
        return thumbnailURL;
    }
}
