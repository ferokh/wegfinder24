package de.info3.wegfinder24.newtwork.JSON_Anfrage;


import java.util.ArrayList;
import java.util.List;


import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Anfrage {
    @SerializedName("coordinates")
    @Expose
    private List<List<Double>> coordinates = null;
    @SerializedName("alternativeRoutes")
    @Expose
    private AlternativeRoutes alternativeRoutes;
    @SerializedName("language")
    @Expose
    private String language;



    public Anfrage(List coordinates, int i, String de) {
    }

    public List<List<Double>> getCoordinates() {return coordinates;}
    public void setCoordinates(List<List<Double>> coordinates) {this.coordinates = coordinates;}

    public AlternativeRoutes getAlternativeRoutes() {return alternativeRoutes;}
    public void setAlternativeRoutes(AlternativeRoutes alternativeRoutes) {this.alternativeRoutes = alternativeRoutes;}

    public String getLanguage() {return language;}
    public void setLanguage(String language) {this.language = language;}

}
