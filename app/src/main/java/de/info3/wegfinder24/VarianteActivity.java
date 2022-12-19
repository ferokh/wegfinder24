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
        String BA = WBAintent.getStringExtra("Startlat");
        String LA = WBAintent.getStringExtra("Startlong");
        String BE = WBAintent.getStringExtra("Ziellat");
        String LE = WBAintent.getStringExtra("Ziellong");

        TextView tvStartDestVar = this.findViewById(R.id.tvStartDestinationVar);
        tvStartDestVar.setText("Start: " + BA + ", " + LA+"\n"+"Ziel: " + BE + ", " + LE);



        //////////////////////////////////////////////CAR///////////////////////////////////////////////////
        TextView tvCarDistance = this.findViewById(R.id.tvCarDistance); //TextView für die Entfernung - Auto
        if (WBAintent != null) {
            String distance = "0";
            String distance_meter = "0";
            distance = WBAintent.getStringExtra("Distanz_Car"); //Übergabe der Werte hier, wenn länger als 1km
            distance_meter = WBAintent.getStringExtra("Distanz_Car_Meter"); //Übergabe der Werte hier, wenn kürzer als 1km

            if (distance == "0") //dann ist es unter 1km = Anzeige in Meter
            {
                tvCarDistance.setText(distance_meter + " m"); //Anzeige mit Meter
            }
            else
            {
                tvCarDistance.setText(distance + " km"); //Anzeige mit Kilometer
            }
        }
        
        Anfrage datencar = (Anfrage) WBAintent.getSerializableExtra("car_WP");
        //Log.i("Lat", Double.toString(datencar.getFeatures().get(0).getGeometry().getCoordinates().get(0).get(0)));
        //Log.i("Long", Double.toString(datencar.getFeatures().get(0).getGeometry().getCoordinates().get(0).get(1)));


        Integer car_WayPoints_First_number = datencar.getFeatures().get(0).getProperties().getWayPoints().get(1);

        List<GeoPoint> car_WayPoints =new ArrayList<GeoPoint>();

        for (int i = 0; i<car_WayPoints_First_number;i++)
        {
            car_WayPoints.add(new GeoPoint (datencar.getFeatures().get(0).getGeometry().getCoordinates().get(i).get(1),datencar.getFeatures().get(0).getGeometry().getCoordinates().get(i).get(0)));
        }

        TextView tvCarDuration = this.findViewById(R.id.tvCarDuration);
        if (WBAintent != null) {
            String duration_stunde = "0";           //Format 00h 00min - hier die Stunden
            String duration_stunde_minute = "0";    //Format 00h 00min - hier die Minuten
            String duration_minute = "0";           //Minuten wenn unter 1h

            duration_stunde = WBAintent.getStringExtra("Dauer_Car_Stunden");
            duration_stunde_minute = WBAintent.getStringExtra("Dauer_Car_Stunden_Minuten");
            duration_minute = WBAintent.getStringExtra("Dauer_Car_Minuten");

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
        TextView tvBikeDistance = this.findViewById(R.id.tvBikeDistance); //TextView für die Entfernung - Fahrrad
        if (WBAintent != null) {
            String distance = "0";
            String distance_meter = "0";
            distance = WBAintent.getStringExtra("Distanz_Bike");//Übergabe der Werte hier, wenn länger als 1km
            distance_meter = WBAintent.getStringExtra("Distanz_Bike_Meter");//Übergabe der Werte hier, wenn kürzer als 1km
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
        if (WBAintent != null) {
            String duration_stunde = "0";
            String duration_stunde_minute = "0";
            String duration_minute = "0";

            duration_stunde = WBAintent.getStringExtra("Dauer_Bike_Stunden");
            duration_stunde_minute = WBAintent.getStringExtra("Dauer_Bike_Stunden_Minuten");
            duration_minute = WBAintent.getStringExtra("Dauer_Bike_Minuten");

            if (duration_minute != "0") //dann ist es unter 1h = Anzeige in Minuten
            {
                tvBikeDuration.setText(duration_minute + " min");
            }
            else
            {
                tvBikeDuration.setText(duration_stunde + " h " + duration_stunde_minute + " min");
            }
        }

        Integer bike_WayPoints_First_number = datencar.getFeatures().get(1).getProperties().getWayPoints().get(1);

        List<GeoPoint> bike_WayPoints =new ArrayList<GeoPoint>();

        for (int i = 0; i<bike_WayPoints_First_number;i++)
        {
            bike_WayPoints.add(new GeoPoint (datencar.getFeatures().get(1).getGeometry().getCoordinates().get(i).get(1),datencar.getFeatures().get(1).getGeometry().getCoordinates().get(i).get(0)));
        }

        //////////////////////////////////////////////WALK///////////////////////////////////////////////////
        TextView tvWalkDistance = this.findViewById(R.id.tvWalkDistance); //TextView für die Entfernung - zu Fuß
        if (WBAintent != null) {
            String distance = "0";
            String distance_meter = "0";
            distance = WBAintent.getStringExtra("Distanz_Walk");//Übergabe der Werte hier, wenn länger als 1km
            distance_meter = WBAintent.getStringExtra("Distanz_Walk_Meter");//Übergabe der Werte hier, wenn kürzer als 1km
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
        if (WBAintent != null) {
            String duration_stunde = "0";
            String duration_stunde_minute = "0";
            String duration_minute = "0";

            duration_stunde = WBAintent.getStringExtra("Dauer_Walk_Stunden");
            duration_stunde_minute = WBAintent.getStringExtra("Dauer_Walk_Stunden_Minuten");
            duration_minute = WBAintent.getStringExtra("Dauer_Walk_Minuten");

            if (duration_minute != "0") //dann ist es unter 1h = Anzeige in Minuten
            {
                tvWalkDuration.setText(duration_minute + " min");
            }
            else
            {
                tvWalkDuration.setText(duration_stunde + " h " + duration_stunde_minute + " min");
            }
        }

        Integer walk_WayPoints_First_number = datencar.getFeatures().get(2).getProperties().getWayPoints().get(1);

        List<GeoPoint> walk_WayPoints =new ArrayList<GeoPoint>();

        for (int i = 0; i<walk_WayPoints_First_number;i++)
        {
            walk_WayPoints.add(new GeoPoint (datencar.getFeatures().get(2).getGeometry().getCoordinates().get(i).get(1),datencar.getFeatures().get(2).getGeometry().getCoordinates().get(i).get(0)));
        }

        //WegActivity Starten von BikeKnopf
        btnBike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VarianteActivity.this, WegActivity.class);
                startActivity(intent);
            }
        });

        //WegActivity Starten von AutoKnopf
        btnCar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VarianteActivity.this, WegActivity.class);
                startActivity(intent);
            }
        });

        //WegActivity Starten von WalkKnopf
        btnWalk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VarianteActivity.this, WegActivity.class);
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
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
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

       /* //Marker setzen
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
        line_bike.setColor(Color.BLUE);

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
}

/*
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
    // Wenn man auf den standard Zurück-Knopf drückt, kommmt man zur EingabeAtivity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent EingabeA = new Intent(VarianteActivity.this, EingabeActivity.class);
        startActivity(EingabeA);
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
}*/