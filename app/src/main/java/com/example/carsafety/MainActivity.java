package com.example.carsafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.anastr.speedviewlib.SpeedView;

import java.math.BigDecimal;

import in.unicodelabs.kdgaugeview.KdGaugeView;

public class MainActivity extends AppCompatActivity implements GPSCallback {
    private GPSManager gpsManager = null;
    private double speed = 0.0;
    Boolean isGPSEnabled=false;
    LocationManager locationManager;
    double currentSpeed,kmphSpeed;
    TextView txtview;
    KdGaugeView speedoMeterView;
    //SpeedView speedView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtview=findViewById(R.id.info);
        speedoMeterView = findViewById(R.id.speedMeter);
      //  speedView = (SpeedView) findViewById(R.id.speedView);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getCurrentSpeed(View view){
        txtview.setText(getString(R.string.info));
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        gpsManager = new GPSManager(MainActivity.this);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled) {
            gpsManager.startListening(getApplicationContext());
            gpsManager.setGPSCallback(this);
        } else {
            gpsManager.showSettingsAlert();
        }
    }

    public void onGPSUpdate(Location location) {
        speed = location.getSpeed();
        currentSpeed = round(speed,3, BigDecimal.ROUND_HALF_UP);
        kmphSpeed = round((currentSpeed*3.6),3,BigDecimal.ROUND_HALF_UP);
        txtview.setText(kmphSpeed+"km/h");
        speedoMeterView.setSpeed((float) kmphSpeed);
       // speedView.speedTo((int) kmphSpeed);

    }

    @Override
    protected void onDestroy() {
      //  gpsManager.stopListening();
      //  gpsManager.setGPSCallback(null);
       // gpsManager = null;
        super.onDestroy();
    }

    public static double round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putFloat("KMPH", (float) kmphSpeed);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
         float Speed = savedInstanceState.getFloat("KMPH",0.0f);
        txtview.setText(Speed+"km/h");
        speedoMeterView.setSpeed(Speed);
        txtview.setText(getString(R.string.info));
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        gpsManager = new GPSManager(MainActivity.this);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled) {
            gpsManager.startListening(getApplicationContext());
            gpsManager.setGPSCallback(this);
        } else {
            gpsManager.showSettingsAlert();
        }
    }
}

