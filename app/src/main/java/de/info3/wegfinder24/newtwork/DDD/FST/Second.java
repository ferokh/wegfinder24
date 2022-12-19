package de.info3.wegfinder24.newtwork.DDD.FST;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.info3.wegfinder24.newtwork.DDD.Distance;
import de.info3.wegfinder24.newtwork.DDD.Duration;

public class Second {

    @SerializedName("Duration")
    @Expose
    private Duration duration;
    @SerializedName("Distance")
    @Expose
    private Distance distance;

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
}
