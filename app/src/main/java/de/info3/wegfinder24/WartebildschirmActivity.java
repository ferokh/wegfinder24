package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.MediaType;

import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;
import de.info3.wegfinder24.newtwork.OpenRouteServiceBike;
import de.info3.wegfinder24.newtwork.OpenRouteServiceCar;

import de.info3.wegfinder24.newtwork.OpenRouteServiceWalk;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//import javax.ws.rs.core.Response;


public class WartebildschirmActivity extends AppCompatActivity {

    double BA = 0; //8.681495;
    double LA = 0; //49.41461;
    double BE = 0; //8.687872;
    double LE = 0; //49.420318;

    double car_distance = 0;
    double car_duration = 0;

    double bike_distance = 0;
    double bike_duration = 0;

    double walk_distance = 0;
    double walk_duration = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wartebildschirm);

        //TextView tvBikeDuration = this.findViewById(R.id.tvBikeDuration);
        Intent intent_StartEnter = this.getIntent();


        BA = intent_StartEnter.getDoubleExtra("Startlat",0);
        LA = intent_StartEnter.getDoubleExtra("Startlong",0);
        BE = intent_StartEnter.getDoubleExtra("Ziellat",0);
        LE = intent_StartEnter.getDoubleExtra("Ziellong",0);

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

        btnBerechnen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest  = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .addHeader("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                                .addHeader("Authorization", "5b3ce3597851110001cf6248f06b7f011fe047c9b80f787320e4eada")
                                .build();
                        return chain.proceed(newRequest);
                    }
                }).build();

                Retrofit retrofit = new Retrofit.Builder()
                        .client(client)
                        .baseUrl("https://api.openrouteservice.org/")
                        //.baseUrl("https://webhook.site/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Anfrage anfrage = new Anfrage(Koordinaten, 3, "de");

                //////////////////////////////////////////////CAR///////////////////////////////////////////////////
                // POST Anfrage
                OpenRouteServiceCar service_car = retrofit.create(OpenRouteServiceCar.class);
                // send the Anfrage to the Rest API
                Call<Anfrage> resultCall_car = service_car.addAnfrage(anfrage);
                resultCall_car.enqueue(new Callback<Anfrage>() {
                    @Override
                    public void onResponse(Call<Anfrage> call, retrofit2.Response<Anfrage> response) {
                        Anfrage example = response.body();
                        //int i = 0;
                        Log.i("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.i("FeaturesList","Problem, viel Glück!");
                            Integer ResponseCode = response.code();
                            Log.i("ResponseCode", Integer.toString(ResponseCode));
                            return;
                        }

                        Double Distanz_car = example.getFeatures().get(0).getProperties().getSummary().getDistance();
                        Double Dauer_car = example.getFeatures().get(0).getProperties().getSummary().getDuration();

                        car_distance = Distanz_car;
                        car_duration=Dauer_car;

                        Log.i("Distanz Auto", Double.toString(Distanz_car));
                        Log.i("Dauer Auto", Double.toString(Dauer_car));
                        //Log.i("Distanz Auto", Double.toString(car.getDistance()));
                        //Log.i("Dauer Auto", Double.toString(car.getDuration()));
                    }

                    @Override
                    public void onFailure(Call<Anfrage> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler");
                        //Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);
                        //startActivity(intent);
                    }
                }); //Auto Abfrage

                //////////////////////////////////////////////BIKE///////////////////////////////////////////////////
                // POST Anfrage
                OpenRouteServiceBike service_bike = retrofit.create(OpenRouteServiceBike.class);
                // send the Anfrage to the Rest API
                Call<Anfrage> resultCall_bike = service_bike.addAnfrage(anfrage);
                resultCall_bike.enqueue(new Callback<Anfrage>() {
                    @Override
                    public void onResponse(Call<Anfrage> call, retrofit2.Response<Anfrage> response) {
                        Anfrage example = response.body();
                        //int i = 0;
                        Log.i("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.i("FeaturesList","Problem, viel Glück!");
                            Integer ResponseCode = response.code();
                            Log.i("ResponseCode", Integer.toString(ResponseCode));
                            return;
                        }

                        Double Distanz_bike = example.getFeatures().get(0).getProperties().getSummary().getDistance();
                        Double Dauer_bike = example.getFeatures().get(0).getProperties().getSummary().getDuration();

                        bike_distance = Distanz_bike;
                        bike_duration = Dauer_bike;

                        Log.i("Distanz Fahrrad", Double.toString(Distanz_bike));
                        Log.i("Dauer Fahrrad", Double.toString(Dauer_bike));
                        //Log.i("Distanz Auto", Double.toString(car.getDistance()));
                        //Log.i("Dauer Auto", Double.toString(car.getDuration()));
                    }

                    @Override
                    public void onFailure(Call<Anfrage> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler");
                        //Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);
                        //startActivity(intent);
                    }
                }); //Fahrrad Abfrage

                //////////////////////////////////////////////WALK///////////////////////////////////////////////////
                // POST Anfrage
                OpenRouteServiceWalk service_walk = retrofit.create(OpenRouteServiceWalk.class);

                // send the Anfrage to the Rest API
                Call<Anfrage> resultCall_walk = service_walk.addAnfrage(anfrage);
                resultCall_walk.enqueue(new Callback<Anfrage>() {
                    @Override
                    public void onResponse(Call<Anfrage> call, retrofit2.Response<Anfrage> response) {
                        Anfrage example = response.body();
                        //int i = 0;
                        Log.i("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.i("FeaturesList","Problem, viel Glück!");
                            Integer ResponseCode = response.code();
                            Log.i("ResponseCode", Integer.toString(ResponseCode));
                            return;
                        }

                        Double Distanz_walk = example.getFeatures().get(0).getProperties().getSummary().getDistance();
                        Double Dauer_walk = example.getFeatures().get(0).getProperties().getSummary().getDuration();

                        walk_distance = Distanz_walk;
                        walk_duration = Dauer_walk;

                        Log.i("Distanz Fuß", Double.toString(Distanz_walk));
                        Log.i("Dauer Fuß", Double.toString(Dauer_walk));
                        //Log.i("Distanz Auto", Double.toString(car.getDistance()));
                        //Log.i("Dauer Auto", Double.toString(car.getDuration()));
                    }

                    @Override
                    public void onFailure(Call<Anfrage> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler");
                        //Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);
                        //startActivity(intent);
                    }
                }); //Fuß Abfrage
            }
        });

        btnOpenVariante.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);

                //////////////////////////////////////////////CAR///////////////////////////////////////////////////
                Log.i("Distanz Car", Double.toString(car_distance));
                Log.i("Dauer Car", Double.toString(car_duration));


                car_distance = car_distance / 1000; //Distanz von Metern in Kilometer
                car_distance = round(car_distance,1); //Mit der Funktion round (s.u.) auf eine Nachkommastelle runden

                if(car_distance < 1.0) //wenn es weniger als 1km ist
                {
                    double car_distance_meter = car_distance * 100;
                    intent.putExtra("Distanz_Car_Meter", Double.toString(car_distance_meter));
                }
                else //wenn es mehr als 1 km sind
                {
                    intent.putExtra("Distanz_Car", Double.toString(car_distance));
                }
                //intent.putExtra("Dauer_Car", Double.toString(car_duration));

                car_duration = car_duration / 60; //Dauer von Sekunden auf Minuten
                car_duration = round(car_duration,0);  //Mit der Funktion round (s.u.) auf keine Nachkommastelle runden

                int car_duration_int = (int) car_duration; //von double in int - von 1,0 auf 1 (sieht schöner aus)

                if(car_duration_int < 60)   //Wenn es weniger als 1h ist, im Format 00min darstellen
                {
                    int car_duration_minuten = car_duration_int;
                    intent.putExtra("Dauer_Car_Minuten", Integer.toString(car_duration_minuten));
                }
                else    //Wenn es mehr als 1h ist, im Format 0h 00min darstellen
                {
                    int stunden = 0;
                    int minuten = 0;
                    while (car_duration_int>59) //Berechnen wie viele Stunden es sind und was an Minuten übrig bleibt
                    {
                        car_duration_int = car_duration_int - 60;
                        stunden = stunden + 1;
                    }
                    minuten = car_duration_int;

                    intent.putExtra("Dauer_Car_Stunden", Integer.toString(stunden));
                    intent.putExtra("Dauer_Car_Stunden_Minuten",Integer.toString(minuten));
                }

                //////////////////////////////////////////////BIKE///////////////////////////////////////////////////
                Log.i("Distanz Bike", Double.toString(bike_distance));
                Log.i("Dauer Bike", Double.toString(bike_duration));

                bike_distance = bike_distance / 1000;
                bike_distance = round(bike_distance,1);
                //bike_distance = Math.round(bike_distance)*100;

                if(bike_distance < 1.0)
                {
                    double bike_distance_meter = bike_distance * 100;
                    intent.putExtra("Distanz_Bike_Meter", Double.toString(bike_distance_meter));
                }
                else
                {
                    intent.putExtra("Distanz_Bike", Double.toString(bike_distance));
                }

                bike_duration = bike_duration / 60;
                bike_duration = round(bike_duration,0);

                int bike_duration_int = (int) bike_duration;

                if(bike_duration_int < 60)
                {
                    int bike_duration_minuten = bike_duration_int;
                    intent.putExtra("Dauer_Bike_Minuten", Integer.toString(bike_duration_minuten));
                }
                else
                {
                    int stunden = 0;
                    int minuten = 0;
                    while (bike_duration_int>59)
                    {
                        bike_duration_int = bike_duration_int - 60;
                        stunden = stunden + 1;
                    }
                    minuten = bike_duration_int;

                    intent.putExtra("Dauer_Bike_Stunden", Integer.toString(stunden));
                    intent.putExtra("Dauer_Bike_Stunden_Minuten",Integer.toString(minuten));
                }

                //////////////////////////////////////////////WALK///////////////////////////////////////////////////
                Log.i("Distanz Walk", Double.toString(walk_distance));
                Log.i("Dauer Walk", Double.toString(walk_duration));

                walk_distance = walk_distance / 1000;
                walk_distance = round(walk_distance,1);

                if(walk_distance < 1.0)
                {
                    double walk_distance_meter = walk_distance * 100;
                    intent.putExtra("Distanz_Walk_Meter", Double.toString(walk_distance_meter));
                }
                else
                {
                    intent.putExtra("Distanz_Walk", Double.toString(walk_distance));
                }

                walk_duration = walk_duration / 60;
                walk_duration = round(walk_duration,0);

                int walk_duration_int = (int) walk_duration;

                if(walk_duration_int < 60)
                {
                    int walk_duration_minuten = walk_duration_int;
                    intent.putExtra("Dauer_Walk_Minuten", Integer.toString(walk_duration_minuten));
                }
                else
                {
                    int stunden = 0;
                    int minuten = 0;
                    while (walk_duration_int>59)
                    {
                        walk_duration_int = walk_duration_int - 60;
                        stunden = stunden + 1;
                    }
                    minuten = walk_duration_int;

                    intent.putExtra("Dauer_Walk_Stunden", Integer.toString(stunden));
                    intent.putExtra("Dauer_Walk_Stunden_Minuten",Integer.toString(minuten));
                }
                startActivity(intent);
            }
        });








    }

    private double round (double value, int decimalPoints)
    {
        double d = Math.pow(10,decimalPoints);
        return Math.round(value * d)/d;
    }
}