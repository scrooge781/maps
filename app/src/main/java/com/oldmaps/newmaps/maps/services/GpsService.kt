package com.oldmaps.newmaps.maps.services

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.oldmaps.newmaps.maps.util.Constants.ACTION_PAUSE_SERVICE
import com.oldmaps.newmaps.maps.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.oldmaps.newmaps.maps.util.Constants.ACTION_STOP_SERVICE
import com.oldmaps.newmaps.maps.util.Constants.FASTEST_LOCATION_INTERVAL
import com.oldmaps.newmaps.maps.util.Constants.LOCATION_UPDATE_INTERVAL
import com.oldmaps.newmaps.maps.util.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class GpsService : LifecycleService() {

    var isFirstRun = true
    var serviceKilled = false

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val isPoint = MutableLiveData<Polylines>()
        val isLocationOnOff = MutableLiveData<Boolean>()
    }

    private fun postInitialValue() {
        isTracking.postValue(false)
        isPoint.postValue(mutableListOf())

    }

    override fun onCreate() {
        super.onCreate()
        postInitialValue()

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        addEmptyPolyline()
                        isTracking.postValue(true)
                        isFirstRun = false
                    } else {
                        addEmptyPolyline()
                        isTracking.postValue(true)
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    isTracking.postValue(false)
                }
                ACTION_STOP_SERVICE -> {
                    killService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun killService() {
        serviceKilled = true
        isFirstRun = true
        isTracking.postValue(false)
        postInitialValue()
        stopForeground(true)
        stopSelf()
    }


    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermission(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result?.locations?.let {
                    for (location in it) {
                        addPathPoint(location)
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            isPoint.value?.apply {
                last().add(pos)
                isLocationOnOff.postValue(true)
                isPoint.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline() = isPoint.value?.apply {
        add(mutableListOf())
        isPoint.postValue(this)
    } ?: isPoint.postValue(mutableListOf(mutableListOf()))

}