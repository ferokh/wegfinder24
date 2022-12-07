package de.info3.wegfinder24.newtwork.DiDu;

import com.google.gson.annotations.SerializedName;

public class F_N_P_Summary {
    @SerializedName("distance")
    private String distance;

    @SerializedName("duration")
    private String duration;

    public String getdistance() {
        return this.distance;
    }

    public void setdistance(String distance) {
        this.distance = distance;
    }

    public String getduration() {
        return this.duration;
    }

    public void setduration(String duration) {
        this.duration = duration;
    }
}
