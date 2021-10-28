package com.oldmaps.newmaps.maps.util

import com.google.android.gms.maps.model.LatLng
import com.oldmaps.newmaps.maps.data.model.model_vintage_map.TilesModel
import kotlin.math.*

object Converting {

    private fun convertCartesianToSpherical(x: Double, y: Double, z: Double): LatLng {
        var n = 2.0.pow(z)
        var lon = (x / n) * 360.0 - 180.0
        var lat = atan(sinh(Math.PI - y / n * 2 * Math.PI)) * (180.0 / Math.PI)

        return LatLng(lat, lon)
    }

    fun averageCoordinate(listTilesMode: List<TilesModel>): LatLng {
        var sumX = 0.0
        var sumY = 0.0

        for (iterator in listTilesMode) {
            sumX += iterator.x.toDouble()
            sumY += iterator.y.toDouble()
        }

        var averageX = sumX / listTilesMode.size
        var averageY = sumY / listTilesMode.size
        var z = (17 - listTilesMode[1].z.toDouble())

        return convertCartesianToSpherical(averageX, averageY, z)
    }

}