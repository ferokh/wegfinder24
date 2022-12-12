package de.info3.wegfinder24.newtwork;

//import javax.xml.transform.Result;

import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OpenrouteService {

    //@POST("a14bd01f-2a7d-4669-a065-70c854efca1a")  // webhook.site Testen der Anfrage
    @POST("v2/directions/driving-car/geojson")
    Call<Anfrage> addAnfrage(@Body Anfrage anfrageauto);


}