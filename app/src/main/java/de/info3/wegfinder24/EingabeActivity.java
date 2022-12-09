package de.info3.wegfinder24;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;

import java.util.List;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class EingabeActivity extends AppCompatActivity implements PermissionsListener,OnMapReadyCallback {

    public final static String MAPBOX_TOKEN = "pk.eyJ1IjoiYnJ1ZWNrdGkiLCJhIjoiY2xiYzBjNHI4MGI2NTNvcGV3OWI5aTJjciJ9.-ZRC7YTlnbSefuqY1u0TZg";

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private LocationComponent locationComponent;
    private boolean granted;
    public ImageButton centerloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, MAPBOX_TOKEN);
        setContentView(R.layout.activity_eingabe);

        // knöpfle GPS
        centerloc= (ImageButton) findViewById(R.id.centerloc);


        //Button KNÖPFLE weiter
        ImageButton btnOpenVariante = this.findViewById(R.id.btnArrow);
        btnOpenVariante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EingabeActivity.this, WartebildschirmActivity.class);
                startActivity(intent);
            }
        });

        //KNÖPFLE ende

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // knöpfle GPS
        centerloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition cameraPosition=new CameraPosition.Builder().target(new LatLng(locationComponent.getLastKnownLocation().getLatitude(),locationComponent.getLastKnownLocation().getLongitude())).zoom(12).build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),500);
            }
        });

    }
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap= mapboxMap;
        mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                enaleCommponent(style);
            }


        });
    }

    @SuppressLint("SuspiciousIndentation")
    public void enaleCommponent(@NonNull Style loadedMapStyle){
        try {
            if (PermissionsManager.areLocationPermissionsGranted(this)) {
                locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.zoomWhileTracking(14,3000);
                locationComponent.setCameraMode(CameraMode.TRACKING);
                locationComponent.setRenderMode(RenderMode.COMPASS);
                locationComponent.tiltWhileTracking(45);
            }else{
                permissionsManager= new PermissionsManager(this);
                permissionsManager.requestLocationPermissions(this);
            }
        }
        catch (Exception e){
            Log.e("ERR_LOAD_MAP",e.getMessage());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enaleCommponent(style);
                }
            });
        }else {
            Toast.makeText(this,"permission not grandet",Toast.LENGTH_LONG).show();
            finish();
        }


    }



}

