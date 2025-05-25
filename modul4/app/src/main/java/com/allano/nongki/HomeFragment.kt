package com.allano.nongki

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.allano.nongki.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: LocationAdapter

    private val viewModel: LocationViewModel by viewModels {
        LocationViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LocationAdapter(emptyList(), findNavController(), viewModel)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.locations.collect { locations ->
                adapter.updateData(locations)
                Log.d("HomeFragment", "Data list diperbarui di UI")
            }
        }

        lifecycleScope.launch {
            viewModel.selectedLocation.collect { location ->
                location?.let {
                    navigateToDetail(it)
                }
            }
        }
    }

    private fun navigateToDetail(location: LocationModel) {
        val bundle = Bundle().apply {
            putInt("fotoResId", location.fotoResId)
            putString("nama", location.nama)
            putString("deskripsi", location.deskripsi)
        }
        Log.d("HomeFragment", "Navigasi ke detail dengan data: ${location.nama}")
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}