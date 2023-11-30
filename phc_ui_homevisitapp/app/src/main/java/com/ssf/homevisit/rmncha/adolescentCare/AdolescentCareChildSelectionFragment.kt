package com.ssf.homevisit.rmncha.adolescentCare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentAdolescentCareChildSelectionBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.rmncha.childCare.mapping.HouseholdMemberAdapter

class AdolescentCareChildSelectionFragment : Fragment() {
    private var _binding: FragmentAdolescentCareChildSelectionBinding? = null
    private val viewModel: AdolescentCareViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdolescentCareChildSelectionBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListAdapter()
        setupBottomBar()
    }

    private fun setListAdapter() {
        val adapter = HouseholdMemberAdapter {
            viewModel.selectedChild.value = it
            val toChildRegister =
                AdolescentCareChildSelectionFragmentDirections.toAdolescentRegisterFragment()
            findNavController().navigate(toChildRegister)
        }
        binding.childrenList.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        binding.childrenList.adapter = adapter
        viewModel.childInHouseHold.observe(viewLifecycleOwner) {
            viewModel.setLoading(false)
            it?.let {
                adapter.submitList(it.content)
                binding.emptyState.root.isVisible = it.content.isEmpty()
            } ?: kotlin.run {
                binding.emptyState.root.isVisible = true
            }
        }
    }

    private fun setupBottomBar() {
        binding.bottomBar.mode = BottomBarMode.ONLY_BACK
        binding.bottomBar.backText = getString(R.string.back)
        binding.bottomBar.txtBack.setOnClickListener { requireActivity().onBackPressed() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}