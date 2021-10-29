package com.oldmaps.newmaps.maps.ui.menu.modern_map

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import com.oldmaps.newmaps.maps.databinding.MenuFragmentModernMapBinding
import com.oldmaps.newmaps.maps.ui.menu.marker.adapter.MarkerMenuAdapter
import com.oldmaps.newmaps.maps.ui.menu.modern_map.adapter.ModerMapTypeAdapter
import com.oldmaps.newmaps.maps.ui.menu.modern_map.adapter.ModernMapAdapter
import com.oldmaps.newmaps.maps.ui.menu.modern_map.adapter.ModernMapModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModernMapFragment : Fragment(R.layout.menu_fragment_modern_map) {

    private lateinit var binding: MenuFragmentModernMapBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MenuFragmentModernMapBinding.inflate(inflater, container, false)

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        requireActivity().window.statusBarColor = Color.BLACK

        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initModernMap()
        initModernMapType()
        binding.backModernMap.setOnClickListener {
            findNavController().navigate(R.id.action_menu_modern_map_to_mapsMainFragment)
        }
    }


    private fun initModernMap(){
        val adapter = ModernMapAdapter()
        var list: ArrayList<ModernMapModel> = ArrayList()

        list.add(ModernMapModel(R.drawable.google_maps, "Google", true))

        adapter.addItems(list)
        binding.recyclerModernMap.adapter = adapter
        binding.recyclerModernMap.layoutManager =
            LinearLayoutManager(
                activity?.applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )

    }

    private fun initModernMapType(){
        val adapter = ModerMapTypeAdapter()
        var list: ArrayList<ModernMapModel> = ArrayList()

        list.add(ModernMapModel(R.drawable.ic_default, "Местность", true))
        list.add(ModernMapModel(R.drawable.ic_satellite, "Спутник", false))
        list.add(ModernMapModel(R.drawable.ic_terrain, "Гибрид", false))
        list.add(ModernMapModel(R.drawable.disable_element_modern_map, "Без карты", false))

        adapter.addItems(list)
        binding.recyclerModerMapType.adapter = adapter
        binding.recyclerModerMapType.layoutManager =
            LinearLayoutManager(
                activity?.applicationContext,
                LinearLayoutManager.HORIZONTAL,
                false
            )


    }
}