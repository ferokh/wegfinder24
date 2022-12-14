package de.info3.wegfinder24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.util.Distance;

import java.io.IOException;
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


//import javax.ws.rs.core.Response;


public class WartebildschirmActivity<Private> extends AppCompatActivity {
    RelativeLayout relativeLayout;
    /*double BA = 8.681495;
    double LA = 49.41461;
    double BE = 8.687872;
    double LE = 49.420318;*/
    AlertDialog.Builder builder;

    double BA = 0;
    double LA = 0;
    double BE = 0;
    double LE = 0;

    double car_distance = 0;
    double car_duration = 0;

    double bike_distance = 0;
    double bike_duration = 0;

    double walk_distance = 0;
    double walk_duration = 0;

    Integer status = 0;
    Integer ups = 0;

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

        //Progressbar
        ProgressBar roundProgress = (ProgressBar) findViewById(R.id.Progress);
        int maxValue = roundProgress.getMax();

        // Button
        //Button btnOpenVariante =this.findViewById(R.id.btnweiter);
        //Button btnBerechnen =this.findViewById(R.id.btnBerechnen);

       // btnBerechnen.setOnClickListener(new View.OnClickListener(){
       //     @Override
       //     public void onClick(View view) {

                Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);
                Intent fehler = new Intent(WartebildschirmActivity.this, EingabeActivity.class);

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
                        Anfrage example = response.body();
                        //int i = 0;
                        Log.i("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.i("FeaturesList","Problem, viel Glück!");
                            Integer ResponseCode = response.code();
                            Log.i("ResponseCode", Integer.toString(ResponseCode));
                            //startActivity(fehler);
                            return;
                        }

                        Double Distanz_car = example.getFeatures().get(0).getProperties().getSummary().getDistance();
                        Double Dauer_car = example.getFeatures().get(0).getProperties().getSummary().getDuration();

                        car_distance = Distanz_car;
                        car_duration=Dauer_car;
                        status ++;
                        Log.i("Distanz Auto", Double.toString(Distanz_car));
                        Log.i("Dauer Auto", Double.toString(Dauer_car));

                        //Log.i("Distanz Auto", Double.toString(car.getDistance()));
                        //Log.i("Dauer Auto", Double.toString(car.getDuration()));
                    }

                    @Override
                    public void onFailure(Call<Anfrage> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler1");
                        ups ++;
                        Log.d("ups1", Integer.toString(ups));

                        //Context context = getApplicationContext();
                        //CharSequence toastText = "dffgbjidfbjl";
                        //Toast.makeText(context, toastText, Toast.LENGTH_LONG);
                        //startActivity(fehler);

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
                        Anfrage example = response.body();
                        //int i = 0;
                        Log.i("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.i("FeaturesList","Problem, viel Glück!");
                            Integer ResponseCode = response.code();
                            Log.i("ResponseCode", Integer.toString(ResponseCode));
                            //startActivity(fehler);
                            return;
                        }

                        Double Distanz_bike = example.getFeatures().get(0).getProperties().getSummary().getDistance();
                        Double Dauer_bike = example.getFeatures().get(0).getProperties().getSummary().getDuration();

                        bike_distance = Distanz_bike;
                        bike_duration = Dauer_bike;
                        status ++;
                        Log.i("Distanz Fahrrad", Double.toString(Distanz_bike));
                        Log.i("Dauer Fahrrad", Double.toString(Dauer_bike));
                        //Log.i("Distanz Auto", Double.toString(car.getDistance()));
                        //Log.i("Dauer Auto", Double.toString(car.getDuration()));
                    }

                    @Override
                    public void onFailure(Call<Anfrage> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler");
                        //startActivity(fehler);
                        ups ++;
                        Log.d("ups2", Integer.toString(ups));
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
                        Anfrage example = response.body();
                        //int i = 0;
                        Log.i("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.i("FeaturesList","Problem, viel Glück!");
                            Integer ResponseCode = response.code();
                            Log.i("ResponseCode", Integer.toString(ResponseCode));
                            //startActivity(fehler);
                            return;
                        }

                        Double Distanz_walk = example.getFeatures().get(0).getProperties().getSummary().getDistance();
                        Double Dauer_walk = example.getFeatures().get(0).getProperties().getSummary().getDuration();

                        walk_distance = Distanz_walk;
                        walk_duration = Dauer_walk;

                        status ++;

                        Log.i("Distanz Fuß", Double.toString(Distanz_walk));
                        Log.i("Dauer Fuß", Double.toString(Dauer_walk));
                        //Log.i("Distanz Auto", Double.toString(car.getDistance()));
                        //Log.i("Dauer Auto", Double.toString(car.getDuration()));
                    }

                    @Override
                    public void onFailure(Call<Anfrage> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler");
                        ups ++;
                        Log.d("ups1", Integer.toString(ups));

                    }
                }); //Fuß Abfrage

                Log.d("ups4", Integer.toString(status));

                int ii = 0;
                int kombi = 0;

                do {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Log.d("Warten", "Warten");
                        }
                    }, 1000);

                    ii = ii + 1;

                    kombi = ups + status;

                    if (status == 3){
                            //////////////////////////////////////////////CAR///////////////////////////////////////////////////
                            Log.i("Distanz Car", Double.toString(car_distance));
                            Log.i("Dauer Car", Double.toString(car_duration));

                            double result_car_distance[] = distanz(car_distance);
                            if (result_car_distance[1] == 0) {
                                intent.putExtra("Distanz_Car_Meter", Double.toString(result_car_distance[0]));
                            } else {
                                intent.putExtra("Distanz_Car", Double.toString(result_car_distance[0]));
                            }

                            int result_car_duration[] = dauer(car_duration);
                            if (result_car_duration[0] == 0) {
                                intent.putExtra("Dauer_Car_Minuten", Integer.toString(result_car_duration[1]));
                            } else {
                                intent.putExtra("Dauer_Car_Stunden", Integer.toString(result_car_duration[0]));
                                intent.putExtra("Dauer_Car_Stunden_Minuten", Integer.toString(result_car_duration[1]));
                            }

                            //////////////////////////////////////////////BIKE///////////////////////////////////////////////////
                            Log.i("Distanz Bike", Double.toString(bike_distance));
                            Log.i("Dauer Bike", Double.toString(bike_duration));

                            double result_bike_distance[] = distanz(bike_distance);
                            if (result_bike_distance[1] == 0) {
                                intent.putExtra("Distanz_Bike_Meter", Double.toString(result_bike_distance[0]));
                            } else {
                                intent.putExtra("Distanz_Bike", Double.toString(result_bike_distance[0]));
                            }

                            int result_bike_duration[] = dauer(bike_duration);
                            if (result_bike_duration[0] == 0) {
                                intent.putExtra("Dauer_Bike_Minuten", Integer.toString(result_bike_duration[1]));
                            } else {
                                intent.putExtra("Dauer_Bike_Stunden", Integer.toString(result_bike_duration[0]));
                                intent.putExtra("Dauer_Bike_Stunden_Minuten", Integer.toString(result_bike_duration[1]));
                            }

                            //////////////////////////////////////////////WALK///////////////////////////////////////////////////
                            Log.i("Distanz Walk", Double.toString(walk_distance));
                            Log.i("Dauer Walk", Double.toString(walk_duration));

                            double result_walk_distance[] = distanz(walk_distance);
                            if (result_walk_distance[1] == 0) {
                                intent.putExtra("Distanz_Walk_Meter", Double.toString(result_walk_distance[0]));
                            } else {
                                intent.putExtra("Distanz_Walk", Double.toString(result_walk_distance[0]));
                            }

                            int result_walk_duration[] = dauer(walk_duration);
                            if (result_walk_duration[0] == 0) {
                                intent.putExtra("Dauer_Walk_Minuten", Integer.toString(result_walk_duration[1]));
                            } else {
                                intent.putExtra("Dauer_Walk_Stunden", Integer.toString(result_walk_duration[0]));
                                intent.putExtra("Dauer_Walk_Stunden_Minuten", Integer.toString(result_walk_duration[1]));
                            }

                            intent.putExtra("Startlat", Double.toString(BA));
                            intent.putExtra("Startlong", Double.toString(LA));
                            intent.putExtra("Ziellat", Double.toString(BE));
                            intent.putExtra("Ziellong", Double.toString(LE));

                            startActivity(intent);


                    }
                }while(kombi < 3 || ii < 10);

                builder =new AlertDialog.Builder(this);
                builder.setTitle("Fehler")
                .setMessage("Es ist ein Problem mit der Internetverbindung oder mit der Datenübermittlung aufgetreten.")
                .setPositiveButton("coolcoolcool", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(fehler);
                    }
                })
                .show();
/*
                if(!ups) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //////////////////////////////////////////////CAR///////////////////////////////////////////////////
                            Log.i("Distanz Car", Double.toString(car_distance));
                            Log.i("Dauer Car", Double.toString(car_duration));

                            double result_car_distance[] = distanz(car_distance);
                            if (result_car_distance[1] == 0) {
                                intent.putExtra("Distanz_Car_Meter", Double.toString(result_car_distance[0]));
                            } else {
                                intent.putExtra("Distanz_Car", Double.toString(result_car_distance[0]));
                            }

                            int result_car_duration[] = dauer(car_duration);
                            if (result_car_duration[0] == 0) {
                                intent.putExtra("Dauer_Car_Minuten", Integer.toString(result_car_duration[1]));
                            } else {
                                intent.putExtra("Dauer_Car_Stunden", Integer.toString(result_car_duration[0]));
                                intent.putExtra("Dauer_Car_Stunden_Minuten", Integer.toString(result_car_duration[1]));
                            }

                            //////////////////////////////////////////////BIKE///////////////////////////////////////////////////
                            Log.i("Distanz Bike", Double.toString(bike_distance));
                            Log.i("Dauer Bike", Double.toString(bike_duration));

                            double result_bike_distance[] = distanz(bike_distance);
                            if (result_bike_distance[1] == 0) {
                                intent.putExtra("Distanz_Bike_Meter", Double.toString(result_bike_distance[0]));
                            } else {
                                intent.putExtra("Distanz_Bike", Double.toString(result_bike_distance[0]));
                            }

                            int result_bike_duration[] = dauer(bike_duration);
                            if (result_bike_duration[0] == 0) {
                                intent.putExtra("Dauer_Bike_Minuten", Integer.toString(result_bike_duration[1]));
                            } else {
                                intent.putExtra("Dauer_Bike_Stunden", Integer.toString(result_bike_duration[0]));
                                intent.putExtra("Dauer_Bike_Stunden_Minuten", Integer.toString(result_bike_duration[1]));
                            }

                            //////////////////////////////////////////////WALK///////////////////////////////////////////////////
                            Log.i("Distanz Walk", Double.toString(walk_distance));
                            Log.i("Dauer Walk", Double.toString(walk_duration));

                            double result_walk_distance[] = distanz(walk_distance);
                            if (result_walk_distance[1] == 0) {
                                intent.putExtra("Distanz_Walk_Meter", Double.toString(result_walk_distance[0]));
                            } else {
                                intent.putExtra("Distanz_Walk", Double.toString(result_walk_distance[0]));
                            }

                            int result_walk_duration[] = dauer(walk_duration);
                            if (result_walk_duration[0] == 0) {
                                intent.putExtra("Dauer_Walk_Minuten", Integer.toString(result_walk_duration[1]));
                            } else {
                                intent.putExtra("Dauer_Walk_Stunden", Integer.toString(result_walk_duration[0]));
                                intent.putExtra("Dauer_Walk_Stunden_Minuten", Integer.toString(result_walk_duration[1]));
                            }

                            intent.putExtra("Startlat", Double.toString(BA));
                            intent.putExtra("Startlong", Double.toString(LA));
                            intent.putExtra("Ziellat", Double.toString(BE));
                            intent.putExtra("Ziellong", Double.toString(LE));

                            startActivity(intent);
                        }
                    }, 2000);
                }
                else  {

                    builder =new AlertDialog.Builder(this);
                    builder.setTitle("Fehler")
                            .setMessage("Es ist ein Problem mit der Internetverbindung oder mit der Datenübermittlung aufgetreten.")
                            .setPositiveButton("coolcoolcool", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(fehler);
                                }
                            })
                            .show();

                }
*/
        //    }

       //});


        /*btnOpenVariante.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);

                //////////////////////////////////////////////CAR///////////////////////////////////////////////////
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
                startActivity(intent);
            }
        });*/
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
    @Override
    protected void onResume() {
        super.onResume();
        //ups = false;
    }

}