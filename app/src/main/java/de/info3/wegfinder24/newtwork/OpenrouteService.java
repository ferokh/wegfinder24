package de.info3.wegfinder24.newtwork;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OpenrouteService {

    //@GET("sl3-alone/XSLT_DM_REQUEST?outputFormat=JSON&coordOutputFormat=WGS84[dd.ddddd]&depType=stopEvents&locationServerActive=1&mode=direct&name_dm=7001001&type_dm=stop&useOnlyStops=1&useRealtime=1")
    //Call<Result> listDepartures();
    @GET("v2/directions/driving-car?api_key=5b3ce3597851110001cf6248f06b7f011fe047c9b80f787320e4eada&start=8.681495,49.41461&end=8.687872,49.420318")
    Call<Result> listAuto1();
}