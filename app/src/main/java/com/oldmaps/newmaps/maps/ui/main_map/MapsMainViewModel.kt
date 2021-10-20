package com.oldmaps.newmaps.maps.ui.main_map

import androidx.lifecycle.*
import com.google.android.gms.maps.model.TileProvider
import com.oldmaps.newmaps.maps.data.model.LatLonZoomModel
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import com.oldmaps.newmaps.maps.repo.CoordinateTileRepository
import com.oldmaps.newmaps.maps.repo.LocalMapsRepository
import com.oldmaps.newmaps.maps.repo.MarkerMapRepository
import com.oldmaps.newmaps.maps.util.Converting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapsMainViewModel @Inject constructor(
    private val repo: LocalMapsRepository,
    private val tileRepo: CoordinateTileRepository,
    private val markerRepo: MarkerMapRepository

) : ViewModel() {

    val tileProvider: MutableLiveData<TileProvider> = MutableLiveData()
    val centerVintageMap: MutableLiveData<LatLonZoomModel> = MutableLiveData()

    fun getTileCoordinate() {
        viewModelScope.launch {
            tileProvider.postValue(tileRepo)
            setCenterVintageMap()

        }
    }

    private fun setCenterVintageMap() {
        viewModelScope.launch {
            val zoomMap = (17 - repo.getZoom().maxzoom).toFloat()
            val listCoordinateByZoom = repo.getCoordByZoom(repo.getZoom().maxzoom.toInt())
            val LatLng = Converting.averageCoordinate(listCoordinateByZoom)
            centerVintageMap.postValue(LatLonZoomModel(zoomMap, LatLng))

        }
    }


    val markerSave: MutableLiveData<MarkerModel> = MutableLiveData()

    fun saveMarker(marker: MarkerModel) {
        viewModelScope.launch {
            markerRepo.saveMarker(marker)
        }
    }

    fun getMarker(id: Long) {
        viewModelScope.launch {
            markerSave.postValue(markerRepo.getMarkerById(id))
        }
    }

}

