package com.oldmaps.newmaps.maps.ui.main_map.marker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import com.oldmaps.newmaps.maps.ui.main_map.MapsMainViewModel
import com.oldmaps.newmaps.maps.ui.main_map.vintage_map.adapter.VintageMapModel
import kotlinx.android.synthetic.main.bottom_sheet_add_marker.view.*
import java.lang.reflect.Array

class MarkerBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_marker, container, false)

        val lat = arguments?.getDouble("marker_lat")
        val lon = arguments?.getDouble("marker_lon")
        val number = view.editTextNumberMarker
        val title = view.editTextTitleMarker
        val desc = view.editTextDescriptionMarker

        view.buttonSaveMarker.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle
                ?.set(MARKER_KEY, MarkerModel(lat, lon, number.text.toString(), title.text.toString(), desc.text.toString()))
            dismiss()

        }

        return view
    }

    companion object {
        const val MARKER_KEY = "markerSaveData"
    }
}