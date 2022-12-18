package de.info3.wegfinder24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Button;

import org.osmdroid.util.Distance;

import java.io.IOException;
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

    double BA = 0;
    double LA = 0;
    double BE = 0;
    double LE = 0;

    Anfrage datencar;
    Anfrage datenwalk;
    Anfrage datenbike;

    int status = 0;
    int ups_verbindung = 0;
    int ups_uebertragung = 0;

    Integer RP_car = 200;
    Integer RP_bike = 200;
    Integer RP_walk = 200;

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
        Anfang.add(LA);
        Anfang.add(BA);
        List Ende = new ArrayList();
        Ende.add(LE);
        Ende.add(BE);
        List Koordinaten = new ArrayList();
        Koordinaten.add(Anfang);
        Koordinaten.add(Ende);

        //Progressbar
        ProgressBar roundProgress = (ProgressBar) findViewById(R.id.Progress);
        int maxValue = roundProgress.getMax();


                Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);


                roundProgress.setVisibility(View.VISIBLE);

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
                roundProgress.setProgress(10);
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
                        datencar = response.body();
                        //int i = 0;
                        Log.i("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.i("FeaturesList","Problem, viel Glück!");
                            RP_car = response.code();
                            Log.i("ResponseCode", Integer.toString(RP_car));
                            ups_uebertragung++;
                        }

                        // Datenübergabe oder Fehler abfangen
                        status++;
                        if (status == 3 && ups_uebertragung == 0) {
                            Datenuebergabe();
                        }
                        if (status == 3 && ups_uebertragung > 0) {
                            UPS_uebertragung();
                        }
                    }

                    @Override
                    public void onFailure(Call<Anfrage> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler");
                        ups_verbindung++;
                        if (status + ups_verbindung == 3) {
                            UPS_verbindung();
                        }
                    }
                }); //Auto Abfrage

                roundProgress.setProgress(40);

                //////////////////////////////////////////////BIKE///////////////////////////////////////////////////
                // POST Anfrage
                OpenRouteServiceBike service_bike = retrofit.create(OpenRouteServiceBike.class);
                // send the Anfrage to the Rest API
                Call<Anfrage> resultCall_bike = service_bike.addAnfrage(anfrage);
                resultCall_bike.enqueue(new Callback<Anfrage>() {
                    @Override
                    public void onResponse(Call<Anfrage> call, retrofit2.Response<Anfrage> response) {
                        datenbike = response.body();
                        //int i = 0;
                        Log.i("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.i("FeaturesList","Problem, viel Glück!");
                            RP_bike = response.code();
                            Log.i("ResponseCode", Integer.toString(RP_bike));
                            ups_uebertragung++;
                        }

                        // Datenübergabe oder Fehler abfangen
                        status++;
                        if (status == 3 && ups_uebertragung == 0) {
                            Datenuebergabe();
                        }
                        if (status == 3 && ups_uebertragung > 0) {
                            UPS_uebertragung();
                        }
                    }

                    @Override
                    public void onFailure(Call<Anfrage> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler");
                        ups_verbindung++;
                        if (status + ups_verbindung == 3) {
                            UPS_verbindung();
                        }
                    }
                }); //Fahrrad Abfrage

                roundProgress.setProgress(70);

                //////////////////////////////////////////////WALK///////////////////////////////////////////////////
                // POST Anfrage
                OpenRouteServiceWalk service_walk = retrofit.create(OpenRouteServiceWalk.class);

                // send the Anfrage to the Rest API
                Call<Anfrage> resultCall_walk = service_walk.addAnfrage(anfrage);
                resultCall_walk.enqueue(new Callback<Anfrage>() {
                    @Override
                    public void onResponse(Call<Anfrage> call, retrofit2.Response<Anfrage> response) {
                        datenwalk = response.body();
                        //int i = 0;
                        Log.i("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.i("FeaturesList","Problem, viel Glück!");
                            RP_walk = response.code();
                            Log.i("ResponseCode", Integer.toString(RP_walk));
                            ups_uebertragung++;
                        }

                        // Datenübergabe oder Fehler abfangen
                        status++;
                        if (status == 3 && ups_uebertragung == 0) {
                            Datenuebergabe();
                        }
                        if (status == 3 && ups_uebertragung > 0) {
                            UPS_uebertragung();
                        }
                    }

                    @Override
                    public void onFailure(Call<Anfrage> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler");
                        ups_verbindung++;
                        if (status + ups_verbindung == 3) {
                            UPS_verbindung();
                        }
                    }
                }); //Fuß Abfrage


        //    }

       //});




    }
    private void UPS_verbindung(){
        Intent fehler = new Intent(WartebildschirmActivity.this, EingabeActivity.class);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Fehler")
                .setMessage("Es konnte keine Verbindung mit dem Karten-Dienst hergestellt werden." +
                        "Bitte überprüfe deine Internetverbindung.")
                .setPositiveButton("coolcoolcool und zurück", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(fehler);
                    }
                })
                .show();
    }
    private void UPS_uebertragung(){
        Intent fehler = new Intent(WartebildschirmActivity.this, EingabeActivity.class);
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Fehler")
                .setMessage("Es konnte keine Daten vom Navigationsdienst empfangen werden.\n" +
                        "Überprüfe, ob die Start- und Endpunkte korrekt in den Eingabefeldern eingegeben wurden, oder wende dich an die Entwickler.\n" +
                        "RP-Auto: " + RP_car + "\nRP-Gehen: " + RP_walk + "\nRP-Fahrrad: " + RP_bike)
                .setPositiveButton("coolcoolcool und zurück", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(fehler);
                    }
                })
                .show();
    }
    private void Datenuebergabe () {
        Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);

        //////////////////////////////////////////////CAR///////////////////////////////////////////////////
        Double car_distance = datencar.getFeatures().get(0).getProperties().getSummary().getDistance();
        Double car_duration = datencar.getFeatures().get(0).getProperties().getSummary().getDuration();


        Log.i("Distanz Car", Double.toString(car_distance));
        Log.i("Dauer Car", Double.toString(car_duration));

        double result_car_distance[] = distanz(car_distance);
        if (result_car_distance[1] == 0)
        {
            intent.putExtra("Distanz_Car_Meter", Double.toString(result_car_distance[0]));
        }
        else
        {
            intent.putExtra("Distanz_Car", Double.toString(result_car_distance[0]));
        }

        int result_car_duration [] = dauer(car_duration);
        if (result_car_duration[0] == 0)
        {
            intent.putExtra("Dauer_Car_Minuten", Integer.toString(result_car_duration[1]));
        }
        else
        {
            intent.putExtra("Dauer_Car_Stunden", Integer.toString(result_car_duration[0]));
            intent.putExtra("Dauer_Car_Stunden_Minuten",Integer.toString(result_car_duration[1]));
        }

        //////////////////////////////////////////////BIKE///////////////////////////////////////////////////
        Double bike_distance = datenbike.getFeatures().get(0).getProperties().getSummary().getDistance();
        Double bike_duration = datenbike.getFeatures().get(0).getProperties().getSummary().getDuration();


        Log.i("Distanz Bike", Double.toString(bike_distance));
        Log.i("Dauer Bike", Double.toString(bike_duration));

        double result_bike_distance[] = distanz(bike_distance);
        if (result_bike_distance[1] == 0)
        {
            intent.putExtra("Distanz_Bike_Meter", Double.toString(result_bike_distance[0]));
        }
        else
        {
            intent.putExtra("Distanz_Bike", Double.toString(result_bike_distance[0]));
        }

        int result_bike_duration [] = dauer(bike_duration);
        if (result_bike_duration[0] == 0)
        {
            intent.putExtra("Dauer_Bike_Minuten", Integer.toString(result_bike_duration[1]));
        }
        else
        {
            intent.putExtra("Dauer_Bike_Stunden", Integer.toString(result_bike_duration[0]));
            intent.putExtra("Dauer_Bike_Stunden_Minuten",Integer.toString(result_bike_duration[1]));
        }

        //////////////////////////////////////////////WALK///////////////////////////////////////////////////
        Double walk_distance = datenwalk.getFeatures().get(0).getProperties().getSummary().getDistance();
        Double walk_duration = datenwalk.getFeatures().get(0).getProperties().getSummary().getDuration();


        Log.i("Distanz Walk", Double.toString(walk_distance));
        Log.i("Dauer Walk", Double.toString(walk_duration));

        double result_walk_distance[] = distanz(walk_distance);
        if (result_walk_distance[1] == 0)
        {
            intent.putExtra("Distanz_Walk_Meter", Double.toString(result_walk_distance[0]));
        }
        else
        {
            intent.putExtra("Distanz_Walk", Double.toString(result_walk_distance[0]));
        }

        int result_walk_duration [] = dauer(walk_duration);
        if (result_walk_duration[0] == 0)
        {
            intent.putExtra("Dauer_Walk_Minuten", Integer.toString(result_walk_duration[1]));
        }
        else
        {
            intent.putExtra("Dauer_Walk_Stunden", Integer.toString(result_walk_duration[0]));
            intent.putExtra("Dauer_Walk_Stunden_Minuten",Integer.toString(result_walk_duration[1]));
        }

        intent.putExtra("Startlat",Double.toString(BA));
        intent.putExtra("Startlong",Double.toString(LA));
        intent.putExtra("Ziellat",Double.toString(BE));
        intent.putExtra("Ziellong",Double.toString(LE));

        startActivity(intent);
    }
    private double round (double value, int decimalPoints)
    {
        double d = Math.pow(10,decimalPoints);
        return Math.round(value * d)/d;
    }

    private double[] distanz (double Distanz)
    {
        Distanz = Distanz / 1000;
        Distanz = round(Distanz,1);

        if(Distanz < 1.0)
        {
            Distanz = Distanz * 100;
            return new double [] {Distanz, 0};
        }
        else
        {
            return new double[] {Distanz,1};
        }
    }

    private int[] dauer(double Dauer)
    {
        Dauer = Dauer / 60;
        Dauer = round(Dauer,0);

        int minuten = (int) Dauer;
        int stunden = 0;

        if(minuten < 60)
        {
            return new int[] {stunden,minuten};
        }
        else
        {
            while (minuten>59)
            {
                minuten = minuten - 60;
                stunden = stunden + 1;
            }
            return new int[] {stunden,minuten};
        }
    }
}