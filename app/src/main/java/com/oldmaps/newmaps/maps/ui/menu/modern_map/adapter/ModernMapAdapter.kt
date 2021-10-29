package com.oldmaps.newmaps.maps.ui.menu.modern_map.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.util.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_list_modern_map.view.*
import kotlinx.android.synthetic.main.item_list_modern_map_type.view.*

class ModernMapAdapter : BaseRecyclerViewAdapter<ModernMapModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_modern_map, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myHolder = holder as? MyViewHolder
        myHolder?.setUpView(modernMap = getItem(position))
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        private val image: ImageView = view.modernMap
        private val title: TextView = view.nameModerMap
        private val focusable: FrameLayout = view.frameLayoutModernMap



        fun setUpView(modernMap: ModernMapModel?) {
            image.setImageResource(modernMap?.image!!)
            title.text = modernMap.title
            if(modernMap.focusable){
                focusable.setBackgroundResource(R.drawable.enable_element_modern_map)
                title.setTextColor(Color.parseColor("#FFAF38"))
            }

        }
    }
}