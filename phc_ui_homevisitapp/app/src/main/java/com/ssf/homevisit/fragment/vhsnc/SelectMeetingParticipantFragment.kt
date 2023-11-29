package com.ssf.homevisit.fragment.vhsnc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.HouseHoldSelectionAdapter
import com.ssf.homevisit.adapters.InvitesAdapter
import com.ssf.homevisit.adapters.WrapperLayoutManager
import com.ssf.homevisit.databinding.FragmentSelectParticipantsBinding
import com.ssf.homevisit.utils.EndlessScrollRecyclListener
import com.ssf.homevisit.utils.VHSNC
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel
import com.ssf.homevisit.viewmodel.UIMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectMeetingParticipantFragment : Fragment() {

    private lateinit var binding: FragmentSelectParticipantsBinding
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
        binding = FragmentSelectParticipantsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNewMeetingUI()
        observeInvitees()
        observeData()
    }

    private fun observeInvitees() {
        val adapter = InvitesAdapter(viewModel.isVHND) {

        }
        val lManager = WrapperLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerInvited.layoutManager = lManager
        binding.recyclerInvited.adapter = adapter
        viewModel.recipients.observe(viewLifecycleOwner) {
            binding.emptyInvitedList.root.isVisible = it.isEmpty()
            setupBottomBar(it.isNotEmpty())
            adapter.submitList(it)
        }
    }

    private fun observeData() {
        val adapter = HouseHoldSelectionAdapter {
            val id = it.target.properties.uuid
            val toIndividualSelection = SelectMeetingParticipantFragmentDirections.meetingParticipantToIndividualSelection(id)
            findNavController().navigate(toIndividualSelection)
        }
        val lManager = WrapperLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.recycler.layoutManager = lManager
        binding.recycler.adapter = adapter
        viewModel.houseHoldsList.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.Main) {
                adapter.submitList(it)
                binding.emptyFullList.root.isVisible = it.isEmpty()
                adapter.notifyItemRangeChanged(0, it.size)
            }
        }
        binding.recycler.addOnScrollListener(object: EndlessScrollRecyclListener() {
            override fun onLoadMore() {
                viewModel.page.value = (viewModel.page.value?:0)+1
            }

            override fun isLoading(): Boolean {
                return false
            }

            override fun isLastPage(): Boolean {
                return viewModel.page.value == viewModel.totalPages
            }

        })
    }

    private fun initNewMeetingUI() {
        setupBottomBar(false)
        if (viewModel.isVHND) {
            binding.guideline32.setGuidelinePercent(.72f)
            binding.guideline22.setGuidelinePercent(.5f)
            binding.txtAction2.isVisible = false
        }
        binding.invitedCount.text = getString(R.string.total_invites, viewModel.getInviteesCount())
        binding.txtInvitedVHSNC.setOnClickListener {
            viewModel.listingMode.value = UIMode.INVITED_LIST
        }
        binding.txtShowCompleteHH.setOnClickListener {
            viewModel.listingMode.value = UIMode.FULL_LIST
        }
    }

    private fun setupBottomBar(enabled: Boolean = false) {
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                if(viewModel.getInviteesCount() > 0) {
//                    viewModel.updateMeeting().observe(viewLifecycleOwner) {
//                        viewModel.isLoading.value = false
//                        it?.let {
////                            requireActivity().onBackPressedDispatcher.onBackPressed()
//                            findNavController().navigateUp()
//                        }
//                    }
//                }
//            }
//        })
        binding.bottomBar.apply {
            mode = BottomBarMode.CONTINUE
            continueBtn.isEnabled = enabled
            continueBtn.setOnClickListener {
                viewModel.updateMeeting().observe(viewLifecycleOwner) {
                    viewModel.hideLoading()
                    it?.let {
                        requireActivity().onBackPressed()
                    }
                }
            }
            txtCancel.setOnClickListener { requireActivity().onBackPressed() }
        }
    }
}
