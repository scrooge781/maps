package com.oldmaps.newmaps.maps.data.local.db_vintage_map

import androidx.room.Dao
import androidx.room.Query
import com.oldmaps.newmaps.maps.data.model.model_vintage_map.InfoModel

@Dao
interface InfoTableDao {

    @Query("SELECT * FROM info")
    suspend fun getZoom(): InfoModel

}