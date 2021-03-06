package com.oldmaps.newmaps.maps.data.local.db_vintage_map

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oldmaps.newmaps.maps.data.model.model_vintage_map.TilesModel
import com.oldmaps.newmaps.maps.data.model.model_vintage_map.InfoModel

@Database(entities = [TilesModel::class, InfoModel::class], version = 1)
abstract class VintageMapDatabase : RoomDatabase() {
    abstract fun tilesTableDao(): TilesTableDao
    abstract fun infoTableDao(): InfoTableDao

    companion object {
        private const val DATABASE_NAME = "vintagemaps"

        fun instance(context: Context): VintageMapDatabase {
            return Room
                .databaseBuilder(context, VintageMapDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .createFromAsset("test.sqlitedb")
                .build()
        }
    }

}