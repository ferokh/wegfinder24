package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import javax.xml.transform.Result;

import de.info3.wegfinder24.newtwork.OpenrouteService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

public class WartebildschirmActivity extends AppCompatActivity {

    //private RequestQueue requestQueue; // Volley

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wartebildschirm);
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openrouteservice.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenrouteService service = retrofit.create(OpenrouteService.class);

        //Call<Result> resultCall = service.listDepartures();


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



        // Button Button Button
        Button btnOpenVariante =this.findViewById(R.id.btnweiter);

        btnOpenVariante.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WartebildschirmActivity.this, VarianteActivity.class);
                startActivity(intent);
            }
        });



    }
}