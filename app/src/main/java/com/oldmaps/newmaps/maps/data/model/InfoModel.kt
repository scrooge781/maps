package com.oldmaps.newmaps.maps.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "info",
    primaryKeys = ["minzoom", "maxzoom"])
data class InfoModel(

    @ColumnInfo(name = "minzoom")
    var minzoom: Long,

    @ColumnInfo(name = "maxzoom")
    var maxzoom: Long
)