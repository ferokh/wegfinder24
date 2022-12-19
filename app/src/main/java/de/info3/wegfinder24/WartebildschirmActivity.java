package de.info3.wegfinder24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.info3.wegfinder24.newtwork.DDD.CBW.Bike;
import de.info3.wegfinder24.newtwork.DDD.CBW.Car;
import de.info3.wegfinder24.newtwork.DDD.CBW.Walk;
import de.info3.wegfinder24.newtwork.DDD.DDProperties;
import de.info3.wegfinder24.newtwork.DDD.Distance;
import de.info3.wegfinder24.newtwork.DDD.Duration;
import de.info3.wegfinder24.newtwork.DDD.FST.First;
import de.info3.wegfinder24.newtwork.DDD.FST.Second;
import de.info3.wegfinder24.newtwork.DDD.FST.Third;
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

    DDProperties dd;

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
        double distance_car_first = datencar.getFeatures().get(0).getProperties().getSummary().getDistance();
        double result_car_distance_first[] = distanz(distance_car_first);

        double distance_car_second = datencar.getFeatures().get(1).getProperties().getSummary().getDistance();
        double result_car_distance_second[] = distanz(distance_car_second);

        double distance_car_third = datencar.getFeatures().get(2).getProperties().getSummary().getDistance();
        double result_car_distance_third[] = distanz(distance_car_third);
        
        int result_car_duration_first[] = dauer(datencar.getFeatures().get(0).getProperties().getSummary().getDuration());
        int result_car_duration_second[] = dauer(datencar.getFeatures().get(1).getProperties().getSummary().getDuration());
        int result_car_duration_third[] = dauer(datencar.getFeatures().get(2).getProperties().getSummary().getDuration());

        DDProperties Properties = new DDProperties();
        Car car = new Car();
        First firstcar = new First();
        Second secondcar = new Second();
        Third thirdcar = new Third();
        Distance firstcardistance = new Distance();
        Distance secondcardistance = new Distance();
        Distance thirdcardistance = new Distance();

        Properties.setCar(car);

        car.setFirst(firstcar);
        car.setSecond(secondcar);
        car.setThird(thirdcar);
        
        firstcar.setDistance(firstcardistance);
        firstcardistance.setMkm(Double.toString(result_car_distance_first[0]));
        firstcardistance.setMorKM(Double.toString(result_car_distance_first[1]));
        
        secondcar.setDistance(secondcardistance);
        secondcardistance.setMkm(Double.toString(result_car_distance_second[0]));
        secondcardistance.setMorKM(Double.toString(result_car_distance_second[1]));
        
        thirdcar.setDistance(thirdcardistance);
        thirdcardistance.setMkm(Double.toString(result_car_distance_third[0]));
        thirdcardistance.setMorKM(Double.toString(result_car_distance_third[1]));
        
        Duration firstcarduration = new Duration();
        Duration secondcarduration = new Duration();
        Duration thirdcarduration = new Duration();
        
        firstcar.setDuration(firstcarduration);
        firstcarduration.setHour(Integer.toString(result_car_duration_first[0]));
        firstcarduration.setMinute(Integer.toString(result_car_duration_first[1]));

        secondcar.setDuration(secondcarduration);
        secondcarduration.setHour(Integer.toString(result_car_duration_second[0]));
        secondcarduration.setMinute(Integer.toString(result_car_duration_second[1]));

        thirdcar.setDuration(thirdcarduration);
        thirdcarduration.setHour(Integer.toString(result_car_duration_third[0]));
        thirdcarduration.setMinute(Integer.toString(result_car_duration_third[1]));

        intent.putExtra("car_WP", (Serializable) datencar);

        //////////////////////////////////////////////BIKE///////////////////////////////////////////////////
        double distance_bike_first = datenbike.getFeatures().get(0).getProperties().getSummary().getDistance();
        double result_bike_distance_first[] = distanz(distance_bike_first);

        double distance_bike_second = datenbike.getFeatures().get(1).getProperties().getSummary().getDistance();
        double result_bike_distance_second[] = distanz(distance_bike_second);

        double distance_bike_third = datenbike.getFeatures().get(2).getProperties().getSummary().getDistance();
        double result_bike_distance_third[] = distanz(distance_bike_third);

        int result_bike_duration_first[] = dauer(datenbike.getFeatures().get(0).getProperties().getSummary().getDuration());
        int result_bike_duration_second[] = dauer(datenbike.getFeatures().get(1).getProperties().getSummary().getDuration());
        int result_bike_duration_third[] = dauer(datenbike.getFeatures().get(2).getProperties().getSummary().getDuration());

        Bike bike = new Bike();
        First firstbike = new First();
        Second secondbike = new Second();
        Third thirdbike = new Third();

        Properties.setBike(bike);

        Distance firstbikedistance = new Distance();
        Distance secondbikedistance = new Distance();
        Distance thirdbikedistance = new Distance();

        bike.setFirst(firstbike);
        bike.setSecond(secondbike);
        bike.setThird(thirdbike);

        firstbike.setDistance(firstbikedistance);
        firstbikedistance.setMkm(Double.toString(result_bike_distance_first[0]));
        firstbikedistance.setMorKM(Double.toString(result_bike_distance_first[1]));

        secondbike.setDistance(secondbikedistance);
        secondbikedistance.setMkm(Double.toString(result_bike_distance_second[0]));
        secondbikedistance.setMorKM(Double.toString(result_bike_distance_second[1]));

        thirdbike.setDistance(thirdbikedistance);
        thirdbikedistance.setMkm(Double.toString(result_bike_distance_third[0]));
        thirdbikedistance.setMorKM(Double.toString(result_bike_distance_third[1]));

        Duration firstbikeduration = new Duration();
        Duration secondbikeduration = new Duration();
        Duration thirdbikeduration = new Duration();

        firstbike.setDuration(firstbikeduration);
        firstbikeduration.setHour(Integer.toString(result_bike_duration_first[0]));
        firstbikeduration.setMinute(Integer.toString(result_bike_duration_first[1]));

        secondbike.setDuration(secondbikeduration);
        secondbikeduration.setHour(Integer.toString(result_bike_duration_second[0]));
        secondbikeduration.setMinute(Integer.toString(result_bike_duration_second[1]));

        thirdbike.setDuration(thirdbikeduration);
        thirdbikeduration.setHour(Integer.toString(result_bike_duration_third[0]));
        thirdbikeduration.setMinute(Integer.toString(result_bike_duration_third[1]));

        intent.putExtra("bike_WP",(Serializable) datenbike);
        
        //////////////////////////////////////////////WALK///////////////////////////////////////////////////
        double distance_walk_first = datenwalk.getFeatures().get(0).getProperties().getSummary().getDistance();
        double result_walk_distance_first[] = distanz(distance_walk_first);
        
        double distance_walk_second = datenwalk.getFeatures().get(1).getProperties().getSummary().getDistance();
        double result_walk_distance_second[] = distanz(distance_walk_second);
        
        double distance_walk_third = datenwalk.getFeatures().get(2).getProperties().getSummary().getDistance();
        double result_walk_distance_third[] = distanz(distance_walk_third);

        int result_walk_duration_first[] = dauer(datenwalk.getFeatures().get(0).getProperties().getSummary().getDuration());
        int result_walk_duration_second[] = dauer(datenwalk.getFeatures().get(1).getProperties().getSummary().getDuration());
        int result_walk_duration_third[] = dauer(datenwalk.getFeatures().get(2).getProperties().getSummary().getDuration());

        Walk walk = new Walk();
        First firstwalk = new First();
        Second secondwalk = new Second();
        Third thirdwalk = new Third();

        Properties.setWalk(walk);

        Distance firstwalkdistance = new Distance();
        Distance secondwalkdistance = new Distance();
        Distance thirdwalkdistance = new Distance();

        walk.setFirst(firstwalk);
        walk.setSecond(secondwalk);
        walk.setThird(thirdwalk);

        firstwalk.setDistance(firstwalkdistance);
        firstwalkdistance.setMkm(Double.toString(result_walk_distance_first[0]));
        firstwalkdistance.setMorKM(Double.toString(result_walk_distance_first[1]));

        secondwalk.setDistance(secondwalkdistance);
        secondwalkdistance.setMkm(Double.toString(result_walk_distance_second[0]));
        secondwalkdistance.setMorKM(Double.toString(result_walk_distance_second[1]));

        thirdwalk.setDistance(thirdwalkdistance);
        thirdwalkdistance.setMkm(Double.toString(result_walk_distance_third[0]));
        thirdwalkdistance.setMorKM(Double.toString(result_walk_distance_third[1]));

        Duration firstwalkduration = new Duration();
        Duration secondwalkduration = new Duration();
        Duration thirdwalkduration = new Duration();

        firstwalk.setDuration(firstwalkduration);
        firstwalkduration.setHour(Integer.toString(result_walk_duration_first[0]));
        firstwalkduration.setMinute(Integer.toString(result_walk_duration_first[1]));

        secondwalk.setDuration(secondwalkduration);
        secondwalkduration.setHour(Integer.toString(result_walk_duration_second[0]));
        secondwalkduration.setMinute(Integer.toString(result_walk_duration_second[1]));

        thirdwalk.setDuration(thirdwalkduration);
        thirdwalkduration.setHour(Integer.toString(result_walk_duration_third[0]));
        thirdwalkduration.setMinute(Integer.toString(result_walk_duration_third[1]));


        intent.putExtra("walk_WP",(Serializable) datenwalk);
        intent.putExtra("DaD",(Serializable) dd);
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