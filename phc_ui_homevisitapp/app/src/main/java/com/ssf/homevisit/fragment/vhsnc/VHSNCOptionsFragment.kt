package com.ssf.homevisit.fragment.vhsnc

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentVhsncOptionsBinding
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel
import com.ssf.homevisit.viewmodel.VHSNCOptionsViewModel
import com.ssf.homevisit.viewmodel.VHSNCViewModel

class VHSNCOptionsFragment : Fragment() {

    private val commonViewModel: CreateMeetingViewModel by activityViewModels()
    private val viewModel: VHSNCViewModel by activityViewModels()

    private lateinit var binding: FragmentVhsncOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        commonViewModel.clearData()
        binding = FragmentVhsncOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomBar()
        setupOptionClick()
    }

    private fun setupOptionClick() {
        with(binding) {
            monthlyMeeting.setOnClickListener {
                findNavController().navigate(VHSNCOptionsFragmentDirections.vhsncHomeToCreateMeeting())
            }
            unitedVillageFund.setOnClickListener {

            }
            unitedVillageHealthFund.setOnClickListener {

            }
            recordMaintenance.setOnClickListener {

            }
            monitorAccessToPublic.setOnClickListener {

            }
            localCollectiveAction.setOnClickListener {

            }
            deliveryInVillage.setOnClickListener {

            }
            monitorHealthService.setOnClickListener {

            }
            villageHealthPlanning.setOnClickListener {

            }
        }
    }

    private fun setupBottomBar() {
        binding.bottomBar.apply {
            mode = BottomBarMode.ONLY_BACK
            backText = getString(R.string.back)
            txtBack.setOnClickListener { this@VHSNCOptionsFragment.requireActivity().onBackPressed() }
        }
    }
}

enum class BottomBarMode(val id: Int) {
    SAVE_CONTINUE(1), FINISH(2), CONTINUE(3), SEND_INVITE(4), ONLY_BACK(5)
}