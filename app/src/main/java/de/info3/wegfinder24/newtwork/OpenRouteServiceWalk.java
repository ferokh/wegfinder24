package de.info3.wegfinder24.newtwork;

import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OpenRouteServiceWalk {
    @POST("v2/directions/foot-walking/geojson")
    Call<Anfrage> addAnfrage(@Body Anfrage anfrageauto);
}
