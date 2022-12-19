package de.info3.wegfinder24.newtwork.DDD;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.info3.wegfinder24.newtwork.DDD.FST.First;
import de.info3.wegfinder24.newtwork.DDD.FST.Second;
import de.info3.wegfinder24.newtwork.DDD.FST.Third;

public class Duration {

    @SerializedName("Hour")
    @Expose
    private String hour;
    @SerializedName("Minute")
    @Expose
    private String minute;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }
}
