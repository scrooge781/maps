package com.oldmaps.newmaps.maps.data.local.db_marker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oldmaps.newmaps.maps.data.model.MarkerModel

@Dao
interface MarkerTableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMarker(marker: MarkerModel)

    @Query("SELECT * FROM marker WHERE id =:id")
    suspend fun getMarkerById(id: Long): MarkerModel

    @Query("SELECT * FROM marker")
    suspend fun getAllMarkers(): List<MarkerModel>

    @Query("DELETE FROM marker WHERE id =:id")
    suspend fun deleteMarkerById(id: Long)

}