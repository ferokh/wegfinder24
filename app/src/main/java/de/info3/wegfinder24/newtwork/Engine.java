package de.info3.wegfinder24.newtwork;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Engine {

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("build_date")
    @Expose
    private String buildDate;
    @SerializedName("graph_date")
    @Expose
    private String graphDate;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getGraphDate() {
        return graphDate;
    }

    public void setGraphDate(String graphDate) {
        this.graphDate = graphDate;
    }

}