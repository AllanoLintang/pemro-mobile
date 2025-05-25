package com.allano.nongki

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.allano.nongki.databinding.FragmentDetailBinding

class DetailFragment: Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nama = arguments?.getString("nama") ?: "Tidak ada nama"
        val deskripsi = arguments?.getString("deskripsi") ?: "Tidak ada deskripsi"
        val fotoResId = arguments?.getInt("fotoResId") ?: R.drawable.image5

        Log.d("DetailFragment", "Menampilkan detail untuk: $nama")
        Log.d("DetailFragment", "Deskripsi: $deskripsi")
        Log.d("DetailFragment", "Foto Resource ID: $fotoResId")

        binding.imageViewDetail.setImageResource(fotoResId)
        binding.titleViewDetail.text = nama
        binding.textViewDetail.text = deskripsi
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}