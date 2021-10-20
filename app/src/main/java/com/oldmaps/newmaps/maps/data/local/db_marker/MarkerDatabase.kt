package com.oldmaps.newmaps.maps.data.local.db_marker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oldmaps.newmaps.maps.data.model.MarkerModel

@Database(entities = [MarkerModel::class], version = 1)
abstract class MarkerDatabase : RoomDatabase() {

    abstract fun markerTableDao(): MarkerTableDao

    companion object {
        private const val NAME_MARKER_DB = "markers_db"

        fun instance(context: Context): MarkerDatabase =
            Room.databaseBuilder(context, MarkerDatabase::class.java, NAME_MARKER_DB)
                .build()


    }

}