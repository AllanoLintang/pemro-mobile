package com.allano.nongki

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LocationViewModel(application: Application) : AndroidViewModel(application){
    private val context = getApplication<Application>().applicationContext

    fun getLocations(): List<LocationModel> {
        val nama = context.resources.getStringArray(R.array.data_name)
        val deskripsi = context.resources.getStringArray(R.array.data_description)
        val rating = context.resources.getStringArray(R.array.data_rating)
        val link = context.resources.getStringArray(R.array.data_link)
        val fotoResId = listOf(
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5
        )

        return nama.indices.map { i ->
            LocationModel(
                nama[i],
                fotoResId[i],
                deskripsi[i],
                rating[i],
                link[i]
            )
        }
    }
}