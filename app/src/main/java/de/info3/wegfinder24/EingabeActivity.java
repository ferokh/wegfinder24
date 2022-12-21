package de.info3.wegfinder24;


import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import java.util.ArrayList;
import java.util.List;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import android.widget.EditText;
import android.widget.Toast;


public class EingabeActivity extends AppCompatActivity {


    MapView map = null;

    private MapView mapView;
    private LocationManager locationManager;
    private MyLocationNewOverlay locationOverlay;

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


        btnGPSZiel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btnStartAuswahl.setActivated(true);
                //btnZielAuswahl.setActivated(false);
                /*btnZielAuswahl.setEnabled(false);
                btnStartAuswahl.setEnabled(true);
                btnZielAuswahl.setVisibility(View.GONE);
                btnStartAuswahl.setVisibility(View.VISIBLE);*/

                index = 1;

                /*edtStartMessagelat.setTextColor(getResources().getColor(R.color.primary));
                edtStartMessagelong.setTextColor(getResources().getColor(R.color.primary));
                edtZielMessagelat.setTextColor(getResources().getColor(R.color.black));
                edtZielMessagelong.setTextColor(getResources().getColor(R.color.black));
                edtStartMessagelat.setHintTextColor(getResources().getColor(R.color.primary));
                edtStartMessagelong.setHintTextColor(getResources().getColor(R.color.primary));
                edtZielMessagelat.setHintTextColor(getResources().getColor(R.color.black));
                edtZielMessagelong.setHintTextColor(getResources().getColor(R.color.black));*/
            }
        });
        btnGPSstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btnZielAuswahl.setActivated(true);
                //btnStartAuswahl.setActivated(false);
                /*btnZielAuswahl.setEnabled(true);
                btnStartAuswahl.setEnabled(false);
                btnZielAuswahl.setVisibility(View.VISIBLE);
                btnStartAuswahl.setVisibility(View.GONE);*/

                index = 0;
                /*edtStartMessagelat.setTextColor(getResources().getColor(R.color.black));
                edtStartMessagelong.setTextColor(getResources().getColor(R.color.black));
                edtZielMessagelat.setTextColor(getResources().getColor(R.color.grey));
                edtZielMessagelong.setTextColor(getResources().getColor(R.color.grey));
                edtStartMessagelat.setHintTextColor(getResources().getColor(R.color.black));
                edtStartMessagelong.setHintTextColor(getResources().getColor(R.color.black));
                edtZielMessagelat.setHintTextColor(getResources().getColor(R.color.grey));
                edtZielMessagelong.setHintTextColor(getResources().getColor(R.color.grey));*/
            }
        });

        if (index == 0)
        {
            btnGPSZiel.setEnabled(true);
            btnGPSstart.setEnabled(false);
            btnGPSZiel.setVisibility(View.VISIBLE);
            btnGPSstart.setVisibility(View.GONE);

            edtStartMessagelat.setTextColor(getResources().getColor(R.color.black));
            edtStartMessagelong.setTextColor(getResources().getColor(R.color.black));
            edtZielMessagelat.setTextColor(getResources().getColor(R.color.grey));
            edtZielMessagelong.setTextColor(getResources().getColor(R.color.grey));
            edtStartMessagelat.setHintTextColor(getResources().getColor(R.color.black));
            edtStartMessagelong.setHintTextColor(getResources().getColor(R.color.black));
            edtZielMessagelat.setHintTextColor(getResources().getColor(R.color.grey));
            edtZielMessagelong.setHintTextColor(getResources().getColor(R.color.grey));
        }


        if (index == 1)
        {
            btnGPSZiel.setEnabled(false);
            btnGPSstart.setEnabled(true);
            btnGPSZiel.setVisibility(View.GONE);
            btnGPSstart.setVisibility(View.VISIBLE);

            edtStartMessagelat.setTextColor(getResources().getColor(R.color.primary));
            edtStartMessagelong.setTextColor(getResources().getColor(R.color.primary));
            edtZielMessagelat.setTextColor(getResources().getColor(R.color.black));
            edtZielMessagelong.setTextColor(getResources().getColor(R.color.black));
            edtStartMessagelat.setHintTextColor(getResources().getColor(R.color.primary));
            edtStartMessagelong.setHintTextColor(getResources().getColor(R.color.primary));
            edtZielMessagelat.setHintTextColor(getResources().getColor(R.color.black));
            edtZielMessagelong.setHintTextColor(getResources().getColor(R.color.black));
        }
        //Click on Map
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                //Toast.makeText(getBaseContext(),p.getLatitude() + " - "+p.getLongitude(),Toast.LENGTH_LONG).show();
                Double latitude = p.getLatitude();
                latitude = round(latitude,6);
                Double longitude = p.getLongitude();
                longitude = round(longitude,6);
                if (index == 0)
                {
                    edtStartMessagelat.setText(Double.toString(latitude));
                    edtStartMessagelong.setText(Double.toString(longitude));
                }
                else if (index == 1)
                {
                    edtZielMessagelat.setText(Double.toString(latitude));
                    edtZielMessagelong.setText(Double.toString(longitude));
                }
                IMapController mapController = map.getController();
                //mapController.setZoom(9);
                //mapController.setCenter(startPoint);

                Marker startMarker=new Marker(map);
                startMarker.setPosition(p);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);

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

        //wenn man auf den Pfeil drückt wird die nächste Activity gestartet und die Eingabe übergeben
        ImageButton btnOpenVariante =this.findViewById(R.id.btnArrow);
        btnOpenVariante.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                /*double Startlat = 8.681495;
                double Startlong = 49.41461;
                double Ziellat = 8.687872;
                double Ziellong = 49.420318;*/
                double Startlat = Double.parseDouble(edtStartMessagelat.getText().toString());
                double Startlong = Double.parseDouble(edtStartMessagelong.getText().toString());
                double Ziellat = Double.parseDouble(edtZielMessagelat.getText().toString());
                double Ziellong = Double.parseDouble(edtZielMessagelong.getText().toString());


                Intent intent = new Intent(EingabeActivity.this, WartebildschirmActivity.class);
                intent.putExtra("Startlat", Startlat);
                intent.putExtra("Startlong", Startlong);
                intent.putExtra("Ziellat",Ziellat);
                intent.putExtra("Ziellong",Ziellong);
                startActivity(intent);
            }
        });

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
}
