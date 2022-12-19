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
import java.util.List;

import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;

public class WegActivity extends AppCompatActivity {

    private MapView mapView;

    private LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weg);

        Button btnWeg1 =this.findViewById(R.id.btnWeg1);
        Button btnWeg2 =this.findViewById(R.id.btnWeg2);
        Button btnWeg3 =this.findViewById(R.id.btnWeg3);
        Button btnBacktoEingabe = this.findViewById(R.id.btnbacktoEingabe);
        ImageButton ibtnBacktoVariante =this.findViewById(R.id.ibtnbacktoVariante);

        Intent VAintent = this.getIntent();
        Anfrage daten = (Anfrage) VAintent.getSerializableExtra("daten");

        String BA = Double.toString(daten.getMetadata().getQuery().getCoordinates().get(0).get(1));
        String LA = Double.toString(daten.getMetadata().getQuery().getCoordinates().get(0).get(0));
        String BE = Double.toString(daten.getMetadata().getQuery().getCoordinates().get(1).get(1));
        String LE = Double.toString(daten.getMetadata().getQuery().getCoordinates().get(1).get(0));

        TextView tvStartDestVar = this.findViewById(R.id.tvStartDestinationWeg);
        tvStartDestVar.setText("Start: " + BA + ", " + LA+"\n"+"Ziel: " + BE + ", " + LE);

//////////////////////////////////////////////WEG1///////////////////////////////////////////////////
        // Strecke anzeigen
        TextView tvWeg1Distance = this.findViewById(R.id.tvWeg1Distance); //TextView für die Entfernung - Auto
        double[] weg1_distance = distanz(daten.getFeatures().get(0).getProperties().getSummary().getDistance());
        if (weg1_distance[1] == 0) {
            tvWeg1Distance.setText(weg1_distance[0] + " m"); //Anzeige mit Meter
        }
        else {
            tvWeg1Distance.setText(weg1_distance[0] + " km"); //Anzeige mit Kilometer
        }

        // Dauer anzeigen
        TextView tvWeg1Duration = this.findViewById(R.id.tvWeg1Duration);
        int[] weg1_duration = dauer(daten.getFeatures().get(0).getProperties().getSummary().getDuration());
        if (weg1_duration[0] == 0) {  // Unterscheidung, wenn Zeit kürzer einer Stunde
            tvWeg1Duration.setText(weg1_duration[1] + " min");
        }
        else {
            tvWeg1Duration.setText(weg1_duration[0] + " h " + weg1_duration[1] + " min");
        }

        // Liste mit Waypoints erstellen
        Integer weg1_WayPoints_First_number = daten.getFeatures().get(0).getProperties().getWayPoints().get(1);
        List<GeoPoint> weg1_WayPoints =new ArrayList<GeoPoint>();
        for (int i = 0; i<weg1_WayPoints_First_number + 1;i++)
        {
            weg1_WayPoints.add(new GeoPoint (daten.getFeatures().get(0).getGeometry().getCoordinates().get(i).get(1),daten.getFeatures().get(0).getGeometry().getCoordinates().get(i).get(0)));
        }

        //////////////////////////////////////////////weg2///////////////////////////////////////////////////
        // Strecke anzeigen
        TextView tvWeg2Distance = this.findViewById(R.id.tvWeg2Distance); //TextView für die Entfernung - Fahrrad
        double[] weg2_distance = distanz(daten.getFeatures().get(1).getProperties().getSummary().getDistance());
        if (weg2_distance[1] == 0) {
            tvWeg2Distance.setText(weg2_distance[0] + " m"); //Anzeige mit Meter
        }
        else {
            tvWeg2Distance.setText(weg2_distance[0] + " km"); //Anzeige mit Kilometer
        }

        // Dauer anzeigen
        TextView tvWeg2Duration = this.findViewById(R.id.tvWeg2Duration);
        int[] weg2_duration = dauer(daten.getFeatures().get(1).getProperties().getSummary().getDuration());
        if (weg2_duration[0] == 0) {  // Unterscheidung, wenn Zeit kürzer einer Stunde
            tvWeg2Duration.setText(weg2_duration[1] + " min");
        }
        else {
            tvWeg2Duration.setText(weg2_duration[0] + " h " + weg2_duration[1] + " min");
        }

        // Liste mit Waypoints erstellen
        Integer weg2_WayPoints_First_number = daten.getFeatures().get(1).getProperties().getWayPoints().get(1);
        List<GeoPoint> weg2_WayPoints =new ArrayList<GeoPoint>();
        for (int i = 0; i< (weg2_WayPoints_First_number + 1);i++)
        {
            weg2_WayPoints.add(new GeoPoint (daten.getFeatures().get(1).getGeometry().getCoordinates().get(i).get(1),daten.getFeatures().get(1).getGeometry().getCoordinates().get(i).get(0)));
        }

        //////////////////////////////////////////////weg3///////////////////////////////////////////////////
        // Strecke anzeigen
        TextView tvWeg3Distance = this.findViewById(R.id.tvWeg3Distance); //TextView für die Entfernung - zu Fuß
        double[] weg3_distance = distanz(daten.getFeatures().get(2).getProperties().getSummary().getDistance());
        if (weg3_distance[1] == 0) {
            tvWeg3Distance.setText(weg3_distance[0] + " m"); //Anzeige mit Meter
        }
        else {
            tvWeg3Distance.setText(weg3_distance[0] + " km"); //Anzeige mit Kilometer
        }

        // Dauer anzeigen
        TextView tvWeg3Duration = this.findViewById(R.id.tvWeg3Duration);
        int[] weg3_duration = dauer(daten.getFeatures().get(2).getProperties().getSummary().getDuration());
        if (weg3_duration[0] == 0) {  // Unterscheidung, wenn Zeit kürzer einer Stunde
            tvWeg3Duration.setText(weg3_duration[1] + " min");
        }
        else {
            tvWeg3Duration.setText(weg3_duration[0] + " h " + weg3_duration[1] + " min");
        }

        // Liste mit Waypoints erstellen
        Integer weg3_WayPoints_First_number = daten.getFeatures().get(2).getProperties().getWayPoints().get(1);
        List<GeoPoint> weg3_WayPoints =new ArrayList<GeoPoint>();
        for (int i = 0; i < weg3_WayPoints_First_number + 1;i++)
        {
            weg3_WayPoints.add(new GeoPoint (daten.getFeatures().get(2).getGeometry().getCoordinates().get(i).get(1),daten.getFeatures().get(2).getGeometry().getCoordinates().get(i).get(0)));
        }

        ////////////////////////////////////////////////////////////////////////////////////////////



        btnWeg1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WegActivity.this, WegActivity.class);
                startActivity(intent);
            }
        });

        btnWeg2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WegActivity.this, WegActivity.class);
                startActivity(intent);
            }
        });

        btnWeg3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WegActivity.this, WegActivity.class);
                startActivity(intent);
            }
        });

        btnBacktoEingabe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WegActivity.this, EingabeActivity.class);
                startActivity(intent);
            }
        });

        ibtnBacktoVariante.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WegActivity.this, VarianteActivity.class);
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
        mapController.setZoom(17.0);//Anfangswert Zoom
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

    private double round (double value, int decimalPoints)
    {
        double d = Math.pow(10,decimalPoints);
        return Math.round(value * d)/d;
    }

    private double[] distanz (double Distanz)
    {
        if(Distanz < 1000)
        {
            Distanz = round( Distanz / 10,0);
            return new double [] {Distanz * 10, 0};
        }
        else
        {
            Distanz = round(Distanz / 1000,1);
            return new double[] {Distanz,1};
        }
    }

    private int[] dauer(double Dauer)
    {
        Dauer = Dauer / 60;
        Dauer = round(Dauer,0);

        int minuten = (int) Dauer;
        int stunden = 0;

        while (minuten>59)
        {
            minuten = minuten - 60;
            stunden = stunden + 1;
        }
        return new int[] {stunden,minuten};

    }
}