package com.allano.alquran.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.allano.alquran.databinding.FragmentHomeBinding
import androidx.fragment.app.activityViewModels

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SurahViewModel by activityViewModels {
        SurahViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val surahAdapter = SurahAdapter(viewModel)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = surahAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            binding.progressBar.isVisible = true

            viewModel.surahs.collectLatest { surahs ->
                Log.d("HomeFragment", "Updating UI with ${surahs.size} surahs.")

                binding.progressBar.isVisible = false

                if (surahs.isNotEmpty()) {
                    binding.tvError.isVisible = false
                    surahAdapter.submitList(surahs)
                } else {
                    binding.tvError.isVisible = true
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigateToDetail.collect { surah ->
                surah?.let {
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it.number)
                    findNavController().navigate(action)
                    viewModel.onDetailNavigated()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}