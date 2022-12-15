package de.info3.wegfinder24.newtwork;

import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OpenRouteServiceBike {
    @POST("v2/directions/cycling-regular/geojson")
    Call<Anfrage> addAnfrage(@Body Anfrage anfrageauto);
}
