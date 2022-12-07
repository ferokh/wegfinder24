package de.info3.wegfinder24.newtwork;

import com.google.gson.annotations.SerializedName;

// Entspricht der Departure.java in der Übung

public class Features {

    @SerializedName("0")
    private F_Null zero;

    public F_Null getzero() {return this.zero;}

    public void setzero(F_Null zero) {this.zero = zero;}

    /*
    @SerializedName("dateTime")
    private DateTime dateTime;

    public com.example.uebung1_nicolas_hofmaier.network.DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(com.example.uebung1_nicolas_hofmaier.network.DateTime dateTime) {
        this.dateTime = dateTime;
    }
*/

}
