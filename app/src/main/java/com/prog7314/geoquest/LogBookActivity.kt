package com.prog7314.geoquest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LogBookActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var locationAdapter: LocationAdapter
    private val savedLocations = mutableListOf<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logbook)

        recyclerView = findViewById(R.id.recyclerViewLogBook)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Example data (you will later fetch from Room DB, Firebase, or API)
        savedLocations.add(
            Location(
                name = "Table Mountain",
                description = "Iconic landmark in Cape Town.",
                latitude = -33.9628,
                longitude = 18.4098,
                imageUrl = "https://example.com/tablemountain.jpg",
                visibility = "Public"
            )
        )

        savedLocations.add(
            Location(
                name = "Lion's Head",
                description = "Popular hiking spot.",
                latitude = -33.9363,
                longitude = 18.3890,
                imageUrl = "https://example.com/lionshead.jpg",
                visibility = "Private"
            )
        )

        locationAdapter = LocationAdapter(savedLocations)
        recyclerView.adapter = locationAdapter
    }
}