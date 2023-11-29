package com.ssf.homevisit.fragment.vhsnc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.AllMeetingParticipantAdapter
import com.ssf.homevisit.databinding.FragmentAllInviteeListBinding
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel

class AllInviteeListFragment : Fragment() {
    private lateinit var binding: FragmentAllInviteeListBinding
    private val viewModel: CreateMeetingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initializerComponents(inflater, container)
        return binding.root
    }

    private fun initializerComponents(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        binding = FragmentAllInviteeListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAllInviteeListUI()
        filterInviteeList(ListMode.Participants)
    }

    private fun setParticipantList(type: ListMode) {
        val adapter = AllMeetingParticipantAdapter()
        binding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recycler.adapter = adapter
        viewModel.getAllParticipants(type).observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.emptyState.root.isVisible = it.isNullOrEmpty()
        }
    }

    private fun initAllInviteeListUI() {
        binding.btnParticipants.setOnClickListener {
            filterInviteeList(ListMode.Participants)
        }
        binding.btnSpecialInvitee.setOnClickListener {
            filterInviteeList(ListMode.SpecialInvitees)
        }
        binding.btnGPMembers.setOnClickListener {
            filterInviteeList(ListMode.GPMembers)
        }

        binding.bottomBar.apply {
            mode = BottomBarMode.ONLY_BACK
            backText = getString(R.string.back)
            txtBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun filterInviteeList(filterFor: ListMode) {
        setParticipantList(filterFor)
        val colorGrey = ContextCompat.getColor(requireContext(), R.color.selected_grey)
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.white)
        when (filterFor) {
            ListMode.Participants -> {
                binding.btnParticipants.apply {
                    setBackgroundResource(R.drawable.bg_tab_btn)
                    setTextColor(colorWhite)
                }
                binding.btnSpecialInvitee.apply {
                    setTextColor(colorGrey)
                    setBackgroundResource(R.drawable.bg_tab_btn_unchecked)
                }
                binding.btnGPMembers.apply {
                    setTextColor(colorGrey)
                    setBackgroundResource(R.drawable.bg_tab_btn_unchecked)
                }
            }
            ListMode.SpecialInvitees -> {
                binding.btnSpecialInvitee.apply {
                    setBackgroundResource(R.drawable.bg_tab_btn)
                    setTextColor(colorWhite)
                }
                binding.btnParticipants.apply {
                    setTextColor(colorGrey)
                    setBackgroundResource(R.drawable.bg_tab_btn_unchecked)
                }
                binding.btnGPMembers.apply {
                    setTextColor(colorGrey)
                    setBackgroundResource(R.drawable.bg_tab_btn_unchecked)
                }

            }
            ListMode.GPMembers -> {
                binding.btnGPMembers.apply {
                    setBackgroundResource(R.drawable.bg_tab_btn)
                    setTextColor(colorWhite)
                }
                binding.btnParticipants.apply {
                    setTextColor(colorGrey)
                    setBackgroundResource(R.drawable.bg_tab_btn_unchecked)
                }
                binding.btnSpecialInvitee.apply {
                    setTextColor(colorGrey)
                    setBackgroundResource(R.drawable.bg_tab_btn_unchecked)
                }
            }
        }
    }
}

enum class ListMode {
    Participants, SpecialInvitees, GPMembers
}

data class AllInviteeItem(
    val name: String,
    val email: String?,
    val contact: String?
)