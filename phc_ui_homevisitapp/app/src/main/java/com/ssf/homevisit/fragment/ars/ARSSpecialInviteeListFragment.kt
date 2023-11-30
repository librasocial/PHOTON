package com.ssf.homevisit.fragment.ars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.SpecialInviteeAdapter
import com.ssf.homevisit.adapters.WrapperLayoutManager
import com.ssf.homevisit.databinding.FragmentSpecialInviteeListBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.fragment.vhsnc.InvitationSentDialog
import com.ssf.homevisit.viewmodel.ARSViewModel

class ARSSpecialInviteeListFragment : Fragment() {

    private lateinit var binding: FragmentSpecialInviteeListBinding
    private val commonViewModel: ARSViewModel by activityViewModels()
    private lateinit var invitationSentDialog: InvitationSentDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpecialInviteeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        invitationSentDialog = InvitationSentDialog(requireContext()) {
            requireActivity().onBackPressed()
        }
        setupBottomBar()
        initViews()
    }

    private fun initViews() {
        binding.txtHeading.text =
            if (commonViewModel.isSpecialInviteeMode.value!!) "Add Special Invitees for ARS Meeting" else "Add ARS Committee Members for ARS Meeting"
        binding.addNewInvitee.setOnClickListener {
            if (commonViewModel.isSpecialInviteeMode.value!!) {
                commonViewModel.addNewSpecialInvitee()
            } else {
                commonViewModel.addNewCommiteeInvitee()
            }
        }
        val adapter = SpecialInviteeAdapter { invitee, position ->
            if (commonViewModel.isSpecialInviteeMode.value!!) {
                commonViewModel.removeInvitee(invitee, position)
            } else {
                commonViewModel.removeCommiteeInvitee(invitee, position)
            }
        }
        val layoutManager = WrapperLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
        if (commonViewModel.isSpecialInviteeMode.value!!) {
            commonViewModel.specialInvitees.observe(viewLifecycleOwner) {
                binding.recycler.isVisible = !it.isNullOrEmpty()
                binding.emptyState.root.isVisible = it.isNullOrEmpty()
                adapter.submitList(it)
            }
        } else {
            commonViewModel.arsCommInvitees.observe(viewLifecycleOwner) {
                binding.recycler.isVisible = !it.isNullOrEmpty()
                binding.emptyState.root.isVisible = it.isNullOrEmpty()
                adapter.submitList(it)
            }
        }
    }

    private fun setupBottomBar() {
        binding.bottomBar.apply {
            backText = getString(R.string.back)
            txtBack.setOnClickListener { requireActivity().onBackPressed() }
            mode = BottomBarMode.SEND_INVITE
            sendInvite.setOnClickListener {
                commonViewModel.updateMeeting().observe(viewLifecycleOwner) {
                    it?.let {
                        commonViewModel.setLoading(false)
                        if (!invitationSentDialog.isShowing) {
                            invitationSentDialog.show()
                        }
                    }
                }
            }
            txtBack.setOnClickListener { requireActivity().onBackPressed() }
        }
    }
}