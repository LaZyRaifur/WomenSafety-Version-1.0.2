package com.example.programmer.womensafety;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;


public class MainActivity extends Activity implements AccelerometerListener,OnMenuSelectedListener, OnMenuStatusChangeListener ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,ResultCallback<LocationSettingsResult> {

 private com.hitomi.cmlibrary.CircleMenu circleMenu;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTING = 100;
    private static final int REQUEST_PHONE_CALL = 1;

    boolean isInitiated = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    initView();
        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30*1000);
        locationRequest.setFastestInterval(5*1000);
    }
    public void onAccelerationChanged(float x,float y,float z)
    {
        //TODO Auto-generated method stub
    }


    public void onShake(float force){
        //do your stuff
        //called when motion Detected
        //Toast.makeText(getBaseContext(),"Motion Detected",Toast.LENGTH_SHORT).show();
        String[] phone ={"01670545125","555","255"};


       Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel",phone[0],null));
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        startActivity(intent);

        if(isInitiated == false){

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
            }
            else
            {
               // Toast.makeText(getBaseContext(),"Motion Detected",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                isInitiated = true;
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getBaseContext(),"Accelerometer Started",Toast.LENGTH_SHORT).show();

        //Check device supported Accelerometer sensor or not
        ;if(AccelerometerManager.isSupported(this)){
            //start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Check device supported Accelerometer sensor or not
        if(AccelerometerManager.isListening())
        {
            //start Accelerometer Listening
            AccelerometerManager.stopListening();

            //Toast.makeText(getBaseContext(),"OnStop Accelerometer Stoped",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Sensor","Service Destroy");

        //Check device supported Accelerometer sensor or not
        if (AccelerometerManager.isListening())
        {
            //start Accelerometer Listening
            AccelerometerManager.stopListening();
            //Toast.makeText(getBaseContext(),"onDestroy Accelerometer Service",Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        circleMenu = (com.hitomi.cmlibrary.CircleMenu) findViewById(R.id.CircleMenu);
        circleMenu.setMainMenu(Color.RED, R.drawable.ic_home, R.drawable.ic_add_box);
        circleMenu.addSubMenu(Color.YELLOW, R.drawable.ic_person_add);
        circleMenu.addSubMenu(Color.RED, R.drawable.panic);
        circleMenu.addSubMenu(Color.WHITE, R.drawable.policeeeee);
        circleMenu.addSubMenu(Color.YELLOW, R.drawable.setting);
        circleMenu.addSubMenu(Color.GREEN, R.drawable.ambulance);
        circleMenu.setOnMenuSelectedListener(this);
        circleMenu.setOnMenuStatusChangeListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> results =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        results.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode())
        {
            case LocationSettingsStatusCodes.SUCCESS:
                //no need to show the dialog
               break;

                case CommonStatusCodes.RESOLUTION_REQUIRED:
                    //Location setting are not satisfied .show the user a dialog
                    try
                    {
                        //show the dialog by calling startResulotionForResult,and check the result
                        //in onActivityResult().
                        status.startResolutionForResult(MainActivity.this,REQUEST_CHECK_SETTING);
                    } catch (IntentSender.SendIntentException e) {
                        //failed to show
                    }
                    break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                //Location setting are unavailable so not possible to show
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTING){
            if (requestCode == RESULT_OK)
            {
                Toast.makeText(getApplicationContext(),"GPS enabled",Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onMenuSelected(int i) {
        switch (i) {
            case 0:
                Intent intent = new Intent(this,ContactList.class);
                startActivity(intent);
                Toast.makeText(this, "Emergency Contact", Toast.LENGTH_LONG).show();
                break;
            case 1:

                Intent PenicIntent = new Intent(this,Penic_button.class);
                startActivity(PenicIntent);
                Toast.makeText(this,"Panic Button : "+999,Toast.LENGTH_SHORT).show();
                break;
            case 2:

                Intent PoliceIntent = new Intent(this,Police.class);
                startActivity(PoliceIntent);
                Toast.makeText(this,"Police Station Number",Toast.LENGTH_SHORT).show();
                break;
            case 3:

                Intent HospitalIntent = new Intent(this,Hospital.class);
                startActivity(HospitalIntent);
                Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show();

                break;
            case 4:

                Intent AmbulenceIntent = new Intent(this,Ambulence.class);
                startActivity(AmbulenceIntent);
                Toast.makeText(this,"Ambulance Number",Toast.LENGTH_SHORT).show();

                break;
            default:
                break;

        }

    }

    @Override
    public void onMenuOpened() {
      // Toast.makeText(this, "Open", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMenuClosed() {
        //Toast.makeText(this, "Close", Toast.LENGTH_SHORT).show();

    }

    private class CircleMenu {
    }
//dialog box


    public void ShowDialog(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("QUIZE");
        builder.setMessage("Are you sure want to Quit?");
        builder.setPositiveButton("OK!!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(),"OK was Clicked",Toast.LENGTH_SHORT).show();
                finish();
            }
        })
                .setNegativeButton("Cencel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Ok was Clicked",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        //Create the AlertDialog object and return it

        builder.create().show();
    }


}
