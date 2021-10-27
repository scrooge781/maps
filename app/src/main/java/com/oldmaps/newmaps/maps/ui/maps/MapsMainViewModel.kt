package com.oldmaps.newmaps.maps.ui.maps

import androidx.lifecycle.*
import com.google.android.gms.maps.model.TileProvider
import com.oldmaps.newmaps.maps.data.model.CenterVintageMapModel
import com.oldmaps.newmaps.maps.data.model.TilesModel
import com.oldmaps.newmaps.maps.repo.CoordTileRepository
import com.oldmaps.newmaps.maps.repo.LocalMapsRepository
import com.oldmaps.newmaps.maps.util.Converting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapsMainViewModel @Inject constructor(
    private val repo: LocalMapsRepository,
    private val tileRepo: CoordTileRepository

) : ViewModel() {

    val getAllInfo: MutableLiveData<List<TilesModel>> = MutableLiveData()
    val tileProvider: MutableLiveData<TileProvider> = MutableLiveData()
    val centerVintageMap: MutableLiveData<CenterVintageMapModel> = MutableLiveData()

    fun getDatabaseAll() {
        viewModelScope.launch {
            getAllInfo.postValue(repo.getAll())
        }
    }

    fun getTileCoord() {
        viewModelScope.launch {
            tileProvider.postValue(tileRepo)
            setCenterVintageMap()

        }
    }

    fun setCenterVintageMap() {
        viewModelScope.launch {
            val zoomMap = (17 - repo.getZoom().maxzoom).toFloat()
            val listCoordinateByZoom = repo.getCoordByZoom(repo.getZoom().maxzoom.toInt())
            val LatLng = Converting.averageCoordinate(listCoordinateByZoom)
            centerVintageMap.postValue(CenterVintageMapModel(zoomMap, LatLng))

        }
    }

}

