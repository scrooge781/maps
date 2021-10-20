package com.oldmaps.newmaps.maps.ui.maps

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.databinding.FragmentMapsMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MapsMainFragment : Fragment(R.layout.fragment_maps_main) {

    private val detailViewModel: MapsMainViewModel by viewModels()
    private lateinit var binding: FragmentMapsMainBinding

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(50.45466, 30.5238)
        //googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Kiew"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsMainBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


       /* //testing button in room information--------------------------------------------------------

        binding.mapsVintageMap.setOnClickListener {
            detailViewModel.getDatabaseAll()
        }

        binding.mapsMyLocation.setOnClickListener {
            detailViewModel.getInfoX(305)
        }

        binding.mapsSearch.setOnClickListener {
            detailViewModel.getInfoZoom()
        }

        detailViewModel.getInfoX.observe(viewLifecycleOwner, {
            println("x: -> ${it.y} + ${it.toString()}")
            Log.d("INFO", "x = " + it.x + " y = " + it.y + " z = " + it.z)
        })

        detailViewModel.getAllInfo.observe(viewLifecycleOwner, {
            for (n in it) {
                Log.d("INFO", "x = " + n.x + " y = " + n.y + " z = " + n.z)
            }
        })

        detailViewModel.getZoom.observe(viewLifecycleOwner, {
            Log.d("INFOZoom", "min = " + it.minzoom + " max= " + it.maxzoom)
        })

        //------------------------------------------------------------------------------------------*/

    }
}