package com.example.roadsafetyapplication.ui.ride.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadsafetyapplication.R;
import com.github.anastr.speedviewlib.AwesomeSpeedometer;
import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.gms.maps.GoogleMap;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/*

public class MapActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION=1;
    private static final int RESULT_CODE = 11;
    TextView tv_speed;
    TextView tv_overspeed;

    MyReceiver myReceiver;

    private int speedlimit_value=0;
    SeekBar seekBar;
    TextView speedLimit;
    private GoogleMap mMap;
    SpeedView speedometer;
    AwesomeSpeedometer awesomeSpeedometer;
    GifDrawable gif;
    GifImageView gif1;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        tv_speed=findViewById(R.id.tv_speed);
        tv_overspeed=findViewById(R.id.tv_overspeed);
        seekBar =findViewById(R.id.seekBar);
        speedLimit=findViewById(R.id.tv_currentspeedlimit);
//        speedometer = findViewById(R.id.speedView);
        awesomeSpeedometer=findViewById(R.id.awesomeSpeedometer);
//        speedometer.setMaxSpeed(250);
        awesomeSpeedometer.setMaxSpeed(250);
        gif1=findViewById(R.id.gif);

        gif1.setVisibility(View.INVISIBLE);
        tv_overspeed.setVisibility(View.INVISIBLE);

//        event listener for seekbar

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedLimit.setText(progress+50+" km/hr");
                speedlimit_value=progress+50;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


////        adding map
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//


        findViewById(R.id.bt_startlocationupdate).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
        else
        {
            startLocationService();
        }
    }
  }
        );

        findViewById(R.id.bt_stoplocationupdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationService();
            }
        });


    }

    @Override
    protected void onStart() {
        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocationService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode== REQUEST_CODE_LOCATION_PERMISSION && grantResults.length >0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }
            else{
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;

    }

    private void startLocationService(){

        if(!isLocationServiceRunning()){
            Intent intent=new Intent(getApplicationContext(),LocationService.class);

            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this,"Location service started",Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationService(){

        if(isLocationServiceRunning()){
            Intent intent=new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this,"Location service stopped",Toast.LENGTH_SHORT).show();
        }
    }


    private class MyReceiver extends BroadcastReceiver {

        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context arg0, Intent arg1) {

            double speed = arg1.getDoubleExtra("SPEED", 0);
            double latitude = arg1.getDoubleExtra("LATITUDE", 0);
            double longitude= arg1.getDoubleExtra("LONGITUDE",0);

            speed=speed*3.6;

            String ans=String.format("%.2f", speed);

//            speedometer.speedTo((float) speed, 2000);
            awesomeSpeedometer.speedTo((int) speed,2000);

//            if(speed>=0)
//            {
//                tv_speed.setText(ans+" km/hr");
//            }
//            else
//            {
//                tv_speed.setText("0-- km/hr");
//            }

            if(speed>speedlimit_value){

                tv_overspeed.setText("SPEED LIMIT REACHED!!! DANGEROUS");
                    tv_overspeed.setVisibility(View.VISIBLE);
                    gif1.setVisibility(View.VISIBLE);


//              Addding music
                Uri uri = Uri.parse("android.resource://"+getPackageName()+"/raw/armani");
                mp = MediaPlayer.create(getApplicationContext(), uri);
                mp.setLooping(false);
                mp.start();

//                generate a vibrartion


            }
            else{
                tv_overspeed.setVisibility(View.INVISIBLE);
                gif1.setVisibility(View.INVISIBLE);

            }


        }

    }





}






*/


