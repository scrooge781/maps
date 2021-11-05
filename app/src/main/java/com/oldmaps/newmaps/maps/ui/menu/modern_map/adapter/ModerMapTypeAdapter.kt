package com.oldmaps.newmaps.maps.ui.menu.modern_map.adapter

import android.content.ClipData
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oldmaps.newmaps.maps.R
import com.oldmaps.newmaps.maps.util.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_list_modern_map_type.view.*
import android.content.ClipData.Item


class ModerMapTypeAdapter : BaseRecyclerViewAdapter<ModernMapModel>() {

    private var selectedPos = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_modern_map_type, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myHolder = holder as? MyViewHolder
        myHolder?.setUpView(modernMap = getItem(position))
        myHolder?.item?.setOnClickListener {
            selectedPos = position
            notifyDataSetChanged()
        }

        if(selectedPos == position){
            myHolder?.focusable?.setBackgroundResource(R.drawable.enable_element_modern_map)
            myHolder?.title?.setTextColor(Color.parseColor("#FFAF38"))
        } else {
            myHolder?.focusable?.setBackgroundResource(R.drawable.disable_element_modern_map)
            myHolder?.title?.setTextColor(Color.parseColor("#FF170E0D"))
        }


    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val item: ConstraintLayout = view.itemModerMapType
        val image: ImageView = view.moderMapType
        val title: TextView = view.textModerMapType
        val focusable: FrameLayout = view.grameLayoutModernMapType


        fun setUpView(modernMap: ModernMapModel?) {
            image.setImageResource(modernMap?.image!!)
            title.text = modernMap.title

            /*  if(modernMap.focusable){
                  focusable.setBackgroundResource(R.drawable.enable_element_modern_map)
                  title.setTextColor(Color.parseColor("#FFAF38"))
              }*/

        }
    }

}