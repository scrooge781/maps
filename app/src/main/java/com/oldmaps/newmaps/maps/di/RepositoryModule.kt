package com.oldmaps.newmaps.maps.di

import com.oldmaps.newmaps.maps.data.local.db_marker.MarkerTableDao
import com.oldmaps.newmaps.maps.data.local.db_vintage_map.InfoTableDao
import com.oldmaps.newmaps.maps.data.local.db_vintage_map.TilesTableDao
import com.oldmaps.newmaps.maps.repo.CoordinateTileRepository
import com.oldmaps.newmaps.maps.repo.LocalMapsRepository
import com.oldmaps.newmaps.maps.repo.MarkerMapRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepoVintageMaps(
        tilesTableDao: TilesTableDao,
        infoTableDao: InfoTableDao
    ): LocalMapsRepository =
        LocalMapsRepository(tilesTableDao, infoTableDao)

    @Provides
    @Singleton
    fun provideRepoCoordTile(
        tilesTableDao: TilesTableDao,
        infoTableDao: InfoTableDao
    ): CoordinateTileRepository =
        CoordinateTileRepository(tilesTableDao, infoTableDao)

    @Provides
    @Singleton
    fun provideRepoMarkerMap(
        markerTableDao: MarkerTableDao
    ): MarkerMapRepository =
        MarkerMapRepository(markerTableDao)
}