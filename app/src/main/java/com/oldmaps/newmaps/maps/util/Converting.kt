package com.oldmaps.newmaps.maps.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.data.model.model_vintage_map.TilesModel
import kotlin.math.*

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
        number: String
    ): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)


            val canvas = Canvas(bitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = context.resources.getColor(R.color.black)
            paint.textSize = 20f
            paint.textAlign = Paint.Align.CENTER

            // draw text to the Canvas center
            // draw text to the Canvas center
            val boundsText = Rect()
            paint.getTextBounds(number, 0, number.length, boundsText)
            val x: Int = (bitmap.width - boundsText.width()) / 2
            val y: Int = (bitmap.height + boundsText.height()) / 2

            canvas.drawText(number, x.toFloat(), y.toFloat(), paint)

            draw(canvas)
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

}