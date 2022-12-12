package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class VarianteActivity extends AppCompatActivity {

    private MapView mapView;

    private LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variante);

        Button btnBike =this.findViewById(R.id.btnBike);
        Button btnCar =this.findViewById(R.id.btnCar);
        Button btnWalk =this.findViewById(R.id.btnWalk);
        Button btnBacktoEingabe =this.findViewById(R.id.btnbacktoEingabe);


        //////////////////////////////////////////////CAR///////////////////////////////////////////////////
        TextView tvCarDistance = this.findViewById(R.id.tvCarDistance);
        Intent intent_CarDistance = this.getIntent();
        if (intent_CarDistance != null) {
            String distance = "0";
            String distance_meter = "0";
            distance = intent_CarDistance.getStringExtra("Distanz_Car");
            distance_meter = intent_CarDistance.getStringExtra("Distanz_Car_Meter");
            if (distance == "0") //dann ist es unter 1km = Anzeige in Meter
            {
                tvCarDistance.setText(distance_meter + " m");
            }
            else
            {
                tvCarDistance.setText(distance + " km");
            }
        }

        TextView tvCarDuration = this.findViewById(R.id.tvCarDuration);
        Intent intent_CarDuration = this.getIntent();
        if (intent_CarDuration != null) {
            String duration_stunde = "0";
            String duration_stunde_minute = "0";
            String duration_minute = "0";

            duration_stunde = intent_CarDuration.getStringExtra("Dauer_Car_Stunden");
            duration_stunde_minute = intent_CarDuration.getStringExtra("Dauer_Car_Stunden_Minuten");
            duration_minute = intent_CarDuration.getStringExtra("Dauer_Car_Minuten");

            if (duration_minute != "0") //dann ist es unter 1h = Anzeige in Minuten
            {
                tvCarDuration.setText(duration_minute + " min");
            }
            else
            {
                tvCarDuration.setText(duration_stunde + " h " + duration_stunde_minute + " min");
            }
        }

        //////////////////////////////////////////////BIKE///////////////////////////////////////////////////
        TextView tvBikeDistance = this.findViewById(R.id.tvBikeDistance);
        Intent intent_BikeDistance = this.getIntent();
        if (intent_BikeDistance != null) {
            String distance = "0";
            String distance_meter = "0";
            distance = intent_BikeDistance.getStringExtra("Distanz_Bike");
            distance_meter = intent_BikeDistance.getStringExtra("Distanz_Bike_Meter");
            if (distance == "0") //dann ist es unter 1km = Anzeige in Meter
            {
                tvBikeDistance.setText(distance_meter + " m");
            }
            else
            {
                tvBikeDistance.setText(distance + " km");
            }
        }

        TextView tvBikeDuration = this.findViewById(R.id.tvBikeDuration);
        Intent intent_BikeDuration = this.getIntent();
        if (intent_BikeDuration != null) {
            String duration_stunde = "0";
            String duration_stunde_minute = "0";
            String duration_minute = "0";

            duration_stunde = intent_BikeDuration.getStringExtra("Dauer_Bike_Stunden");
            duration_stunde_minute = intent_BikeDuration.getStringExtra("Dauer_Bike_Stunden_Minuten");
            duration_minute = intent_BikeDuration.getStringExtra("Dauer_Bike_Minuten");

            if (duration_minute != "0") //dann ist es unter 1h = Anzeige in Minuten
            {
                tvBikeDuration.setText(duration_minute + " min");
            }
            else
            {
                tvBikeDuration.setText(duration_stunde + " h " + duration_stunde_minute + " min");
            }
        }
        //////////////////////////////////////////////WALK///////////////////////////////////////////////////
        TextView tvWalkDistance = this.findViewById(R.id.tvWalkDistance);
        Intent intent_WalkDistance = this.getIntent();
        if (intent_WalkDistance != null) {
            String distance = "0";
            String distance_meter = "0";
            distance = intent_WalkDistance.getStringExtra("Distanz_Walk");
            distance_meter = intent_WalkDistance.getStringExtra("Distanz_Walk_Meter");
            if (distance == "0") //dann ist es unter 1km = Anzeige in Meter
            {
                tvWalkDistance.setText(distance_meter + " m");
            }
            else
            {
                tvWalkDistance.setText(distance + " km");
            }
        }

        TextView tvWalkDuration = this.findViewById(R.id.tvWalkDuration);
        Intent intent_WalkDuration = this.getIntent();
        if (intent_WalkDuration != null) {
            String duration_stunde = "0";
            String duration_stunde_minute = "0";
            String duration_minute = "0";

            duration_stunde = intent_WalkDuration.getStringExtra("Dauer_Walk_Stunden");
            duration_stunde_minute = intent_WalkDuration.getStringExtra("Dauer_Walk_Stunden_Minuten");
            duration_minute = intent_WalkDuration.getStringExtra("Dauer_Walk_Minuten");

            if (duration_minute != "0") //dann ist es unter 1h = Anzeige in Minuten
            {
                tvWalkDuration.setText(duration_minute + " min");
            }
            else
            {
                tvWalkDuration.setText(duration_stunde + " h " + duration_stunde_minute + " min");
            }
        }

        btnBike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VarianteActivity.this, WegActivity.class);
                startActivity(intent);
            }
        });

        btnCar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VarianteActivity.this, WegActivity.class);
                startActivity(intent);
            }
        });

        btnWalk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VarianteActivity.this, WegActivity.class);
                startActivity(intent);
            }
        });

        btnBacktoEingabe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VarianteActivity.this, EingabeActivity.class);
                startActivity(intent);
            }
        });

        //Kartenserver von Herr Knopf
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
        };

        Permissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {//wenn alles okay ist wird die Karte angezeigt
                setupMapView();
                String string = "Karte sofort erstellt";
                Log.d("Karte",string);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {//Wenn Berechtigung nicht okay ist, wird die Karte nicht angezeigt
                super.onDenied(context, deniedPermissions);

                if (deniedPermissions.size() == 1)
                {
                    String permission = deniedPermissions.get(0);
                    if (permission.equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE))//darf auf den Speicher zugegriffen werden
                    {
                        setupMapView();
                        String string = "Karte nicht sofort erstellt";
                        Log.d("Karte", string);
                    }
                }
            }
        });

        //Zugriff auf den Kartenserver der von Herr Knopf bereitgestellt wird
        String authorizationString = this.getMapServerAuthorizationString("ws2223@hka", "LeevwBfDi#2027");//username und passwort
        Configuration.getInstance().getAdditionalHttpRequestProperties().put("Authorization", authorizationString);

        XYTileSource mapServer = new XYTileSource("MapServer", 8, 20, 256, ".png", new String[]{"https://www.mapserver.dev/styles/default/"});

        this.mapView = this.findViewById(R.id.mapView);
        this.mapView.setTileSource(mapServer);

        GeoPoint startPoint = new GeoPoint(49.0069, 8.4037);//Standard Startpunkt

        IMapController mapController = mapView.getController();
        mapController.setZoom(14.0);//Anfangswert Zoom
        mapController.setCenter(startPoint);//Wo ist die Mitte der Karte zu Beginn

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.mapView.onPause();
    }

    @SuppressLint("MissingPermission")
    private void setupMapView()
    {
        //Abfrage GPS - Koordinaten des Handys
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                GeoPoint startPoint = new GeoPoint(latitude, longitude);

                IMapController mapController = mapView.getController();
                mapController.setCenter(startPoint);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

    }

    private String getMapServerAuthorizationString(String username, String password)
    {
        String authorizationString = String.format("%s:%s", username, password);
        return "Basic " + Base64.encodeToString(authorizationString.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
    }
}