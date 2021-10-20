package com.oldmaps.newmaps.maps.ui.maps

import androidx.lifecycle.*
import com.oldmaps.newmaps.maps.data.model.InfoModel
import com.oldmaps.newmaps.maps.data.model.TilesModel
import com.oldmaps.newmaps.maps.repo.LocalMapsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapsMainViewModel @Inject constructor(
    private val repo: LocalMapsRepository
) : ViewModel() {

    val getAllInfo: MutableLiveData<List<TilesModel>> = MutableLiveData()
    val getInfoX: MutableLiveData<TilesModel> = MutableLiveData()
    val getZoom: MutableLiveData<InfoModel> = MutableLiveData()

    fun getDatabaseAll() {
        viewModelScope.launch {
            getAllInfo.postValue(repo.getAll())
        }
    }

    fun getInfoX(x: Int){
        viewModelScope.launch {
            getInfoX.postValue(repo.getInfoX(x))
        }
    }

    fun getInfoZoom(){
        viewModelScope.launch {
            getZoom.postValue(repo.getZoom())
        }
    }
}

