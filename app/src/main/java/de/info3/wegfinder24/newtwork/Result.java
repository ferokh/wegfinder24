package de.info3.wegfinder24.newtwork;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    private List<Features> featuresList;
    @SerializedName("feature")

    public List<Features> getfeaturesList() {
        return featuresList;
    }

    public void setfeaturesList(List<Features> featuresList) {
        this.featuresList = featuresList;
    }
}
