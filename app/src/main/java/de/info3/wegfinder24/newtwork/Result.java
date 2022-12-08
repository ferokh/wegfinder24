package de.info3.wegfinder24.newtwork;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("features")
    private List<Features> featuresList;
    //private String<Features> featuresString;

    public List<Features> getfeaturesList() {return featuresList;}
    //public String<Features> getfeaturesString() {return featuresString;}

    public void setfeaturesList(List<Features> featuresList) {this.featuresList = featuresList;}
    //public void setfeaturesString(String<Features> featuresString) {this.featuresString = featuresString;}
}

//https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248f06b7f011fe047c9b80f787320e4eada&start=8.681495,49.41461&end=8.687872,49.420318

