package de.info3.wegfinder24.newtwork;

//import javax.xml.transform.Result;

import android.util.Log;
import java.io.IOException;
import de.info3.wegfinder24.newtwork.JSON.Example;
import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenrouteService {

    //@GET("/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248f06b7f011fe047c9b80f787320e4eada&start=8.681495,49.41461&end=8.687872,49.420318")
    //Call<Example> listAuto1();

    //@POST("a14bd01f-2a7d-4669-a065-70c854efca1a")  // webhook.site Testen der Anfrage
    @POST("v2/directions/driving-car")
    Call<Anfrage> addAnfrage(@Body Anfrage anfrageauto);


}