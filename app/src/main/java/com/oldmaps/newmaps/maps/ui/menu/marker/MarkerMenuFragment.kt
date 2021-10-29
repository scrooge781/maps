package com.oldmaps.newmaps.maps.ui.menu.marker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import com.oldmaps.newmaps.maps.databinding.FragmentMapsMainBinding
import com.oldmaps.newmaps.maps.databinding.FragmentMarkerMenuBinding
import com.oldmaps.newmaps.maps.ui.main_map.vintage_map.adapter.VintageMapModel
import com.oldmaps.newmaps.maps.ui.menu.marker.adapter.MarkerMenuAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkerMenuFragment : Fragment(R.layout.fragment_marker_menu) {

    private val viewModel: MarkerMenuViewModel by viewModels()
    private lateinit var binding: FragmentMarkerMenuBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarkerMenuBinding.inflate(inflater, container, false)

        viewModel.getAllMaker()
        initViewModelObservle()
        binding.backMarkers.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun initViewModelObservle() {
        viewModel.allMarker.observe(viewLifecycleOwner, { listMarker ->
            val adapter = MarkerMenuAdapter()
            var list: ArrayList<MarkerModel> = ArrayList()
            for (marker in listMarker) {
                list.add(marker)
            }
            adapter.addItems(list)
            binding.recyclerViewMarkers.adapter = adapter
            binding.recyclerViewMarkers.layoutManager =
                LinearLayoutManager(activity?.applicationContext)
        })
    }

}