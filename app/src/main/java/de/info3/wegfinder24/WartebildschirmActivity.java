package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.info3.wegfinder24.newtwork.Result;
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
import javax.ws.rs.core.MediaType;
//import javax.xml.transform.Result;

public class WartebildschirmActivity extends AppCompatActivity {

    //private RequestQueue requestQueue; // Volley

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
                /*Client client = ClientBuilder.newClient();
                Response response = client.target("https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248f06b7f011fe047c9b80f787320e4eada&start=8.681495,49.41461&end=8.687872,49.420318")
                        .request(MediaType.TEXT_PLAIN_TYPE)
                        .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                        .get();

                System.out.println("status: " + response.getStatus());
                System.out.println("headers: " + response.getHeaders());
                System.out.println("body:" + response.readEntity(String.class));*/ //Get Diretions Service
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.openrouteservice.org/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                OpenrouteService service = retrofit.create(OpenrouteService.class);

                Call<Result> resultCall = service.listAuto1();

                resultCall.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        Log.d("FeaturesList","Anfrage ging durch");
                        //Log.i("DepartureActivity", String.valueOf(result.getfeaturesList().size()));
                        //Log.d("Name"+' ' +i, result.getDepartureList().get(i).getServingLine().getName());
                        //Log.d("Dauer",result.getfeaturesList().get(0).getzero().getproperties().getsummary().getduration());
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Log.d("DepartureActivity", "Anfragefehler");
                    }
                });
                Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);
                startActivity(intent);
            }
        });
        /*
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        String url = "https://httpbin.org/post";

        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);        */

        /*
        Client client = ClientBuilder.newClient();
        Entity<String> payload = Entity.json({"coordinates": [[8.681495,49.41461],[8.686507,49.41943],[8.687872,49.420318]]});
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