package com.oldmaps.newmaps.maps.ui.vintage_map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.util.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_list_vitage_map.view.*

class VintageMapAdapter : BaseRecyclerViewAdapter<VintageMapModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_vitage_map, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myHolder = holder as? MyViewHolder
        myHolder?.setUpView(vm = getItem(position))
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        private val imageView: ImageView = view.small_image_vm
        private val textView: TextView = view.text_name_vm


        fun setUpView(vm: VintageMapModel?) {
            imageView.setImageResource(vm?.imageSmallVintageMap!!)
            textView.text = vm.textNameVitageMap
        }
    }
}
