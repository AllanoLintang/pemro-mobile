package com.allano.alquran.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allano.alquran.data.local.SurahEntity
import com.allano.alquran.databinding.ItemSurahBinding

class SurahAdapter(private val viewModel: SurahViewModel) :
    ListAdapter<SurahEntity, SurahAdapter.SurahViewHolder>(SurahDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurahViewHolder {
        val binding = ItemSurahBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurahViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SurahViewHolder, position: Int) {
        val surah = getItem(position)
        holder.bind(surah)
        holder.itemView.setOnClickListener {
            Log.d("SurahAdapter", "Item clicked: ${surah.englishName}")
            viewModel.onSurahClicked(surah)
        }
    }

    class SurahViewHolder(private val binding: ItemSurahBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(surah: SurahEntity) {
            binding.tvSurahNumber.text = surah.number.toString()
            binding.tvSurahName.text = surah.englishName
            binding.tvSurahTranslation.text = surah.englishNameTranslation
            binding.tvSurahInfo.text = "${surah.revelationType} - ${surah.numberOfAyahs} verses"
        }
    }
}

class SurahDiffCallback : DiffUtil.ItemCallback<SurahEntity>() {
    override fun areItemsTheSame(oldItem: SurahEntity, newItem: SurahEntity): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: SurahEntity, newItem: SurahEntity): Boolean {
        return oldItem == newItem
    }
}