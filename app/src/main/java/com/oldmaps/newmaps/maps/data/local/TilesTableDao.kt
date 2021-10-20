package com.oldmaps.newmaps.maps.data.local

import androidx.room.Dao
import androidx.room.Query
import com.oldmaps.newmaps.maps.data.model.TilesModel


@Dao
interface TilesTableDao {

    /*  @Query("SELECT * FROM tiles WHERE x = :x AND y = :y AND z = :z")
      suspend fun findById(x: Int, y: Int, z: Int): TilesTableModel?
  */

    @Query("SELECT * FROM tiles")
    suspend fun queryGetAllData(): List<TilesModel>

    @Query("SELECT * FROM tiles WHERE x = :x")
    suspend fun getInfoX(x: Int): TilesModel

}