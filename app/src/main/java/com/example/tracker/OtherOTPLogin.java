package com.example.tracker;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Arrays;

public class OtherOTPLogin extends AppCompatActivity implements LocationListener {


    private EditText editTextMobile;

    private LocationManager locationManager;
    FusedLocationProviderClient locationProviderClient;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_otplogin);

        editTextMobile = findViewById(R.id.editTextMobile);

        // getLocation();

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();

                if (mobile.isEmpty() || mobile.length() < 10) {
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(OtherOTPLogin.this, VerifyOTP.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });

    }

    //On Start
    @Override
    protected void onStart() {
        super.onStart();
        getLocation();
    }


    //Get User Location
    void getLocation() {
        if (checkPermission()) {
            if (islocationEnabled()) {
                locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.e("adawdawd", "awdwdawddwdadw");
                        if (location != null) {
                            Log.e("Location", "Lat:" + location.getLatitude() + "Long:" + location.getLongitude());
                            findViewById(R.id.textView).setVisibility(View.VISIBLE);
                            findViewById(R.id.buttonContinue).setVisibility(View.VISIBLE);
                            findViewById(R.id.editTextMobile).setVisibility(View.VISIBLE);
                            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                            findViewById(R.id.searchingtextview).setVisibility(View.INVISIBLE);
                        }

                    }
                });
            } else {
                new AlertDialog.Builder(context).setTitle("On Location")
                        .setMessage("To verify your current location")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        } else {
            Toast.makeText(this, "Enable Location Permission", Toast.LENGTH_SHORT).show();
        }

    }

    //Check GPS and Network is enabled or not.
    boolean islocationEnabled() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                return true;
            else {
                Toast.makeText(this, "Enable Network", Toast.LENGTH_SHORT).show();
                return false;
            }

        } else {
            Toast.makeText(this, "Enable Gps", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;

        } else {
            return true;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("MEsg", "&&" + requestCode + "&&" + Arrays.toString(grantResults));
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (grantResults.length > 0 && grantResults[0] == -1) {
            Log.e("Location", "permission is not accepted");
          /*  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",getPackageName(),null);
            intent.setData(uri);
            startActivity(intent);
            //startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS));*/
        }

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Granted. Start getting the location information
            Log.e("MEsg", "&&&&&&&&&&&&&&&&&");
            getLocation();

        }
    }

    @Override
    public void onLocationChanged(Location location) {
     Log.e("Location123",location.toString());
    }
}