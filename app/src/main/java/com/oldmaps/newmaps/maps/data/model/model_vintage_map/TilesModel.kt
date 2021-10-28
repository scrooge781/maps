package com.oldmaps.newmaps.maps.data.model.model_vintage_map

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "tiles",
    primaryKeys = ["x", "y", "z", "s"]
)
data class TilesModel(

    @ColumnInfo(name = "x")
    var x: Long,

    @ColumnInfo(name = "y")
    var y: Long,

    @ColumnInfo(name = "z")
    var z: Long,

    @ColumnInfo(name = "s")
    var s: Long,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null

)