package com.covidapp

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationListener
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.covidapp.interfaces.OnHttpResponse
import com.covidapp.webapi.model.WebApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions



class MapActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,GoogleMap.OnMarkerClickListener {


    companion object {

        fun getLaunchIntent(from: Context) = Intent(from, Register::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        }

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }


    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

 override fun onCreate(savedInstanceState: Bundle?) {
     super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_map_home)
     // Obtain the SupportMapFragment and get notified when the map is ready to be used.
     val mapFragment = supportFragmentManager
         .findFragmentById(R.id.map) as SupportMapFragment
     mapFragment.getMapAsync(this)
     fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

     getUser()

 }


 override fun onMapReady(googleMap: GoogleMap) {



     map = googleMap

     map.uiSettings.isZoomControlsEnabled = true
     map.setOnMarkerClickListener(this)

     setUpMap()


     map.isMyLocationEnabled = true

// 2
     fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
         // Got last known location. In some rare situations this can be null.
         // 3
         if (location != null) {
             lastLocation = location
             val currentLatLng = LatLng(location.latitude, location.longitude)
             map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
         }
     }

     for (marker in 0..((getMarkers().size)-1)){
         createMarker(getMarkers()[marker].latitude,getMarkers()[marker].longitude, getMarkers()[marker].title, getMarkers()[marker].iconResID)

     }

 }


    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }





    protected fun createMarker(latitude:Double, longitude:Double, title:String, iconResID:Int):Marker {
        return map.addMarker(MarkerOptions()
            .position(LatLng(latitude, longitude))
            .anchor(0.5f, 0.5f)
            .title(title)
          //  .snippet(snippet)
            .icon(bitmapDescriptorFromVector(this,iconResID)))
    }


    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }


    private fun getMarkers():ArrayList<MarkerData>{
        var markersArray = ArrayList<MarkerData>()
        markersArray.add(MarkerData(10.362750, 76.144623,"Quarantine 2",R.drawable.ic_location_green))
        markersArray.add( MarkerData(10.244643, 76.205690,"Quarantine 2",R.drawable.ic_location_green))
        markersArray.add( MarkerData(10.678680, 76.084046,"Quarantine 2",R.drawable.ic_location_red))
        markersArray.add( MarkerData(11.218031, 75.802494,"Quarantine 2",R.drawable.ic_location_green))
        markersArray.add(MarkerData(11.351482, 75.740653,"Quarantine 2",R.drawable.ic_location_red))
        markersArray.add(MarkerData(11.458492, 75.680670,"Quarantine 2",R.drawable.ic_location_green))
        markersArray.add(MarkerData(11.713883, 75.524331,"Quarantine 2",R.drawable.ic_location_green))
        markersArray.add(MarkerData(11.768146, 75.479268,"Quarantine 2",R.drawable.ic_location_red))
        markersArray.add(MarkerData(11.851993, 75.415293,"Quarantine 2",R.drawable.ic_location_green))
        markersArray.add(MarkerData(8.702241, 76.814225,"Quarantine 2",R.drawable.ic_location_green))
        markersArray.add(MarkerData(8.819760, 76.698662,"Quarantine 2",R.drawable.ic_location_red))
        markersArray.add(MarkerData(9.127201, 76.840946,"Quarantine 2",R.drawable.ic_location_red))
        markersArray.add(MarkerData(10.162220, 77.107119,"Quarantine 2",R.drawable.ic_location_green))
        markersArray.add(MarkerData(11.667858, 76.344065,"Quarantine 2",R.drawable.ic_location_green))
        markersArray.add(MarkerData(11.859610, 76.066977,"Quarantine 2",R.drawable.ic_location_green))

        return markersArray
    }

    data class MarkerData(val latitude: Double,val longitude: Double,val title: String,val iconResID: Int)

    private fun getUserInfo(){
        Log.d("apicall3534","value : insideGetUserInfo")
        WebApi(object : OnHttpResponse {
            override fun <T> onResponse(objectResponse: T) {

                // val obj =objectResponse as UserModel

                Log.d("response546","value: ${objectResponse.toString()}")


            }

        }).getAllDelayedChannels()
    }



    private fun getUser(){

        var data = HashMap<String,String>()

        FirebaseFunctions.getInstance() // Optional region: .getInstance("europe-west1")
            .getHttpsCallable("http://us-central1-savekerala-79d2a.cloudfunctions.net/api/users/+919037513781")
            .call(data)
            .addOnFailureListener {
                Log.wtf("FF", it)
                Log.d("response546","value failure: ${it.message.toString()}")
            }
            .addOnSuccessListener {

                Log.d("response546","value success: ${it.data.toString()}")
            }
    }
}









