package com.oldmaps.newmaps.maps.ui.menu.marker

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import com.oldmaps.newmaps.maps.databinding.MenuFragmentMarkersBinding
import com.oldmaps.newmaps.maps.ui.menu.marker.adapter.MarkerMenuAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkerMenuFragment : Fragment(R.layout.menu_fragment_markers) {

    private val viewModel: MarkerMenuViewModel by viewModels()
    private lateinit var binding: MenuFragmentMarkersBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MenuFragmentMarkersBinding.inflate(inflater, container, false)

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