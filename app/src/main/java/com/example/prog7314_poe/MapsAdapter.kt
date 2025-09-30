package com.example.prog7314_poe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prog7314_poe.models.UserMap

class MapsAdapter(
    val context: Context,
    val userMaps: List<UserMap>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<MapsAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userMap = userMaps[position]
        holder.itemView.setOnClickListener { onClickListener.onItemClick(position) }
        // Bind the userMap data to the view holder

        val textViewTitle = holder.itemView.findViewById<TextView>(android.R.id.text1)
        textViewTitle.text = userMap.title
    }

    override fun getItemCount()= userMaps.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
