package com.example.smartzi

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val driverA = LatLng(13.068500, 80.234938)
    private val driverB = LatLng(13.062306, 80.231172)
    private val driverC = LatLng(13.071086, 80.230709)
    private lateinit var bookNow:Button
    private var minDistance: Float = 1000F
    private var driverName:String? = null

    private var locatinArray: ArrayList<Drivers> = ArrayList()
    private var myLatitude: Double = 0.0
    private var myLongitude: Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        bookNow = findViewById<Button>(R.id.BookNow)
        bookNow.isEnabled= false


        val driverALocation = Location("driverA")
        driverALocation.latitude = 13.068500
        driverALocation.longitude = 80.234938
        locatinArray.add(Drivers("Driver A",driverALocation))

        val driverBLocation = Location("driverB")
        driverBLocation.latitude = 13.062306
        driverBLocation.longitude = 80.231172
        locatinArray.add(Drivers("Driver B",driverBLocation))

        val driverCLocation = Location("driverC")
        driverCLocation.latitude = 13.071086
        driverCLocation.longitude = 80.230709
        locatinArray.add(Drivers("Drivers C",driverCLocation))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val myLocation = Location("myLocation")
                myLocation.latitude = location!!.latitude
                myLocation.longitude = location.longitude

                myLatitude = location.latitude
                myLongitude = location.longitude

                val currentLocation = LatLng(myLatitude, myLongitude)
                mMap.addMarker(MarkerOptions().position(currentLocation).title("Your Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12F), 2000, null)
                val circleOptions: CircleOptions =  CircleOptions()
                        .center(
                                LatLng(
                                        myLatitude,
                                        myLongitude
                                )
                        )
                        .radius(1000.00)
                        .fillColor(0x40ff0000)
                        .strokeColor(Color.TRANSPARENT)
                        .strokeWidth(2.toFloat());
                mMap.addCircle(circleOptions)
                distanceCal(locatinArray,myLocation)
            }

        bookNow.setOnClickListener{
            val intent = Intent(this@MapsActivity, BookingActivity::class.java)
            intent.putExtra("DriverName", driverName)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.addMarker(MarkerOptions().position(driverA).title("Driver A"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(driverA))

        mMap.addMarker(MarkerOptions().position(driverB).title("Driver B"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(driverB))

        mMap.addMarker(MarkerOptions().position(driverC).title("Driver C"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(driverC))
    }
    // calculate the distance between current location and drivers location
    fun distanceCal(arrayLoaction: ArrayList<Drivers>, loc1: Location) {

        for (drivers in arrayLoaction){
            val distance = loc1.distanceTo(drivers.location)
            if (distance <= 1000 ){
                if (distance <= minDistance){
                    minDistance = distance
                    driverName = drivers.name
                }
                bookNow.isEnabled= true
            }
        }
    }
}