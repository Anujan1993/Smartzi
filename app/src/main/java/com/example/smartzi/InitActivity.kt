package com.example.smartzi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


open class InitActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                789
            )
        }
    }
    fun locationEnable():Boolean{
        val manager = getSystemService( Context.LOCATION_SERVICE ) as LocationManager
        return manager.isProviderEnabled( LocationManager.GPS_PROVIDER )
    }
    fun internetConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            789 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    val connection = internetConnection()

                    val locationE = locationEnable()
                    if (connection && locationE) {
                        val intent = Intent(this@InitActivity, MapsActivity::class.java)
                        startActivity(intent)
                    }
                    if(!connection){
                        dialogBoxBase(
                            "NO INTERNET",
                            "Sorry to run this Application we need Internet ",
                            "internet"
                        )
                    }
                    if (!locationE){
                        dialogBoxBase(
                            "LOCATION OFF",
                            "Sorry to run this Application you have to enable location ",
                            "location"
                        )
                    }
                } else {
                    dialogBoxBase(
                        "PERMISSION NEED",
                        "Sorry to Access this Application we need required permissions",
                        "")
                }
                return
            }
            else -> {
                dialogBoxBase(
                    "ALL PERMISSION NEED",
                    "Sorry to Access this Application we need required permissions",
                    "")
            }
        }
    }
    open fun dialogBoxBase(title: String, BodyMessage: String,action:String?) {
        /*** Get Articles for list  */
        val dialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Connect",null)
            .setTitle(title)
            .setMessage(BodyMessage)
            .show()
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (action == "internet") {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            else if(action == "location"){
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }

}