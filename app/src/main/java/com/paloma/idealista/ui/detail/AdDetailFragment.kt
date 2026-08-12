package com.paloma.idealista.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.paloma.idealista.databinding.FragmentAdDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.fragment.navArgs

@AndroidEntryPoint
class AdDetailFragment : Fragment() {

    private var _binding: FragmentAdDetailBinding? = null
    private val binding get() = _binding!!

    private val args: AdDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: load and display ad detail (next card)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}