package de.info3.wegfinder24.newtwork.JSON_Anfrage;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class AlternativeRoutes implements Serializable {

    @SerializedName("share_factor")
    @Expose
    private Double shareFactor;
    @SerializedName("target_count")
    @Expose
    private Integer targetCount;
    @SerializedName("weight_factor")
    @Expose
    private Double weightFactor;

    public AlternativeRoutes(Double shareFactor,Integer targetCount, Double weightFactor)
    {
        this.shareFactor = shareFactor;
        this.targetCount = targetCount;
        this.weightFactor = weightFactor;
    }
    //public AlternativeRoutes(Integer targetCount) {this.targetCount = targetCount;}
    //public AlternativeRoutes(Integer weightFactor) {this.weightFactor = weightFactor;}

    public Double getShareFactor() {return shareFactor;}
    public void setShareFactor(Double shareFactor) {this.shareFactor = shareFactor;}

    public Integer getTargetCount() {return targetCount;}
    public void setTargetCount(Integer targetCount) {this.targetCount = targetCount;}

    public Double getWeightFactor() {return weightFactor;}
    public void setWeightFactor(Double weightFactor) {this.weightFactor = weightFactor;}

}
