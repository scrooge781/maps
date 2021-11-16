package com.oldmaps.newmaps.maps.ui.main_map

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.slider.Slider

import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import com.oldmaps.newmaps.maps.databinding.FragmentMapsMainBinding
import com.oldmaps.newmaps.maps.ui.main_map.marker.MarkerBottomSheet
import com.oldmaps.newmaps.maps.util.Converting.bitmapDescriptorFromVector
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MapsMainFragment : Fragment(R.layout.fragment_maps_main), OnMapReadyCallback {

    private var map: GoogleMap? = null
    private var tileOverlayTransparent: TileOverlay? = null
    private val viewModel: MapsMainViewModel by viewModels()
    private lateinit var binding: FragmentMapsMainBinding
    private var markerId: Int = 0

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
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        if (!Places.isInitialized()) {
            Places.initialize(context, getString(R.string.google_api_key), Locale.US);
        }

        initViewModelObserve()


        binding.mapsVintageMap.setOnClickListener {
            findNavController().navigate(R.id.action_mapsMainFragment_to_vintageMapBottomSheet)
        }

        binding.mapsSearch.setOnClickListener {

            var fields=Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG)
            var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(context)
            activity?.startActivityForResult(intent, 1)


        }

        //saved marker data
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<MarkerModel>(MarkerBottomSheet.MARKER_KEY)
            ?.observe(viewLifecycleOwner, { markerData ->
                viewModel.saveMarker(markerData)
                setMarkerOnMap(markerData)

                //update info markers
                viewModel.getAllMarker()
            })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap ?: return

        val initStartPoint = LatLng(50.45466, 30.5238)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initStartPoint))
        googleMap.uiSettings.isRotateGesturesEnabled = false


        googleMap.setOnMapLongClickListener { latlng ->
            val bundle = Bundle()
            bundle.putDouble("marker_lat", latlng.latitude)
            bundle.putDouble("marker_lon", latlng.longitude)
            bundle.putInt("marker_id", markerId)
            findNavController().navigate(R.id.action_mapsMainFragment_to_markerBottomSheet, bundle)
        }

        initShowMarkers()
        //viewModel.getAllMarker()

        // get tile vintage maps by local db
        viewModel.getTileCoordinate()

        //get map type
        viewModel.getMapType()


    }

    private fun initViewModelObserve() {
        //set tile vintage map by local db
        viewModel.tileProvider.observe(viewLifecycleOwner, { tile ->
            tileOverlayTransparent =
                map?.addTileOverlay(TileOverlayOptions().tileProvider(tile).transparency(0.0f))
        })


        //set center vintage map
        viewModel.centerVintageMap.observe(viewLifecycleOwner, {
            val centerVintageMap: CameraPosition = CameraPosition.Builder()
                .target(it.latlng!!)
                .zoom(it.maxZoomMap!!)
                .build()

            //map.setMaxZoomPreference(it.minZoomMap!!)
            map?.moveCamera(CameraUpdateFactory.newCameraPosition(centerVintageMap))
            slideTransparently(true)
        })

        viewModel.allMarker.observe(viewLifecycleOwner, { listMarker ->

            if (!listMarker.isEmpty()) {
                markerId = listMarker.get(listMarker.size - 1).id
                for (marker in listMarker) {
                    setMarkerOnMap(marker)
                }
            }
        })

        //set map type
        viewModel.typeMap.observe(viewLifecycleOwner, { mapType ->
            map?.mapType = mapType
        })

        viewModel.showMarker.observe(viewLifecycleOwner, { visibility ->
            when(visibility){
                true -> viewModel.getAllMarker()
                else -> map?.clear()
            }

        })

    }

    private fun initShowMarkers() {
        when (sharedPreferences.getBoolean("showMarker", false)) {
            true -> viewModel.getAllMarker()
            else -> map?.clear()
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
        val scale = resources.displayMetrics.density
        map?.addMarker(
            MarkerOptions()
                .position(latlng)
                .title(marker.title)
                .icon(
                    bitmapDescriptorFromVector(
                        context!!,
                        R.drawable.ic_icon_marker,
                        marker.id.toString(),
                        scale
                    )
                )
        )
    }
}

