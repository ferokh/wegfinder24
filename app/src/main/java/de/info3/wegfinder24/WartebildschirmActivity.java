package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.info3.wegfinder24.newtwork.JSON.Example;
import de.info3.wegfinder24.newtwork.OpenrouteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import javax.ws.rs.core.Response;

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

                Call<Example> resultCall = service.listAuto1();

                resultCall.enqueue(new Callback<Example>() {
                    @Override
                    public void onResponse(Call<Example> call, Response<Example> response) {
                        Example example = response.body();
                        //int i = 0;
                        Log.d("FeaturesList","Anfrage ging durch");
                        if (response.code() != 200){
                            Log.d("FeaturesList","Problem, viel Glück!");
                            return;
                        }

                        Double Distanz = example.getFeatures().get(0).getProperties().getSummary().getDistance();
                        Double Dauer = example.getFeatures().get(0).getProperties().getSummary().getDuration();
                        Log.i("Distanz", Double.toString(Distanz));
                        Log.i("Dauer", Double.toString(Dauer));

                    }

                    @Override
                    public void onFailure(Call<Example> call, Throwable t) {
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