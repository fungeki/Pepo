package com.ranuskin.ranloock.pepo

import android.annotation.TargetApi
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.location.LocationManager
import android.content.DialogInterface
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

//, NavigationView.OnNavigationItemSelectedListener
class MainActivity : AppCompatActivity(){


    private val TAG = "MainActivity"

    var mLocationPermissionGranted = false
    val ERROR_DIALOG_REQUEST = 9001
    val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002
    val PERMISSIONS_REQUEST_ENABLE_GPS = 9003


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark,null)
        bottom_nav_bar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        join_now_circle.setOnClickListener{
            val pepoSignInLink = "https://goo.gl/forms/jrlMjWEZMm70NYkG2"
            val linkIntent = Intent(Intent.ACTION_VIEW)
            linkIntent.data = Uri.parse(pepoSignInLink)
            startActivity(linkIntent)
        }

//        setSupportActionBar(toolbar)


//        val toggle = ActionBarDrawerToggle(
//            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
        //drawer_layout.addDrawerListener(toggle)
        //toggle.syncState()

        //nav_view.setNavigationItemSelectedListener(this)

    }



    fun refreshFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.container, LostAndFoundFragment()).commit()
    }

    private fun checkMapServices(): Boolean {
        if (isServicesOK()) {
            if (isMapsEnabled()) {
                return true
            }
        }
        return false

    }

    override fun onResume() {
        super.onResume()
        if (checkMapServices()){
            if (mLocationPermissionGranted){

            } else {
                getLocationPermission()
            }
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.bot_nav_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.container,LostAndFoundFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.bot_nav_vet -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.bot_nav_report -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.bot_nav_shop ->{
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                val enableGpsIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
            })
        val alert = builder.create()
        alert.show()
    }

    fun isMapsEnabled(): Boolean {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
            supportFragmentManager.beginTransaction().replace(R.id.container, LostAndFoundFragment()).commit()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    fun isServicesOK(): Boolean {
        //Log.d(FragmentActivity.TAG, "isServicesOK: checking google services version")

        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@MainActivity)

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests

            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            val dialog =
                GoogleApiAvailability.getInstance().getErrorDialog(this@MainActivity, available, ERROR_DIALOG_REQUEST)
            dialog.show()
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                    refreshFragment()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PERMISSIONS_REQUEST_ENABLE_GPS -> {
                if (mLocationPermissionGranted) {

                } else {
                    getLocationPermission()
                }
            }
        }

    }










//    override fun onBackPressed() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.nav_home -> {
//                // Handle the camera action
//            }
//            R.id.nav_gallery -> {
//
//            }
//            R.id.nav_vet_chat -> {
//
//            }
//            R.id.nav_manage -> {
//
//            }
//            R.id.nav_share -> {
//
//            }
//        }
//
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }
}
