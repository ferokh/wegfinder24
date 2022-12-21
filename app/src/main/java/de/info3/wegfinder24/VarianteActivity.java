package de.info3.wegfinder24;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.info3.wegfinder24.newtwork.JSON_Anfrage.Anfrage;

public class VarianteActivity extends AppCompatActivity {

    MapView map = null;

    private MapView mapView;
    private LocationManager locationManager;
    private MyLocationNewOverlay locationOverlay;

    //private LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_variante);

        //Definition Knöpfe
        Button btnBike =this.findViewById(R.id.btnBike);
        Button btnCar =this.findViewById(R.id.btnCar);
        Button btnWalk =this.findViewById(R.id.btnWalk);
        Button btnBacktoEingabe =this.findViewById(R.id.btnbacktoEingabe);

        Intent WBAintent = this.getIntent();
        Anfrage datencar = (Anfrage) WBAintent.getSerializableExtra("datencar");
        Anfrage datenbike = (Anfrage) WBAintent.getSerializableExtra("datenbike");
        Anfrage datenwalk = (Anfrage) WBAintent.getSerializableExtra("datenwalk");

        String BA = Double.toString(datencar.getMetadata().getQuery().getCoordinates().get(0).get(1));
        String LA = Double.toString(datencar.getMetadata().getQuery().getCoordinates().get(0).get(0));
        String BE = Double.toString(datencar.getMetadata().getQuery().getCoordinates().get(1).get(1));
        String LE = Double.toString(datencar.getMetadata().getQuery().getCoordinates().get(1).get(0));

        TextView tvStartDestVar = this.findViewById(R.id.tvStartDestinationVar);
        tvStartDestVar.setText("Start: " + BA + ", " + LA+"\n"+"Ziel: " + BE + ", " + LE);



        //////////////////////////////////////////////CAR///////////////////////////////////////////////////
        // Strecke anzeigen
        TextView tvCarDistance = this.findViewById(R.id.tvCarDistance); //TextView für die Entfernung - Auto
        double[] car_distance = distanz(datencar.getFeatures().get(0).getProperties().getSummary().getDistance());
        if (car_distance[1] == 0) {
            tvCarDistance.setText(car_distance[0] + " m"); //Anzeige mit Meter
        }
        else {
            tvCarDistance.setText(car_distance[0] + " km"); //Anzeige mit Kilometer
        }

        // Dauer anzeigen
        TextView tvCarDuration = this.findViewById(R.id.tvCarDuration);
        int[] car_duration = dauer(datencar.getFeatures().get(0).getProperties().getSummary().getDuration());
        if (car_duration[0] == 0) {  // Unterscheidung, wenn Zeit kürzer einer Stunde
            tvCarDuration.setText(car_duration[1] + " min");
        }
        else {
            tvCarDuration.setText(car_duration[0] + " h " + car_duration[1] + " min");
        }

        // Liste mit Waypoints erstellen
        Integer car_WayPoints_First_number = datencar.getFeatures().get(0).getProperties().getWayPoints().get(1);
        List<GeoPoint> car_WayPoints =new ArrayList<GeoPoint>();
        for (int i = 0; i<car_WayPoints_First_number + 1;i++)
        {
            car_WayPoints.add(new GeoPoint (datencar.getFeatures().get(0).getGeometry().getCoordinates().get(i).get(1),datencar.getFeatures().get(0).getGeometry().getCoordinates().get(i).get(0)));
        }

        //////////////////////////////////////////////BIKE///////////////////////////////////////////////////
        // Strecke anzeigen
        TextView tvBikeDistance = this.findViewById(R.id.tvBikeDistance); //TextView für die Entfernung - Fahrrad
        double[] bike_distance = distanz(datenbike.getFeatures().get(0).getProperties().getSummary().getDistance());
        if (bike_distance[1] == 0) {
            tvBikeDistance.setText(bike_distance[0] + " m"); //Anzeige mit Meter
        }
        else {
            tvBikeDistance.setText(bike_distance[0] + " km"); //Anzeige mit Kilometer
        }

        // Dauer anzeigen
        TextView tvBikeDuration = this.findViewById(R.id.tvBikeDuration);
        int[] bike_duration = dauer(datenbike.getFeatures().get(0).getProperties().getSummary().getDuration());
        if (bike_duration[0] == 0) {  // Unterscheidung, wenn Zeit kürzer einer Stunde
            tvBikeDuration.setText(bike_duration[1] + " min");
        }
        else {
            tvBikeDuration.setText(bike_duration[0] + " h " + bike_duration[1] + " min");
        }

        // Liste mit Waypoints erstellen
        Integer bike_WayPoints_First_number = datenbike.getFeatures().get(0).getProperties().getWayPoints().get(1);
        List<GeoPoint> bike_WayPoints =new ArrayList<GeoPoint>();
        for (int i = 0; i<bike_WayPoints_First_number + 1;i++)
        {
            bike_WayPoints.add(new GeoPoint (datenbike.getFeatures().get(0).getGeometry().getCoordinates().get(i).get(1),datenbike.getFeatures().get(0).getGeometry().getCoordinates().get(i).get(0)));
        }

        //////////////////////////////////////////////WALK///////////////////////////////////////////////////
        // Strecke anzeigen
        TextView tvWalkDistance = this.findViewById(R.id.tvWalkDistance); //TextView für die Entfernung - zu Fuß
        double[] walk_distance = distanz(datenwalk.getFeatures().get(0).getProperties().getSummary().getDistance());
        if (walk_distance[1] == 0) {
            tvWalkDistance.setText(walk_distance[0] + " m"); //Anzeige mit Meter
        }
        else {
            tvWalkDistance.setText(walk_distance[0] + " km"); //Anzeige mit Kilometer
        }

        // Dauer anzeigen
        TextView tvWalkDuration = this.findViewById(R.id.tvWalkDuration);
        int[] walk_duration = dauer(datenwalk.getFeatures().get(0).getProperties().getSummary().getDuration());
        if (walk_duration[0] == 0) {  // Unterscheidung, wenn Zeit kürzer einer Stunde
            tvWalkDuration.setText(walk_duration[1] + " min");
        }
        else {
            tvWalkDuration.setText(walk_duration[0] + " h " + walk_duration[1] + " min");
        }

        // Liste mit Waypoints erstellen
        Integer walk_WayPoints_First_number = datenwalk.getFeatures().get(0).getProperties().getWayPoints().get(1);
        List<GeoPoint> walk_WayPoints =new ArrayList<GeoPoint>();
        for (int i = 0; i<walk_WayPoints_First_number + 1;i++)
        {
            walk_WayPoints.add(new GeoPoint (datenwalk.getFeatures().get(0).getGeometry().getCoordinates().get(i).get(1),datenwalk.getFeatures().get(0).getGeometry().getCoordinates().get(i).get(0)));
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        //WegActivity Starten von BikeKnopf
        btnBike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VarianteActivity.this, WegActivity.class);
                intent.putExtra("daten", (Serializable) datenbike);
                startActivity(intent);
            }
        });

        //WegActivity Starten von AutoKnopf
        btnCar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VarianteActivity.this, WegActivity.class);
                intent.putExtra("daten", (Serializable) datencar);
                startActivity(intent);
            }
        });

        //WegActivity Starten von WalkKnopf
        btnWalk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VarianteActivity.this, WegActivity.class);
                intent.putExtra("daten", (Serializable) datenwalk);
                startActivity(intent);
            }
        });

        //Zurück zur EingabeActivity
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
        };

        Permissions.check(this, permissions, null, null, new PermissionHandler() {  //Kontrolle der Berechtigungen
            @Override
            public void onGranted() {//wenn alles okay ist wird die Karte angezeigt
                setupMapView();
                String string = "Karte sofort erstellt";
                Log.d("Karte",string);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) { //Wenn Berechtigung nicht okay ist, wird die Karte nicht angezeigt
                super.onDenied(context, deniedPermissions);

                if (deniedPermissions.size() == 1)
                {
                    String permission = deniedPermissions.get(0);
                    if (permission.equals(Manifest.permission.MANAGE_EXTERNAL_STORAGE)) //darf auf den Speicher zugegriffen werden
                    {
                        setupMapView();
                        String string = "Karte nicht sofort erstellt";
                        Log.d("Karte", string);
                    }
                }
            }
        });

        //Map anzeigen
        map = (MapView) findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        mapView = map;


        //Kompass
        CompassOverlay compassOverlay = new CompassOverlay(this, mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        //Standort anzeigen lassen
        GpsMyLocationProvider provider = new GpsMyLocationProvider(this);
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        locationOverlay = new MyLocationNewOverlay(provider, mapView);
        locationOverlay.enableFollowLocation();
        locationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                Log.d("MyTag", String.format("First location fix: %s", locationOverlay.getLastFix()));
            }
        });
        mapView.getOverlayManager().add(locationOverlay);

        /*
        //Marker setzen
        GeoPoint startPoint=new GeoPoint(48.13,-1.63);
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        mapController.setCenter(startPoint);

        Marker startMarker=new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);*/

        //Polygon mit Array anzeigen lassen
        ////////////////////CAR///////////////////////
        Polyline line_car = new Polyline(mapView);
        line_car.setWidth(4f);
        line_car.setColor(Color.RED);

        line_car.setPoints(car_WayPoints);
        line_car.setGeodesic(true);
        mapView.getOverlayManager().add(line_car);
        mapView.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);

        ////////////////////BIKE///////////////////////
        Polyline line_bike = new Polyline(mapView);
        line_bike.setWidth(4f);
        line_bike.setColor(Color.CYAN);

        line_bike.setPoints(bike_WayPoints);
        line_bike.setGeodesic(true);
        mapView.getOverlayManager().add(line_bike);
        mapView.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);

        ////////////////////WALK///////////////////////
        Polyline line_walk = new Polyline(mapView);
        line_walk.setWidth(4f);
        line_walk.setColor(Color.GREEN);

        line_walk.setPoints(walk_WayPoints);
        line_walk.setGeodesic(true);
        mapView.getOverlayManager().add(line_walk);
        mapView.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);


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
                mapController.setZoom(15);
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

    @Override
    protected void onResume() {
        super.onResume();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        locationOverlay.enableMyLocation();
        this.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationOverlay.disableMyLocation();
        this.mapView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mapView.onPause();
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
