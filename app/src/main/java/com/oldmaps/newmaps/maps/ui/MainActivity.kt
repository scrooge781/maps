package com.oldmaps.newmaps.maps.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.util.CollectionUtils.setOf
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.services.GpsService
import com.oldmaps.newmaps.maps.ui.main_map.MapsMainViewModel
import com.oldmaps.newmaps.maps.util.Constants
import com.oldmaps.newmaps.maps.util.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.oldmaps.newmaps.maps.util.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject
import android.location.LocationManager
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var isTracking = false


    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val mapsViewModel: MapsMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Прозрачный фон Status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN
        )
        init()
    }

    private fun init() {

        //убирает ActionBar
        supportActionBar?.hide()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menu_modern_map, R.id.menu_vintage_map, R.id.menu_marker_map, R.id.menu_setting
            ), drawer_layout
        )

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)


        val switchMarker =
            nav_view.getHeaderView(0).findViewById<SwitchMaterial>(R.id.show_marker_menu)

        when (sharedPreferences.getBoolean("showMarker", false)) {
            true -> switchMarker.isChecked = true
            else -> switchMarker.isChecked = false
        }

        switchMarker.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                true -> {
                    sharedPreferences.edit().putBoolean("showMarker", true).apply()
                    mapsViewModel.showMarkerOnMaps(true)
                }
                else -> {
                    sharedPreferences.edit().putBoolean("showMarker", false).apply()
                    mapsViewModel.showMarkerOnMaps(false)
                }
            }
        }
    }

    fun openCloseNavigationDrawer(view: View) {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i("TAG", "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("TAG", status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        enableLocation()
        requestPermission()

    }

    //permissions gps location device
    private fun requestPermission() {
        if (TrackingUtility.hasLocationPermission(this)) {
            return
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        enableLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun toggleRun() {
        if (isTracking) {
            sendCommendToService(Constants.ACTION_PAUSE_SERVICE)
        } else {
            sendCommendToService(Constants.ACTION_START_OR_RESUME_SERVICE)
        }
    }


    private fun sendCommendToService(action: String) {
        Intent(this, GpsService::class.java).also {
            it.action = action
            startService(it)
        }
    }

    private fun enableLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 30 * 1000.toLong()
            fastestInterval = 5 * 1000.toLong()
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
                println("location>>>>>>> ${response.locationSettingsStates.isGpsPresent}")
                if (response.locationSettingsStates.isGpsPresent){
                    toggleRun()
                    GpsService.isLocationOnOff.postValue(true)
                }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(e.status.resolution).build()
                        launcher.launch(intentSenderRequest)
                    } catch (e: IntentSender.SendIntentException) {
                    }
                }
            }
        }
    }

    private var launcher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                toggleRun()
                GpsService.isLocationOnOff.postValue(true)
            } else {
                GpsService.isLocationOnOff.postValue(false)
            }
        }

}