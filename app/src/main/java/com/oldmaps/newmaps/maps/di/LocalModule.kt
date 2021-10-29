package com.oldmaps.newmaps.maps.di

import android.content.Context
import android.content.SharedPreferences
import com.oldmaps.newmaps.maps.data.local.db_marker.MarkerDatabase
import com.oldmaps.newmaps.maps.data.local.db_marker.MarkerTableDao
import com.oldmaps.newmaps.maps.data.local.db_vintage_map.VintageMapDatabase
import com.oldmaps.newmaps.maps.data.local.db_vintage_map.InfoTableDao
import com.oldmaps.newmaps.maps.data.local.db_vintage_map.TilesTableDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    //Vintage map database

    @Singleton
    @Provides
    fun provideTestDatabase(@ApplicationContext context: Context): VintageMapDatabase =
        VintageMapDatabase.instance(context)

    @Singleton
    @Provides
    fun provideTilesTableDoa(database: VintageMapDatabase): TilesTableDao =
        database.tilesTableDao()


    @Singleton
    @Provides
    fun provideInfoTableDao(database: VintageMapDatabase): InfoTableDao =
        database.infoTableDao()


    //----------------------------------------------------------------------------------------------
    // Markers database

    @Singleton
    @Provides
    fun provideMarkerDatabase(@ApplicationContext context: Context): MarkerDatabase =
        MarkerDatabase.instance(context)

    @Singleton
    @Provides
    fun provideMarkerTableDao(database: MarkerDatabase): MarkerTableDao =
        database.markerTableDao()

    //----------------------------------------------------------------------------------------------

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("preferences_name", Context.MODE_PRIVATE)
    }

}