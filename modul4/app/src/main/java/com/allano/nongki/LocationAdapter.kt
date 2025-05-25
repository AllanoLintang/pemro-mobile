package com.allano.nongki

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.allano.nongki.databinding.ItemLocationBinding
import androidx.core.net.toUri

class LocationAdapter(
    private var items: List<LocationModel>,
    private val navController: NavController,
    private val viewModel: LocationViewModel
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateData(newItems: List<LocationModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item = items[position]
        holder.binding.textViewName.text = item.nama
        holder.binding.rating.text = item.rating
        holder.binding.imageView.setImageResource(item.fotoResId)

        holder.binding.buttonBrowser.setOnClickListener {
            val url = item.link
            Log.d("LocationAdapter", "Tombol browser ditekan untuk: ${item.nama}, URL: $url")
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            it.context.startActivity(intent)
        }

        holder.binding.buttonDetail.setOnClickListener {
            Log.d("LocationAdapter", "Tombol detail ditekan untuk: ${item.nama}")
            viewModel.selectLocation(item)
        }
    }

    override fun getItemCount() = items.size
}