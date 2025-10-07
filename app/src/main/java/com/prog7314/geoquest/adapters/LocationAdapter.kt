package com.prog7314.geoquest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LocationAdapter(private val locationList: List<Location>) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val locationImage: ImageView = itemView.findViewById(R.id.locationImage)
        val locationName: TextView = itemView.findViewById(R.id.locationName)
        val locationDescription: TextView = itemView.findViewById(R.id.locationDescription)
        val locationCoords: TextView = itemView.findViewById(R.id.locationCoords)
        val locationVisibility: TextView = itemView.findViewById(R.id.locationVisibility)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locationList[position]
        holder.locationName.text = location.name
        holder.locationDescription.text = location.description
        holder.locationCoords.text = "Lat: ${location.latitude}, Lng: ${location.longitude}"
        holder.locationVisibility.text = "Visibility: ${location.visibility}"

        Glide.with(holder.itemView.context)
            .load(location.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.locationImage)
    }

    override fun getItemCount(): Int = locationList.size
}