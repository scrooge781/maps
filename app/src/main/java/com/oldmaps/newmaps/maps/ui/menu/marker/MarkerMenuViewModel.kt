package com.oldmaps.newmaps.maps.ui.menu.marker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import com.oldmaps.newmaps.maps.repo.MarkerMapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkerMenuViewModel @Inject constructor(
    private val markerRepo: MarkerMapRepository
) : ViewModel() {

    val allMarker: MutableLiveData<List<MarkerModel>> = MutableLiveData()

    fun getAllMaker() {
        viewModelScope.launch {
            allMarker.postValue(markerRepo.getAllMarker())
        }
    }

}