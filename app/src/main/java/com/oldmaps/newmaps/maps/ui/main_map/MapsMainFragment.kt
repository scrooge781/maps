package com.oldmaps.newmaps.maps.ui.main_map

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.slider.Slider

import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import com.oldmaps.newmaps.maps.databinding.FragmentMapsMainBinding
import com.oldmaps.newmaps.maps.ui.main_map.marker.MarkerBottomSheet
import com.oldmaps.newmaps.maps.util.Converting.bitmapDescriptorFromVector
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapsMainFragment : Fragment(R.layout.fragment_maps_main), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var tileOverlayTransparent: TileOverlay? = null
    private val viewModel: MapsMainViewModel by viewModels()
    private lateinit var binding: FragmentMapsMainBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsMainBinding.inflate(inflater, container, false)

        //Прозрачный фон Status bar
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        initViewModelObserve()


        binding.mapsVintageMap.setOnClickListener {
            findNavController().navigate(R.id.action_mapsMainFragment_to_vintageMapBottomSheet)
        }

        //saved marker data
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<MarkerModel>(MarkerBottomSheet.MARKER_KEY)
            ?.observe(viewLifecycleOwner, { markerData ->
                viewModel.saveMarker(markerData)
                setMarkerOnMap(markerData)
            })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap ?: return

        val initStartPoint = LatLng(50.45466, 30.5238)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initStartPoint))
        googleMap.mapType = GoogleMap.MAP_TYPE_NONE

        googleMap.setOnMapLongClickListener { latlng ->
            val bundle = Bundle()
            bundle.putDouble("marker_lat", latlng.latitude)
            bundle.putDouble("marker_lon", latlng.longitude)
            findNavController().navigate(R.id.action_mapsMainFragment_to_markerBottomSheet, bundle)
        }

        //initShowMarkers()
        viewModel.getAllMarker()

        // get tile vintage maps by local db
        viewModel.getTileCoordinate()

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

        viewModel.allMarker.observe(viewLifecycleOwner, { listMarker ->
            for (marker in listMarker) {
                setMarkerOnMap(marker)
            }
        })
    }

    private fun initShowMarkers() {
        when (sharedPreferences.getBoolean("showMarker", false)) {
            true -> viewModel.getAllMarker()
            else -> map.clear()
        }
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

    private fun setMarkerOnMap(marker: MarkerModel) {
        val latlng = LatLng(marker.lat!!, marker.lon!!)
        map.addMarker(
            MarkerOptions()
                .position(latlng)
                .title(marker.title)
                .icon(
                    bitmapDescriptorFromVector(
                        context!!,
                        R.drawable.ic_icon_marker,
                        marker.number!!
                    )
                )
        )
    }
}