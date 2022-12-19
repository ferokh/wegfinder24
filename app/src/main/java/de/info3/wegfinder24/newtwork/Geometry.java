package de.info3.wegfinder24.newtwork;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.osmdroid.util.GeoPoint;

@Generated("jsonschema2pojo")
public class Geometry {

    @SerializedName("coordinates")
    @Expose
    private List<List<Double>> coordinates = null;
    @SerializedName("type")
    @Expose
    private String type;

    public List<List<GeoPoint>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}