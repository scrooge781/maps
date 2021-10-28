package com.oldmaps.newmaps.maps.di

import android.content.Context
import com.oldmaps.newmaps.maps.data.local.db_vintage_map.InfoTableDao
import com.oldmaps.newmaps.maps.data.local.db_vintage_map.TilesTableDao
import com.oldmaps.newmaps.maps.repo.CoordTileRepository
import com.oldmaps.newmaps.maps.repo.LocalMapsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        @ApplicationContext context: Context,
        tilesTableDao: TilesTableDao
    ): CoordTileRepository =
        CoordTileRepository(context, tilesTableDao)


}