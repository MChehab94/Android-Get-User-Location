package mchehab.com.getlocation;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements LocationResultListener {

    private final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private final int LOCATION_ACTIVITY_REQUEST_CODE = 1000;

    private EditText editTextLocation;
    private Button buttonLocation;
    private ProgressBar progressBar;
    private LocationHandler locationHandler;

    private void showEnableLocationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("You must enable location in order to proceed")
                .setPositiveButton("Enable", (dialog, which) -> {
                    showProgressBar();
                    locationHandler.getUserLocation();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    hideProgressBar();
                    dialog.dismiss();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationHandler = new LocationHandler(this, this,
                LOCATION_ACTIVITY_REQUEST_CODE, LOCATION_PERMISSION_REQUEST_CODE);
        setupUI();
        setButtonLocationListener();
    }

    private void setupUI(){
        editTextLocation = findViewById(R.id.editTextLocation);
        progressBar = findViewById(R.id.progressBar);
        buttonLocation = findViewById(R.id.buttonLocation);
    }

    private void setButtonLocationListener(){
        buttonLocation.setOnClickListener(v -> {
            editTextLocation.getText().clear();
            showProgressBar();
            locationHandler.getUserLocation();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            showProgressBar();
            locationHandler.getUserLocation();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCATION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showProgressBar();
                locationHandler.getUserLocation();
            }else if (resultCode == RESULT_CANCELED){
                showEnableLocationDialog();
            }
        }
    }

    @Override
    public void getLocation(Location location) {
        hideProgressBar();
        editTextLocation.setText("Latitude: " + location.getLatitude() + ",Longitude: " + location.getLongitude());
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }
}