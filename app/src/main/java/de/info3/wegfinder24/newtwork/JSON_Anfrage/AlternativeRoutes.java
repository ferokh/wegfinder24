package de.info3.wegfinder24.newtwork.JSON_Anfrage;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class AlternativeRoutes implements Serializable {

    @SerializedName("target_count")
    @Expose
    private Integer targetCount;

    public AlternativeRoutes(Integer targetCount) {
        this.targetCount = targetCount;
    }

    public Integer getTargetCount() {return targetCount;}
    public void setTargetCount(Integer targetCount) {this.targetCount = targetCount;}

}
