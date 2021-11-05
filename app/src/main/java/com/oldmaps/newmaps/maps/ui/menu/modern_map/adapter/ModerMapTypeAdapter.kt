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
import android.content.SharedPreferences
import javax.inject.Inject


class ModerMapTypeAdapter(val sharedPreferences: SharedPreferences) :
    BaseRecyclerViewAdapter<ModernMapModel>() {

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
            getItem(0)?.focusable = false
            getItem(1)?.focusable = false
            getItem(2)?.focusable = false
            getItem(3)?.focusable = false
            notifyDataSetChanged()


            when (position) {
                0 -> sharedPreferences.edit().putInt("type_map", 0).apply()
                1 -> sharedPreferences.edit().putInt("type_map", 1).apply()
                2 -> sharedPreferences.edit().putInt("type_map", 2).apply()
                3 -> sharedPreferences.edit().putInt("type_map", 3).apply()
            }
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

            if (selectedPos == position || modernMap.focusable) {
                focusable.setBackgroundResource(R.drawable.enable_element_modern_map)
                title.setTextColor(Color.parseColor("#FFAF38"))
            } else {
                focusable.setBackgroundResource(R.drawable.disable_element_modern_map)
                title.setTextColor(Color.parseColor("#FF170E0D"))
            }
        }
    }

}