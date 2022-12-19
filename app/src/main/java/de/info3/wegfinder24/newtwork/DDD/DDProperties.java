package de.info3.wegfinder24.newtwork.DDD;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.info3.wegfinder24.newtwork.DDD.CBW.Bike;
import de.info3.wegfinder24.newtwork.DDD.CBW.Car;
import de.info3.wegfinder24.newtwork.DDD.CBW.Walk;

public class DDProperties {
    @SerializedName("Car")
    @Expose
    private Car car;
    @SerializedName("Bike")
    @Expose
    private Bike bike;
    @SerializedName("Walk")
    @Expose
    private Walk walk;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Bike getBike() {
        return bike;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    public Walk getWalk() {
        return walk;
    }

    public void setWalk(Walk walk) {
        this.walk = walk;
    }
}
