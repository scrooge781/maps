package com.oldmaps.newmaps.maps.di

import android.content.Context
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

    @Singleton
    @Provides
    fun provideTestDatabase(@ApplicationContext app: Context): VintageMapDatabase =
        VintageMapDatabase.instance(app)

    @Singleton
    @Provides
    fun provideTilesTableDoa(database: VintageMapDatabase): TilesTableDao =
        database.tilesTableDao()


    @Singleton
    @Provides
    fun provideInfoTableDao(database: VintageMapDatabase): InfoTableDao =
        database.infoTableDao()
}