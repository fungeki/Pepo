package com.ranuskin.ranloock.pepo


import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.tasks.Task


class LostAndFoundFragment : Fragment(), OnMapReadyCallback {

    var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    var mLastKnownLocation: Location? = null
    var mMap: GoogleMap? = null
    private var mLocationPermissionGranted = true

    override fun onMapReady(p0: GoogleMap?) {

        mMap = p0

        if (ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = false
            return
        }

        p0!!.isMyLocationEnabled = true
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_lost_and_found_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
        val mapFragment = childFragmentManager.findFragmentById(R.id.lost_and_found_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun getLastKnownLocation(){
        Log.d("meow", "getLastKnownLocation was called")
        if (ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mFusedLocationProviderClient?.lastLocation?.addOnCompleteListener(activity!!
        ) { task: Task<Location> ->
            if (task.isSuccessful){
                mLastKnownLocation = task.result

        }
    }
    }

    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
        try {
            if (mLocationPermissionGranted) {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                mMap?.isMyLocationEnabled = false
                mMap?.getUiSettings()?.isMyLocationButtonEnabled = false
                mLastKnownLocation = null
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }

    }

}
