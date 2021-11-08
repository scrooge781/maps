package com.oldmaps.newmaps.maps.data.model

import com.google.android.gms.maps.model.LatLng

data class LatLonZoomModel(

    var maxZoomMap: Float? = null,
    var minZoomMap: Float? = null,
    var latlng: LatLng? = null
)