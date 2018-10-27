package mchehab.com.kotlin

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar

class MainActivity : AppCompatActivity(), LocationResultListener {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private val LOCATION_ACTIVITY_REQUEST_CODE = 1000

    private var editTextLocation: EditText? = null
    private var buttonLocation: Button? = null
    private var progressBar: ProgressBar? = null
    private var locationHandler: LocationHandler? = null
    private var alertDialogLocationError: AlertDialog? = null

    private fun initAlertDialog() {
        alertDialogLocationError = AlertDialog.Builder(this)
                .setMessage("Some Custom Error Location Retrieval Message Here")
                .setTitle("Error")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Retry") { dialog, which ->
                    showProgressBar()
                    locationHandler!!.getUserLocation()
                }
                .setCancelable(false).create()
    }

    private fun showEnableLocationDialog() {
        AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("You must enable location in order to proceed")
                .setPositiveButton("Enable") { dialog, which ->
                    showProgressBar()
                    locationHandler!!.getUserLocation()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    hideProgressBar()
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create()
                .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAlertDialog()
        locationHandler = LocationHandler(this, this,
                LOCATION_ACTIVITY_REQUEST_CODE, LOCATION_PERMISSION_REQUEST_CODE)
        editTextLocation = findViewById(R.id.editTextLocation)
        progressBar = findViewById(R.id.progressBar)
        buttonLocation = findViewById(R.id.buttonLocation)
        setButtonLocationListener()
    }

    private fun setButtonLocationListener() {
        buttonLocation!!.setOnClickListener { v ->
            editTextLocation!!.text.clear()
            showProgressBar()
            locationHandler!!.getUserLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            showProgressBar()
            locationHandler!!.getUserLocation()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOCATION_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                showProgressBar()
                locationHandler!!.getUserLocation()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showEnableLocationDialog()
            }
        }
    }

    override fun getLocation(location: Location) {
        hideProgressBar()
        editTextLocation!!.setText("Latitude: " + location.latitude + ",Longitude: " + location.longitude)
    }

    private fun showProgressBar() {
        progressBar!!.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar!!.visibility = View.GONE
    }
}