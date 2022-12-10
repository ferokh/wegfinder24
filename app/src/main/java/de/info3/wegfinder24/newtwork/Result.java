package de.info3.wegfinder24.newtwork;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Result {
    private List<Departures> departureList;

    @SerializedName("departureList")

    public List<Departures> getDepartureList() {
        return departureList;
    }

    public void setDepartureList(List<Departures> departureList) {
        this.departureList = departureList;
    }
    /*private List<Features> featuresList;
    public List<Features> getfeaturesList() {return featuresList;}
    public void setfeaturesList(List<Features> featuresList) {this.featuresList = featuresList;}
    */

    //public List<Features> features = null;
    //public List<Features> getFeatures() {return features;}
    //public void setFeatures(List<Features> features) {this.features = features;}

}

//https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248f06b7f011fe047c9b80f787320e4eada&start=8.681495,49.41461&end=8.687872,49.420318

