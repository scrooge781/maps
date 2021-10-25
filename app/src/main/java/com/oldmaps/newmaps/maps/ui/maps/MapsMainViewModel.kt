package com.oldmaps.newmaps.maps.ui.maps

import androidx.lifecycle.*
import com.google.android.gms.maps.model.TileProvider
import com.oldmaps.newmaps.maps.data.model.InfoModel
import com.oldmaps.newmaps.maps.data.model.TilesModel
import com.oldmaps.newmaps.maps.repo.CoordTileRepository
import com.oldmaps.newmaps.maps.repo.LocalMapsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapsMainViewModel @Inject constructor(
    private val repo: LocalMapsRepository,
    private val tileRepo: CoordTileRepository

) : ViewModel() {

    val getAllInfo: MutableLiveData<List<TilesModel>> = MutableLiveData()
    val getZoom: MutableLiveData<InfoModel> = MutableLiveData()
    val tileProvider: MutableLiveData<TileProvider> = MutableLiveData()

    fun getDatabaseAll() {
        viewModelScope.launch {
            getAllInfo.postValue(repo.getAll())
        }
    }

    fun getInfoZoom() {
        viewModelScope.launch {
            getZoom.postValue(repo.getZoom())
        }
    }

    fun getTileCoord(){
        viewModelScope.launch {
            tileProvider.postValue(tileRepo)
        }
    }

}

