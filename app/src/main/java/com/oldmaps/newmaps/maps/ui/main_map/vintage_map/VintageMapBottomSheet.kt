package com.oldmaps.newmaps.maps.ui.main_map.vintage_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.ui.main_map.vintage_map.adapter.VintageMapAdapter
import com.oldmaps.newmaps.maps.ui.main_map.vintage_map.adapter.VintageMapModel
import kotlinx.android.synthetic.main.bottom_sheet_list_vintage_map.view.*

class VintageMapBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_list_vintage_map, container, false)

        val adapter = VintageMapAdapter()

        var list: ArrayList<VintageMapModel> = ArrayList()
        for (i in 0..15) {
            list.add(VintageMapModel(R.drawable.image_small_vm, "Name Map #${i}"))
        }
        adapter.addItems(list)

        view.vintage_map_list_view.adapter = adapter
        view.vintage_map_list_view.layoutManager = LinearLayoutManager(activity?.applicationContext)

        return view
    }

    companion object {
        const val TAG = "VintageMapModalBottomSheet"
    }
}