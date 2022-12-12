package de.info3.wegfinder24.newtwork.JSON_Anfrage;


import java.util.List;


//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.info3.wegfinder24.newtwork.Feature;
import de.info3.wegfinder24.newtwork.Metadata;

//@Generated("jsonschema2pojo")
public class Anfrage {
    @SerializedName("coordinates")
    @Expose
    private List<List<Double>> coordinates = null;
    @SerializedName("alternative_routes")
    @Expose
    private AlternativeRoutes alternativeRoutes;
    @SerializedName("language")
    @Expose
    private String language;
/////////////////////////////////
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("features")
    @Expose
    private List<Feature> features = null;
    @SerializedName("bbox")
    @Expose
    private List<Double> bbox = null;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
/////////////////////////////////
    public Anfrage(List coordinates, int i, String de) {
        this.coordinates=coordinates;
        this.alternativeRoutes=new AlternativeRoutes(i);
        this.language=de;
    }

    public List<List<Double>> getCoordinates() {return coordinates;}
    public void setCoordinates(List<List<Double>> coordinates) {this.coordinates = coordinates;}

    public AlternativeRoutes getAlternativeRoutes() {return alternativeRoutes;}
    public void setAlternativeRoutes(AlternativeRoutes alternativeRoutes) {this.alternativeRoutes = alternativeRoutes;}

    public String getLanguage() {return language;}
    public void setLanguage(String language) {this.language = language;}

}
