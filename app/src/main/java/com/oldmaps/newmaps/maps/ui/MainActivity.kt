package com.oldmaps.newmaps.maps.ui

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Switch
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.util.CollectionUtils.setOf
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.ui.main_map.MapsMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val mapsViewModel: MapsMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Прозрачный фон Status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN
        )

        init()
    }

    private fun init() {

        //убирает ActionBar
        supportActionBar?.hide()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menu_modern_map, R.id.menu_vintage_map, R.id.menu_marker_map, R.id.menu_setting
            ), drawer_layout
        )

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)


        val switchMarker =
            nav_view.getHeaderView(0).findViewById<SwitchMaterial>(R.id.show_marker_menu)

        when (sharedPreferences.getBoolean("showMarker", false)) {
            true -> switchMarker.isChecked = true
            else -> switchMarker.isChecked = false
        }

        switchMarker.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                true -> {
                    sharedPreferences.edit().putBoolean("showMarker", true).apply()
                    mapsViewModel.showMarkerOnMaps(true)
                }
                else -> {
                    sharedPreferences.edit().putBoolean("showMarker", false).apply()
                    mapsViewModel.showMarkerOnMaps(false)
                }
            }
        }
    }

    fun openCloseNavigationDrawer(view: View) {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i("TAG", "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("TAG", status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}