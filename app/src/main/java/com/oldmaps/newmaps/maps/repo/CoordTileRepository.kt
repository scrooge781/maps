package com.oldmaps.newmaps.maps.repo

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import com.oldmaps.newmaps.maps.data.local.db_vintage_map.TilesTableDao

class CoordTileRepository(
    private val context: Context,
    private val tilesTableDao: TilesTableDao
) : TileProvider {
    override fun getTile(x: Int, y: Int, zoom: Int): Tile {
        val TILE_SIZE_DP = 256
        val scaleFactor: Float = context.resources.displayMetrics.density * 0.6f
        val z = 17 - zoom

        Log.d("xyz", "x: $x  y: $y z: $z")

        return Tile(
            (TILE_SIZE_DP * scaleFactor).toInt(),
            (TILE_SIZE_DP * scaleFactor).toInt(),
            tilesTableDao.getBlobImage(x, y, z).image
        )
    }
}