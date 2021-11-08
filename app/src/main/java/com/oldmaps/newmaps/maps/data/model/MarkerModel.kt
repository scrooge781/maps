package com.oldmaps.newmaps.maps.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "marker")
data class MarkerModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var lat: Double? = null,
    var lon: Double? = null,
    var title: String? = null,
    var description: String? = null

): Parcelable
