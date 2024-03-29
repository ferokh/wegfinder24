package de.info3.wegfinder24;


import static java.lang.Math.sqrt;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import java.util.ArrayList;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import android.widget.EditText;
public class EingabeActivity extends AppCompatActivity {
    MapView map = null;
    private MapView mapView;
    private LocationManager locationManager;
    private MyLocationNewOverlay locationOverlay;

    double Latitude;// = 49.000000;
    double Longitude;// = 8.000000;

    double Startlat=9999;
    double Startlong=9999;
    double Ziellat=9999;
    double Ziellong=9999;

    GeoPoint startPoint;
    GeoPoint endPoint;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eingabe);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //über den ArrowButton die Variate Activity öffnen


        //Definition der EditText-Felder für die Eingabe von Start und Ziel Lat/Long
        EditText edtStartMessagelat = this.findViewById(R.id.edtStartEnter);
        EditText edtStartMessagelong = this.findViewById(R.id.edtStartEnterLong);
        EditText edtZielMessagelat = this.findViewById(R.id.edtZielEnter);
        EditText edtZielMessagelong = this.findViewById(R.id.edtZielEnterLong);
        ImageButton btnGPSstart = this.findViewById(R.id.btnGPSstart);
        ImageButton btnGPSZiel = this.findViewById(R.id.btnGPSziel);

        Resources res = getResources();
        Drawable edtgrey= ResourcesCompat.getDrawable(res, R.drawable.edt_shape_grey, getTheme());
        Drawable edtwhite= ResourcesCompat.getDrawable(res, R.drawable.edt_shape, getTheme());
        Drawable btnprimary= ResourcesCompat.getDrawable(res, R.drawable.btn_round, getTheme());
        Drawable btnprimary_grey= ResourcesCompat.getDrawable(res, R.drawable.btn_round_grey, getTheme());
        int grey = ResourcesCompat.getColor(res, R.color.grey, getTheme());
        int black = ResourcesCompat.getColor(res, R.color.black, getTheme());

        //wenn man auf den Pfeil drückt wird die nächste Activity gestartet und die Eingabe übergeben
        ImageButton btnOpenVariante =this.findViewById(R.id.btnArrow);
        btnOpenVariante.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                /*double Startlat = 8.681495;
                double Startlong = 49.41461;
                double Ziellat = 8.687872;
                double Ziellong = 49.420318;*/
                Startlat = Double.parseDouble(edtStartMessagelat.getText().toString());
                Startlong = Double.parseDouble(edtStartMessagelong.getText().toString());
                Ziellat = Double.parseDouble(edtZielMessagelat.getText().toString());
                Ziellong = Double.parseDouble(edtZielMessagelong.getText().toString());
                if (Startlat == 9999 || Startlong==9999 || Ziellat == 9999 || Ziellong == 9999)
                {
                    Dialogshort();
                }
                else {
                    double dx = 71.5 * (Startlong - Ziellong);
                    double dy = 111.3 * (Startlat - Ziellat);
                    double distance = sqrt((dx * dx) + (dy * dy)); //Entfernung in km

                    if (distance > 200) {
                        Dialoglong();
                    } else {
                        Intent intent = new Intent(EingabeActivity.this, WartebildschirmActivity.class);
                        intent.putExtra("Startlat", Startlat);
                        intent.putExtra("Startlong", Startlong);
                        intent.putExtra("Ziellat", Ziellat);
                        intent.putExtra("Ziellong", Ziellong);
                        startActivity(intent);
                    }
                }
            }
        });
        //Permission def.
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

        btnGPSstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 0;
                //edtStartMessagelat.setBackground(edtwhite);
                //edtStartMessagelong.setBackground(edtwhite);
                edtStartMessagelat.setHintTextColor(black);
                edtStartMessagelong.setHintTextColor(black);
                edtStartMessagelat.setTextColor(black);
                edtStartMessagelong.setTextColor(black);

                //edtZielMessagelat.setBackground(edtgrey);
                //edtZielMessagelong.setBackground(edtgrey);
                edtZielMessagelat.setHintTextColor(grey);
                edtZielMessagelong.setHintTextColor(grey);
                edtZielMessagelat.setTextColor(grey);
                edtZielMessagelong.setTextColor(grey);

                btnGPSstart.setBackground(btnprimary);
                btnGPSZiel.setBackground(btnprimary_grey);

            }
        });

        btnGPSZiel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                index = 1 ;
                //edtStartMessagelat.setBackground(edtgrey);
                //edtStartMessagelong.setBackground(edtgrey);
                edtStartMessagelat.setHintTextColor(grey);
                edtStartMessagelong.setHintTextColor(grey);
                edtStartMessagelat.setTextColor(grey);
                edtStartMessagelong.setTextColor(grey);

                //edtZielMessagelat.setBackground(edtwhite);
                //edtZielMessagelong.setBackground(edtwhite);
                edtZielMessagelat.setHintTextColor(black);
                edtZielMessagelong.setHintTextColor(black);
                edtZielMessagelat.setTextColor(black);
                edtZielMessagelong.setTextColor(black);

                btnGPSstart.setBackground(btnprimary_grey);
                btnGPSZiel.setBackground(btnprimary);
            }
        });



        //Click on Map
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {

                if (index == 0) {
                    Double latitude = p.getLatitude();
                    latitude = round(latitude, 6);
                    Double longitude = p.getLongitude();
                    longitude = round(longitude, 6);

                    edtStartMessagelat.setText(Double.toString(latitude));
                    edtStartMessagelong.setText(Double.toString(longitude));
                    index = 1;

                    Marker startMarker = new Marker(map);
                    startMarker.setId("Start1");
                    startMarker.setPosition(p);
                    startMarker.setTitle("Start");
                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(startMarker);

                    mapView.getOverlays().remove(startMarker);
                    mapView.invalidate();

                    for (int i = 0; i < map.getOverlays().size(); i++) {
                        Overlay overlay = map.getOverlays().get(i);
                        if (overlay instanceof Marker && ((Marker) overlay).getId().equals("Start1")) {
                            map.getOverlays().remove(overlay);

                        }
                    }
                    startMarker.setPosition(p);
                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(startMarker);

                } else if (index == 1) {
                    Double latitude = p.getLatitude();
                    latitude = round(latitude, 6);
                    Double longitude = p.getLongitude();
                    longitude = round(longitude, 6);

                    edtZielMessagelat.setText(Double.toString(latitude));
                    edtZielMessagelong.setText(Double.toString(longitude));
                    index = 0;

                    Marker endMarker = new Marker(map);
                    endMarker.setId("End");
                    endMarker.setPosition(p);
                    endMarker.setTitle("Start");
                    endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(endMarker);

                    mapView.getOverlays().remove(endMarker);
                    mapView.invalidate();

                    for (int i = 0; i < map.getOverlays().size(); i++) {
                        Overlay overlay = map.getOverlays().get(i);
                        if (overlay instanceof Marker && ((Marker) overlay).getId().equals("End")) {
                            map.getOverlays().remove(overlay);

                        }
                    }
                    endMarker.setPosition(p);
                    endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(endMarker);


                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), mReceive);
        map.getOverlays().add(OverlayEvents);
        //Kompass
        CompassOverlay compassOverlay = new CompassOverlay(this, mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

       //Standort anzeigen lassen
        // Knopf die 2. GPS

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

    }



    @SuppressLint("MissingPermission")
    private void setupMapView()
    {
        //Abfrage GPS - Koordinaten des Handys
        LocationListener locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(android.location.Location location) {
                Latitude = location.getLatitude();
                Longitude = location.getLongitude();

                GeoPoint startPoint = new GeoPoint(Latitude, Longitude);

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
    private void Dialoglong(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);


        builder.setTitle("Distanz Fehler")
                //.setMessage("Anständige Navigation ist noch nicht verfügbar. Zahlen Sie einen Kaffee an den Entwickler um die App fertigzustellen.")
                .setMessage("Die gesetzten Punkte sind zu weit auseinander.\nEine adäquate Routenführung ist daher leider nicht möglich.\nWenn Sie fortfahren gibt es eine Fehlermeldung!\n\nDas Entwicklerteam")
                .setPositiveButton("trotzdem fortfahren", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(EingabeActivity.this, WartebildschirmActivity.class);
                        intent.putExtra("Startlat", Startlat);
                        intent.putExtra("Startlong", Startlong);
                        intent.putExtra("Ziellat", Ziellat);
                        intent.putExtra("Ziellong", Ziellong);
                        startActivity(intent);
                    }
                })
                .setNeutralButton("neue Eingabe", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(EingabeActivity.this, EingabeActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }
    private void Dialogshort(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);


        builder.setTitle("Du Schlingel")
                //.setMessage("Anständige Navigation ist noch nicht verfügbar. Zahlen Sie einen Kaffee an den Entwickler um die App fertigzustellen.")
                .setMessage("Mindest eine Koordinate hat den Wert 0 und liegt damit in in Deutschland.\nBitte Eingabe überprüfen!\n\nDas Entwicklerteam")
                .setNeutralButton("neue Eingabe", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(EingabeActivity.this, EingabeActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }
}
