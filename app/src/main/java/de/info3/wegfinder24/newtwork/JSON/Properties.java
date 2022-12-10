package de.info3.wegfinder24.newtwork.JSON;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Properties {

    @SerializedName("segments")
    @Expose
    private List<Segment> segments = null;
    @SerializedName("way_points")
    @Expose
    private List<Integer> wayPoints = null;
    @SerializedName("summary")
    @Expose
    private Summary summary;

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public List<Integer> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<Integer> wayPoints) {
        this.wayPoints = wayPoints;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

}