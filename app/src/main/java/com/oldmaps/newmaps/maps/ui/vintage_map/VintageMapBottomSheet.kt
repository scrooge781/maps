package com.oldmaps.newmaps.maps.ui.vintage_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.ui.vintage_map.adapter.VintageMapAdapter
import com.oldmaps.newmaps.maps.ui.vintage_map.adapter.VintageMapModel
import kotlinx.android.synthetic.main.bottom_sheet_list_vintage_map.view.*

class VintageMapBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_list_vintage_map, container, false)

        val adapter = VintageMapAdapter()
        var list: ArrayList<VintageMapModel> = ArrayList<VintageMapModel>()
        list.add(VintageMapModel(R.drawable.image_small_vm, "Name Map #1"))
        list.add(VintageMapModel(R.drawable.image_small_vm, "Name Map #2"))
        list.add(VintageMapModel(R.drawable.image_small_vm, "Name Map #3"))
        list.add(VintageMapModel(R.drawable.image_small_vm, "Name Map #4"))
        list.add(VintageMapModel(R.drawable.image_small_vm, "Name Map #5"))
        list.add(VintageMapModel(R.drawable.image_small_vm, "Name Map #6"))
        adapter.addItems(list)




        view.vintage_map_list_view.adapter = adapter
        view.vintage_map_list_view.layoutManager = LinearLayoutManager(activity?.applicationContext)

        return view
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}