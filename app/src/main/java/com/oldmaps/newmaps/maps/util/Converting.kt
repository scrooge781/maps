package com.oldmaps.newmaps.maps.util

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.oldmaps.newmaps.maps.data.model.model_vintage_map.TilesModel
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sinh


object Converting {

    private fun convertCartesianToSpherical(x: Double, y: Double, z: Double): LatLng {
        var n = 2.0.pow(z)
        var lon = (x / n) * 360.0 - 180.0
        var lat = atan(sinh(Math.PI - y / n * 2 * Math.PI)) * (180.0 / Math.PI)

        return LatLng(lat, lon)
    }

    fun averageCoordinate(listTilesMode: List<TilesModel>): LatLng {
        var sumX = 0.0
        var sumY = 0.0

        for (iterator in listTilesMode) {
            sumX += iterator.x.toDouble()
            sumY += iterator.y.toDouble()
        }

        var averageX = sumX / listTilesMode.size
        var averageY = sumY / listTilesMode.size
        var z = (17 - listTilesMode[1].z.toDouble())

        return convertCartesianToSpherical(averageX, averageY, z)
    }


    fun bitmapDescriptorFromVector(
        context: Context,
        vectorResId: Int,
        number: String,
        scale: Float
    ): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {

            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)


            val canvas = Canvas(bitmap)

            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            draw(canvas)

            // new antialised Paint
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = Color.BLACK
            paint.textSize = (17f * scale)

            drawCenter(canvas, paint, number)

            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private val r = Rect()

    private fun drawCenter(canvas: Canvas, paint: Paint, text: String) {
        canvas.getClipBounds(r)
        val cHeight = r.height()
        val cWidth = r.width()
        paint.textAlign = Paint.Align.CENTER
        paint.getTextBounds(text, 0, text.length, r)
        val x = cWidth / 2f - r.height() / 10
        val y = cHeight / 2f
        canvas.drawText(text, x, y, paint)
    }

}