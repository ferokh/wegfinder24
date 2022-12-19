package de.info3.wegfinder24.newtwork.DDD;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Distance {
    @SerializedName("mkm")
    @Expose
    private String mkm;
    @SerializedName("MorKM")
    @Expose
    private String morKM;

    public String getMkm() {
        return mkm;
    }

    public void setMkm(String mkm) {
        this.mkm = mkm;
    }

    public String getMorKM() {
        return morKM;
    }

    public void setMorKM(String morKM) {
        this.morKM = morKM;
    }
}
