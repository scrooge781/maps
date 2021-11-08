package com.oldmaps.newmaps.maps.ui.main_map.marker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import kotlinx.android.synthetic.main.bottom_sheet_add_marker.view.*

class MarkerBottomSheet : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the window no floating style
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppRoundedBottomSheetDialogThemeNoFloating)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_marker, container, false)

        val id = arguments?.getInt("marker_id")
        val lat = arguments?.getDouble("marker_lat")
        val lon = arguments?.getDouble("marker_lon")

        val title = view.editTextTitleMarker
        val desc = view.editTextDescriptionMarker

        view.idTextNumberMarker.text = (id!! + 1).toString()


        view.buttonSaveMarker.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle
                ?.set(
                    MARKER_KEY,
                    MarkerModel(
                        (id + 1),
                        lat,
                        lon,
                        title.text.toString(),
                        desc.text.toString()
                    )
                )
            dismiss()

        }

        return view
    }

    companion object {
        const val MARKER_KEY = "markerSaveData"
    }
}