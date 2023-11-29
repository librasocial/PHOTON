package com.ssf.homevisit.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssf.homevisit.databinding.FragmentPulsePolioBinding
import com.ssf.homevisit.viewmodel.PulsePolioViewModel

class PulsePolioFragment : Fragment() {

    companion object {
        fun newInstance() = PulsePolioFragment()
    }

    private lateinit var viewModel: PulsePolioViewModel
    private lateinit var binding: FragmentPulsePolioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPulsePolioBinding.inflate(inflater, container, false)
        binding.toolbar.path = "Programs > Pulse Polio > "
        binding.toolbar.destination = "Select a Day for Pulse Polio Programme"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PulsePolioViewModel::class.java]

    }
}