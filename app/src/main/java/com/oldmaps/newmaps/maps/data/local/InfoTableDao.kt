package com.oldmaps.newmaps.maps.data.local

import androidx.room.Dao
import androidx.room.Query
import com.oldmaps.newmaps.maps.data.model.InfoModel

@Dao
interface InfoTableDao {

    @Query("SELECT * FROM info")
    suspend fun getZoom(): InfoModel

}