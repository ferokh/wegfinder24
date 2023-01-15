package de.info3.wegfinder24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

        Anfrage anfrage = new Anfrage(Koordinaten, 0.5,3, 2.5, "de");

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
        builder.setTitle("Fehler - wir haben es gesagt")
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
        ArrayList<Anfrage> daten = new ArrayList<>(3);
        // immer: Reihenfolge im Array: Bike=0, Car=1, Walk=2       !!!
        daten.add(datenbike);
        daten.add(datencar);
        daten.add(datenwalk);
        intent.putExtra("daten", daten);
        startActivity(intent);
    }
}