package com.oldmaps.newmaps.maps.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oldmaps.newmaps.maps.data.model.TilesModel
import com.oldmaps.newmaps.maps.data.model.InfoModel

@Database(entities = [TilesModel::class, InfoModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tilesTableDao(): TilesTableDao
    abstract fun infoTableDao(): InfoTableDao

    companion object {
        private const val DATABASE_NAME = "vintagemaps"

        fun instance(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .createFromAsset("test.sqlitedb")
                .build()
        }
    }

}