package de.info3.wegfinder24.newtwork.JSON_Anfrage;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AlternativeRoutes {

    @SerializedName("targetCount")
    @Expose
    private Integer targetCount;

    public Integer getTargetCount() {return targetCount;}
    public void setTargetCount(Integer targetCount) {this.targetCount = targetCount;}

}
