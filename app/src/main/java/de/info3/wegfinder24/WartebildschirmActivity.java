package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.info3.wegfinder24.newtwork.Result;
import de.info3.wegfinder24.newtwork.TestJSON.Summary;
import de.info3.wegfinder24.newtwork.OpenrouteService;
import java.util.ArrayList;

import de.info3.wegfinder24.newtwork.OpenrouteService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MediaType;
//import javax.xml.transform.Result;

public class WartebildschirmActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wartebildschirm);

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

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.openrouteservice.org")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                OpenrouteService service = retrofit.create(OpenrouteService.class);

                Call<Result> resultCall = service.listAuto1();

                resultCall.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        //int i = 0;
                        Log.d("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.d("FeaturesList","Problem, viel Glück!");
                            return;
                        }
                        //Log.i("WartebildschirmActivity", String.valueOf(result.getfeaturesList().size()));
                        //Log.d("Name"+' ' +i, result.getDepartureList().get(i).getServingLine().getName());
                        //Log.d("Dauer",result.getfeaturesList().get(0).getzero().getproperties().getsummary().getduration());
                        //TODO:de.info3.wegfinder24.newtwork.F_Null.getproperties()' on a null object reference kommt als Fehlermeldung
                        //Log.d("Dauer", result.getfeaturesList().get(0).getzero().getproperties().getsummary().getduration().toString());
                        //Log.d("Dauer",result.getFeatures().get(0).getProperties().getSummary().getDuration().toString());
                        //Double Dauer = result.getFeatures().get(0).getProperties().getSummary().getDuration();
                        //Double Dauer = Summary.
                        //getFeatures().get(0).getProperties().getSummary().getDuration();
                        //Log.d("Dauer", result.);
                        //Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);
                        //intent.putExtra("Message",Dauer);
                        //startActivity(intent);

                        //Log.i("DepartureActivity", String.valueOf(result.getDepartureList().size()));

                        for ( int i = 0; i < 40; i++)
                        {
                            //Log.i("Ausgabe", String.valueOf(result.getDepartureList().get(i)));
                            //Log.i("Ausgabe" +i, result.getDepartureList().get(i).getServingLine().getName());
                            //Log.i("Ausgabe" +i, result.getDepartureList().get(i).getServingLine().getNumber());
                            //Log.i("Ausgabe" +i, result.getDepartureList().get(i).getServingLine().getDestination());
                            Double Distanz = result.getDepartureList().get(i).getdidu().getDistance();
                            Double Dauer = result.getDepartureList().get(i).getdidu().getDuration();
                            Log.i("Ausgabe" +i, Double.toString(Distanz));
                            Log.i("Ausgabe" +i, Double.toString(Dauer));
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Log.d("WartebildschirmActivity", "Anfragefehler");
                        //Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);
                        //startActivity(intent);
                    }
                });
            }
        });


/*      Kopiert aus dem /v2/directions/{profile}/geojson vom Openrouteservice

        Client client = ClientBuilder.newClient();
        Entity<String> payload = Entity.json({"coordinates":[[8.681495,49.41461],[8.687872,49.420318]],"alternative_routes":{"target_count":3},"language":"de"});
        Response response = client.target("https://api.openrouteservice.org/v2/directions/driving-car/geojson")
                .request()
                .header("Authorization", "5b3ce3597851110001cf6248f06b7f011fe047c9b80f787320e4eada")
                .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                .header("Content-Type", "application/json; charset=utf-8")
                .post(payload);

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        System.out.println("body:" + response.readEntity(String.class));

*/





    }
}