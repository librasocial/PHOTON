package com.ssf.homevisit.fragment.vhnd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.adapters.PhcStaffAdapter
import com.ssf.homevisit.controller.UIController
import com.ssf.homevisit.databinding.FragmentPhcStaffListBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.fragment.vhsnc.InvitationSentDialog
import com.ssf.homevisit.models.SpecialInvitee
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel

class PHCStaffListFragment : Fragment() {
    private lateinit var binding: FragmentPhcStaffListBinding
    private val viewModel: CreateMeetingViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initializerComponents(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNewMeetingUI()
        setupBottomBar(true)
    }

    private fun initNewMeetingUI() {
        val adapter = PhcStaffAdapter {

        }
        binding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recycler.adapter = adapter
        viewModel.phcStaff.observe(viewLifecycleOwner) { phcStaffReponse ->
            phcStaffReponse?.let {
                adapter.submitList(it.content)
            }
        }
    }

    private fun initializerComponents(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        binding = FragmentPhcStaffListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupBottomBar(enabled: Boolean = false) {
        binding.bottomBar.apply {
            mode = BottomBarMode.SEND_INVITE
            saveButton.isEnabled = enabled
            sendInvite.setOnClickListener {
                val invitees = viewModel.phcStaff.value?.content?.map {
                    SpecialInvitee(
                        MutableLiveData(it.targetNode.properties.name),
                        MutableLiveData(it.targetNode.properties.email),
                        MutableLiveData(it.targetNode.properties.contact)
                    )
                }
                invitees?.let {
                    viewModel.addPhcStaffInvitee(invitees)
                }
                InvitationSentDialog(requireContext()) {
                    viewModel.houseHoldId.value = ""
                    requireActivity().onBackPressed()
                }.show()
            }
            txtCancel.setOnClickListener { requireActivity().onBackPressed() }
        }
    }
}