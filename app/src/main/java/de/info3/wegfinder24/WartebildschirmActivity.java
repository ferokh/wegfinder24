package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import de.info3.wegfinder24.newtwork.JSON.Example;
import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;
import de.info3.wegfinder24.newtwork.OpenrouteService;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import javax.ws.rs.core.Response;

import javax.xml.transform.Result;

public class WartebildschirmActivity extends AppCompatActivity {

    double BA = 8.681495;
    double LA = 49.41461;
    double BE = 8.687872;
    double LE = 49.420318;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wartebildschirm);

        // Koordinaten in Liste stecken
        List Anfang = new ArrayList();
        Anfang.add(BA);
        Anfang.add(LA);
        List Ende = new ArrayList();
        Ende.add(BE);
        Ende.add(LE);
        List Koordinaten = new ArrayList();
        Koordinaten.add(Anfang);
        Koordinaten.add(Ende);



        // Button
        Button btnOpenVariante =this.findViewById(R.id.btnweiter);
        Button btnBerechnen =this.findViewById(R.id.btnBerechnen);

        btnOpenVariante.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);
                startActivity(intent);
            }
        });

        btnBerechnen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {



                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest  = chain.request().newBuilder()
                                .addHeader("Authorization", "5b3ce3597851110001cf6248f06b7f011fe047c9b80f787320e4eada")
                                .addHeader("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .build();
                        return chain.proceed(newRequest);
                    }
                }).build();

                Retrofit retrofit = new Retrofit.Builder()
                        .client(client)
                        .baseUrl("https://api.openrouteservice.org")
                        //.baseUrl("https://webhook.site/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // POST Anfrage
                OpenrouteService service = retrofit.create(OpenrouteService.class);
                Anfrage anfrage = new Anfrage(Koordinaten, 3, "de");


                // send the Anfrage to the Rest API
                Call<Anfrage> resultCall = service.addAnfrage(anfrage);
                //Response<Anfrage> response = resultCall.execute();


                //assertTrue(response.isSuccessful());


                // GET Anfrage
                //OpenrouteService service = retrofit.create(OpenrouteService.class);
                //Call<Example> resultCall = service.listAuto1();

                resultCall.enqueue(new Callback<Anfrage>() {
                    @Override
                    public void onResponse(Call<Anfrage> call, retrofit2.Response<Anfrage> response) {
                        Anfrage example = response.body();
                        //int i = 0;
                        Log.d("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.d("FeaturesList","Problem, viel Glück!");
                            Integer ResponseCode = response.code();
                            Log.d("ResponseCode", Integer.toString(ResponseCode));
                            return;
                        }
                        //Double Distanz = example.getFeatures().get(0).getProperties().getSummary().getDistance();
                        //Double Dauer = example.getFeatures().get(0).getProperties().getSummary().getDuration();
                        //Log.i("Distanz", Double.toString(Distanz));
                        //Log.i("Dauer", Double.toString(Dauer));
                    }

                    @Override
                    public void onFailure(Call<Anfrage> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler");
                        //Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);
                        //startActivity(intent);
                    }







                });
            }
        });









    }
}