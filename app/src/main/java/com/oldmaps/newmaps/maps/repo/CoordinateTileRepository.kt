package com.oldmaps.newmaps.maps.repo

import android.graphics.*
import android.util.Log
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import com.oldmaps.newmaps.maps.data.local.db_vintage_map.InfoTableDao
import com.oldmaps.newmaps.maps.data.local.db_vintage_map.TilesTableDao
import javax.inject.Inject

import java.io.ByteArrayOutputStream


class CoordinateTileRepository @Inject constructor(
    private val tilesTableDao: TilesTableDao,
    private val infoTableDao: InfoTableDao
) : TileProvider {
    private val tilePainter: Paint = Paint()

    // these will only be used when DRAW_DEBUG_DATA is true
    private var debugRectPaint: Paint? = null
    private var debugTextPaint: Paint? = null
    override fun getTile(x: Int, y: Int, zoom: Int): Tile {
        val minZoom = 17 - infoTableDao.getZoom().maxzoom.toInt()
        val maxZoom = 17 - infoTableDao.getZoom().minzoom.toInt()
        Log.d("xyz", "x: $x  y: $y z: $zoom  minzoom: $minZoom  maxZoom: $maxZoom")

        return if (zoom <= maxZoom) {
            val z = 17 - zoom
            val image = tilesTableDao.getBlobImage(x, y, z).image
            Tile(
                TILE_SIZE,
                TILE_SIZE,
                image
            )
        } else {
            val image = Bitmap.createBitmap(
                TILE_SIZE, TILE_SIZE,
                Bitmap.Config.ARGB_8888
            )
            image.eraseColor(Color.TRANSPARENT)
            val canvas = Canvas(image)
            drawTile(canvas, zoom, x, y)
            val data = bitmapToByteArray(image)
            image.recycle()
            Tile(
                TILE_SIZE,
                TILE_SIZE,
                data
            )
        }
    }

    private fun drawTile(canvas: Canvas, zoom: Int, x: Int, y: Int) {
        val bitmap = getTileAsBitmap(x, y, zoom)
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0F, 0F, tilePainter)
            bitmap.recycle()
        }
        if (DRAW_DEBUG_DATA) {
            canvas.drawRect(
                0F,
                0F,
                canvas.width.toFloat(),
                canvas.height.toFloat(),
                debugRectPaint!!
            )
            canvas.drawText("$x, $x ($zoom)", 128F, 128F, debugTextPaint!!)
        }
    }

    private fun getTileAsBitmap(x: Int, y: Int, zoom: Int): Bitmap? {

        val minZoom = 17 - infoTableDao.getZoom().maxzoom.toInt()
        val maxZoom = 17 - infoTableDao.getZoom().minzoom.toInt()
        if (zoom in minZoom..maxZoom) {
            val z = 17 - zoom
            val image = tilesTableDao.getBlobImage(x, y, z).image
            return BitmapFactory.decodeByteArray(image, 0, image?.size!!)
        }
        val leftColumn = x % 2 == 0
        val topRow = y % 2 == 0
        var bitmap = getTileAsBitmap(x / 2, y / 2, zoom - 1)
        val quadrant: Int = if (leftColumn && topRow) {
            1
        } else if (!leftColumn && topRow) {
            2
        } else if (leftColumn) {
            3
        } else {
            4
        }
        when (quadrant) {
            1 -> bitmap = Bitmap.createBitmap(
                bitmap!!,
                0,
                0,
                HALF_TILE_SIZE,
                HALF_TILE_SIZE
            )
            2 -> bitmap = Bitmap.createBitmap(
                bitmap!!,
                HALF_TILE_SIZE,
                0,
                HALF_TILE_SIZE,
                HALF_TILE_SIZE
            )
            3 -> bitmap = Bitmap.createBitmap(
                bitmap!!,
                0,
                HALF_TILE_SIZE,
                HALF_TILE_SIZE,
                HALF_TILE_SIZE
            )
            4 -> bitmap = Bitmap.createBitmap(
                bitmap!!,
                HALF_TILE_SIZE,
                HALF_TILE_SIZE,
                HALF_TILE_SIZE,
                HALF_TILE_SIZE
            )
        }
        return Bitmap.createScaledBitmap(
            bitmap!!,
            TILE_SIZE,
            TILE_SIZE,
            false
        )
    }

    companion object {
        private const val DRAW_DEBUG_DATA = false
        private const val TILE_SIZE = 256
        private const val HALF_TILE_SIZE = TILE_SIZE / 2
        private fun bitmapToByteArray(bm: Bitmap): ByteArray {
            val bos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val data: ByteArray = bos.toByteArray()
            try {
                bos.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return data
        }
    }

    init {
        if (DRAW_DEBUG_DATA) {
            debugRectPaint = Paint()
            debugRectPaint?.color = Color.RED
            debugRectPaint?.strokeWidth = 1F
            debugRectPaint?.style = Paint.Style.STROKE
            debugTextPaint = Paint()
            debugTextPaint?.color = Color.WHITE
            debugTextPaint?.style = Paint.Style.FILL
            debugTextPaint?.color = Color.BLACK
            debugTextPaint?.textSize = 20F
        }
    }
}

