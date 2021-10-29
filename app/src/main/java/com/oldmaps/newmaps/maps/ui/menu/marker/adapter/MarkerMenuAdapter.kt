package com.oldmaps.newmaps.maps.ui.menu.marker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.data.model.MarkerModel
import com.oldmaps.newmaps.maps.util.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_list_markers.view.*

class MarkerMenuAdapter : BaseRecyclerViewAdapter<MarkerModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_markers, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myHolder = holder as? MyViewHolder
        myHolder?.setUpView(markerModel = getItem(position))
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        private val number: TextView = view.textImageViewMarkers
        private val title: TextView = view.textTitleMarkers
        private val desc: TextView = view.textDescMarkers


        fun setUpView(markerModel: MarkerModel?) {
            number.text = markerModel?.number
            title.text = markerModel?.title
            desc.text = markerModel?.description

        }
    }
}