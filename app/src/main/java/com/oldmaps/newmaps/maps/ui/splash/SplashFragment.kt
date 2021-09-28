package com.oldmaps.newmaps.maps.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.oldmaps.newmaps.maps.R

class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        findNavController().navigate(R.id.action_splashFragment_to_mapsMainFragment)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}