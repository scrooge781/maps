package com.oldmaps.newmaps.maps.repo

import com.oldmaps.newmaps.maps.data.local.InfoTableDao
import com.oldmaps.newmaps.maps.data.local.TilesTableDao
import com.oldmaps.newmaps.maps.data.model.InfoModel
import com.oldmaps.newmaps.maps.data.model.TilesModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LocalMapsRepository(
    private val tilesTableDao: TilesTableDao,
    private val infoTableDao: InfoTableDao

) {
    suspend fun getAll(): List<TilesModel> =
        withContext(Dispatchers.IO) {
            tilesTableDao.queryGetAllData()
        }

    suspend fun getZoom(): InfoModel =
        withContext(Dispatchers.IO) {
            infoTableDao.getZoom()
        }

    suspend fun getCoordByZoom(zoom: Int): List<TilesModel> =
        withContext(Dispatchers.IO){
            tilesTableDao.getCoordByZoom(zoom)
        }
}