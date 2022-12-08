package de.info3.wegfinder24;

import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EingabeActivity extends AppCompatActivity {

    public final static String MAPBOX_TOKEN ="pk.eyJ1IjoiYnJ1ZWNrdGkiLCJhIjoiY2xiYzBjNHI4MGI2NTNvcGV3OWI5aTJjciJ9.-ZRC7YTlnbSefuqY1u0TZg";

    private MapView mapView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, MAPBOX_TOKEN);
        setContentView(R.layout.activity_eingabe);

        /*ImageButton btnOpenVariante = this.findViewById(R.id.btnArrow);

         btnOpenVariante.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EingabeActivity.this, WartebildschirmActivity.class);
                startActivity(intent);
            }
        });*/



        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {}
                });
            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }





   /* public final static String MAPBOX_TOKEN="pk.eyJ1IjoiYnJ1ZWNrdGkiLCJhIjoiY2xiYzBjNHI4MGI2NTNvcGV3OWI5aTJjciJ9.-ZRC7YTlnbSefuqY1u0TZg";

    private LocationManager locationManager;
    MapView mapView;
    MapboxMap map;



    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, MAPBOX_TOKEN);
        setContentView(R.layout.activity_eingabe);
        initView();
        initPermissions();
        //über den ArrowButton die Variate Activity öffnen
        ImageButton btnOpenVariante = this.findViewById(R.id.btnArrow);

        btnOpenVariante.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EingabeActivity.this, WartebildschirmActivity.class);
                startActivity(intent);
            }
        });
    }







    protected void initView(){
        mapView=(MapView) findViewById(R.id.map);
    }

    protected void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            updateLocation();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            updateLocation();
        }
    }

    @SuppressLint("MissingPermission")
    protected void updateLocation(){
        mapView.getMapAsync(this::onMapReady);
    }

    protected void onMapReady(MapboxMap map) {
        this.map=map;
        map.setStyle(Style.SATELLITE_STREETS,this::onStyleReady);
    }

    @SuppressLint("MissingPermission")
    protected void onStyleReady(Style style) {
       LocationComponent locationComponent= map.getLocationComponent();
       locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, style).build());
       locationComponent.setLocationComponentEnabled(true);
       locationComponent.setCameraMode(CameraMode.TRACKING);
       locationComponent.zoomWhileTracking(10,3000);
       locationComponent.setRenderMode(RenderMode.COMPASS);
       locationComponent.tiltWhileTracking(45);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mapView.onStart();
    }
    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onStop(){
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    } */

}