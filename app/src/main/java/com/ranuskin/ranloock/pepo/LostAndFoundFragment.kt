package com.ranuskin.ranloock.pepo


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.ranuskin.ranloock.pepo.Objects.LostPet
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.koushikdutta.ion.Ion
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.support.v4.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.ranuskin.ranloock.pepo.Objects.ClusterMarker
import com.ranuskin.ranloock.pepo.Objects.resizeImage
import com.ranuskin.ranloock.pepo.utils.MyClusterManagerRenderer
import java.util.concurrent.ExecutionException


class LostAndFoundFragment : Fragment(), OnMapReadyCallback, ClusterManager.OnClusterItemInfoWindowClickListener<ClusterMarker>{




    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private lateinit var mClusterManager: ClusterManager<ClusterMarker>
    private lateinit var mClusterManagerRenderer: MyClusterManagerRenderer
    private var clusterMarkers = mutableListOf<ClusterMarker>()


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        // 3
        private const val REQUEST_CHECK_SETTINGS = 2
    }
    var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    var mLastKnownLocation: Location? = null
    var mMap: GoogleMap? = null
    private var mLocationPermissionGranted = true
    val doge = LostPet("דוג מקדוקנס", 32.0750224, 34.7727508, "https://i.kym-cdn.com/entries/icons/mobile/000/013/564/doge.jpg"
    , "02/03/19", "לוקו שלנו נעלם לפני שלושה ימים רועה גרמני שחור!\n" +
                "אבד באיזור דימונה!\n" +
                "בבקשה מי שראה או שמע שידבר איתי!\n" +
                "פרס כספי למוצא!!!!", "doge next door", "too old","054-3210987"
        , "So Owner")
    val doge2 = LostPet("דוג גת יו", 31.6094991, 34.7708247, "https://i.kym-cdn.com/entries/icons/original/000/010/041/internet-memes-fetch-my-mind-its-blown-away.jpg"
        , "02/03/19", "לוקו שלנו נעלם לפני שלושה ימים רועה גרמני שחור!\n" +
                "אבד באיזור דימונה!\n" +
                "בבקשה מי שראה או שמע שידבר איתי!\n" +
                "פרס כספי למוצא!!!!", "doge next door", "too old","054-3210987"
        , "So Owner")
    val doges = mutableListOf<LostPet>()

    override fun onMapReady(p0: GoogleMap?) {

        mMap = p0

        if (ContextCompat.checkSelfPermission(activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = false
            return
        }

        p0!!.isMyLocationEnabled = true
        getLastKnownLocation()
        mMap!!.uiSettings!!.isZoomControlsEnabled = true
//        mMap!!.setOnMarkerClickListener(this)
        //mMap!!.setOnInfoWindowClickListener(this)

        setUpClusterer()
        markerFactory()
    }

    override fun onClusterItemInfoWindowClick(p0: ClusterMarker?) {
        println("${p0!!.title} was clicked")
        val bundle = Bundle()
        bundle.putSerializable("pet",doges[p0.serial])
        val ft = activity!!.supportFragmentManager.beginTransaction().addToBackStack(null)
        val detailsForLostFoundFragment = DetailsForLostFoundFragment()
        detailsForLostFoundFragment.arguments = bundle
        ft.replace(R.id.container, detailsForLostFoundFragment).commit()
    }

    private fun setUpClusterer(){
        mClusterManager = ClusterManager(activity!!,mMap!!)
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mClusterManagerRenderer = MyClusterManagerRenderer(activity!!,mMap!!,mClusterManager)
        mClusterManager.renderer = mClusterManagerRenderer

        mMap!!.setOnCameraIdleListener(mClusterManager)
        mMap!!.setOnMarkerClickListener(mClusterManager)
        mMap!!.setOnInfoWindowClickListener(mClusterManager)
        mClusterManager.setOnClusterItemInfoWindowClickListener(this)


    }
    private fun markerFactory(){

        try {

            var iterator = 0
            for (mDoge in doges){
                var bmImg = Ion.with(context)
                    .load(mDoge.imageURL).asBitmap().get()
                val latlng = LatLng(mDoge.lat, mDoge.lng)
                val clusterItem = ClusterMarker(latlng,mDoge.name,"נראה לאחרונה " + "${mDoge.date}",bmImg,iterator)
                mClusterManager.addItem(clusterItem)

                clusterMarkers.add(clusterItem)
                iterator += 1
            }
//            var bmImg = Ion.with(context)
//                .load(doge.imageURL).asBitmap().get()
//            val latlng = LatLng(doge.lat, doge.lng)
//            val clusterItem = ClusterMarker(latlng,doge.name,"was lost on ${doge.date}",bmImg,0)
//            mClusterManager.addItem(clusterItem)
//
//            clusterMarkers.add(clusterItem)

        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

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

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                mLastKnownLocation = p0.lastLocation
            }
        }
        doges.add(doge)
        doges.add(doge2)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
        createLocationRequest()
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
                val mLocation = LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude)
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 16.0f))

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

    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(activity!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        //2
        mFusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }
    private fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest()
        // 2
        locationRequest.interval = 10000
        // 3
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(activity!!)
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(activity!!,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }
    // 1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    // 2
    override fun onPause() {
        super.onPause()
        mFusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
    }

    // 3
    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

}
