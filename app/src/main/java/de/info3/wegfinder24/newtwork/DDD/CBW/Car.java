package de.info3.wegfinder24.newtwork.DDD.CBW;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.info3.wegfinder24.newtwork.DDD.FST.First;
import de.info3.wegfinder24.newtwork.DDD.FST.Second;
import de.info3.wegfinder24.newtwork.DDD.FST.Third;

public class Car {

    @SerializedName("First")
    @Expose
    private First first;
    @SerializedName("Second")
    @Expose
    private Second second;
    @SerializedName("Third")
    @Expose
    private Third third;

    public First getFirst() {
        return first;
    }

    public void setFirst(First first) {
        this.first = first;
    }

    public Second getSecond() {
        return second;
    }

    public void setSecond(Second second) {
        this.second = second;
    }

    public Third getThird() {
        return third;
    }

    public void setThird(Third third) {
        this.third = third;
    }
}
