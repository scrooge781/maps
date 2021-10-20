package com.oldmaps.newmaps.maps.repo

import com.oldmaps.newmaps.maps.data.local.db_marker.MarkerTableDao
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarkerMapRepository @Inject constructor(
    private val markerTableDao: MarkerTableDao
) {

    suspend fun saveMarker(marker: MarkerModel) =
        withContext(Dispatchers.IO) {
            markerTableDao.saveMarker(marker)
        }


    suspend fun getMarkerById(id: Long): MarkerModel =
        withContext(Dispatchers.IO) {
            markerTableDao.getMarkerById(id)
        }

    suspend fun getAllMarker(): List<MarkerModel> =
        withContext(Dispatchers.IO) {
            markerTableDao.getAllMarkers()
        }

    suspend fun deleteMarkerById(id: Long) =
        withContext(Dispatchers.IO) {
            markerTableDao.deleteMarkerById(id)
        }

}