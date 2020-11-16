package com.example.smartzi

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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


class InitActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    // check weather Location is enabled or not on your mobile
    fun locationEnable():Boolean{
        val manager = getSystemService( Context.LOCATION_SERVICE ) as LocationManager
        return manager.isProviderEnabled( LocationManager.GPS_PROVIDER )
    }
    // check weather Internet is enabled or not on your mobile
    fun internetConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    // Define the action if the Internet is enabled or not and Location is enabled or not on your mobile
    fun internetLocationCheck(){
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
                    "Connect"
            )
        }
        if (!locationE){
            dialogBoxBase(
                    "LOCATION OFF",
                    "Sorry to run this Application you have to enable location ",
                    "ON Location"
            )
        }
    }
    // get Permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            789 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    internetLocationCheck()
                } else {
                    dialogBoxBase(
                        "PERMISSION NEED",
                        "Sorry to Access this Application we need required permissions",
                        "ok")
                    this.finish()
                }
                return
            }
            else -> {
                dialogBoxBase(
                    "ALL PERMISSION NEED",
                    "Sorry to Access this Application we need required permissions",
                    "Ok")
                this.finish()
            }
        }
    }
    fun dialogBoxBase(title: String, BodyMessage: String,action:String?) {
        /*** Get Articles for list  */
        val dialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setNegativeButton("Cancel", null)
            .setPositiveButton(action,null)
            .setTitle(title)
            .setMessage(BodyMessage)
            .show()
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            when (action) {
                "Connect" -> {
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    dialog.dismiss()
                }
                "ON Location" -> {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    dialog.dismiss()
                }
            }
        }
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.CYAN)
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
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
        else{
            internetLocationCheck()
        }
    }
}