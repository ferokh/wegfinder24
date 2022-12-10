package de.info3.wegfinder24.newtwork;

import com.google.gson.annotations.SerializedName;



import de.info3.wegfinder24.newtwork.TestJSON.Geometry;
import de.info3.wegfinder24.newtwork.TestJSON.Summary;

public class Departures {
    @SerializedName("coordinates")
    private Geometry Coordinates;

    public Geometry getCoordinates() {
        return this.Coordinates;
    }

    public void setCoordinates(Geometry Coordinates) {
        this.Coordinates = Coordinates;
    }

    @SerializedName("DiDu")
    private Summary didu;

    public de.info3.wegfinder24.newtwork.TestJSON.Summary getdidu() {
        return didu;
    }

    public void setdidu(de.info3.wegfinder24.newtwork.TestJSON.Summary didu) {
        this.didu = didu;
    }

}
