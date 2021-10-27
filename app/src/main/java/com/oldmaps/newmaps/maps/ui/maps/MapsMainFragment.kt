package com.oldmaps.newmaps.maps.ui.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.slider.Slider

import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.databinding.FragmentMapsMainBinding
import com.oldmaps.newmaps.maps.ui.vintage_map.VintageMapBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsMainFragment : Fragment(R.layout.fragment_maps_main), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var tileOverlayTransparent: TileOverlay? = null
    private val viewModel: MapsMainViewModel by viewModels()
    private lateinit var binding: FragmentMapsMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        initViewModelObserve()

        binding.mapsVintageMap.setOnClickListener {
            val modalBottomSheet = VintageMapBottomSheet()
            modalBottomSheet.show(childFragmentManager, VintageMapBottomSheet.TAG)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap ?: return

        val initStartPoint = LatLng(50.45466, 30.5238)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initStartPoint))


        // get tile vintage maps by local db
        viewModel.getTileCoord()

    }

    private fun initViewModelObserve() {
        //set tile vintage map by local db
        viewModel.tileProvider.observe(viewLifecycleOwner, { tile ->
            tileOverlayTransparent =
                map.addTileOverlay(TileOverlayOptions().tileProvider(tile).transparency(0.0f))
        })


        //set center vintage map
        viewModel.centerVintageMap.observe(viewLifecycleOwner, {
            val centerVintageMap: CameraPosition = CameraPosition.Builder()
                .target(it.latlng!!)
                .zoom(it.zoom!!)
                .build()

            map.moveCamera(CameraUpdateFactory.newCameraPosition(centerVintageMap))
            slideTransparently(true)
        })
    }

    private fun slideTransparently(visible: Boolean) {
        when (visible) {
            true -> {
                binding.sliderTransparency.visibility = View.VISIBLE
                // set transparency vintage map
                binding.sliderTransparency.addOnSliderTouchListener(object :
                    Slider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: Slider) {
                    }

                    override fun onStopTrackingTouch(slider: Slider) {
                        tileOverlayTransparent?.transparency = 1 - slider.value / 100
                    }

                })
            }
            else -> binding.sliderTransparency.visibility = View.GONE
        }

    }
}