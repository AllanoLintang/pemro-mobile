package com.allano.nongki

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val _locations = MutableStateFlow<List<LocationModel>>(emptyList())
    val locations: StateFlow<List<LocationModel>> = _locations.asStateFlow()

    private val _selectedLocation = MutableStateFlow<LocationModel?>(null)
    val selectedLocation: StateFlow<LocationModel?> = _selectedLocation.asStateFlow()

    init {
        loadLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
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

            val locationsList = nama.indices.map { i ->
                LocationModel(
                    nama[i],
                    fotoResId[i],
                    deskripsi[i],
                    rating[i],
                    link[i]
                ).also {
                    Log.d("LocationViewModel", "Data item ditambahkan: ${it.nama}")
                }
            }

            _locations.value = locationsList
            Log.d("LocationViewModel", "Data list selesai dimuat, total: ${locationsList.size} item")
        }
    }

    fun selectLocation(location: LocationModel) {
        _selectedLocation.value = location
        Log.d("LocationViewModel", "Item dipilih: ${location.nama}")
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                return LocationViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}