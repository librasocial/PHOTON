package com.ssf.homevisit.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.ssf.homevisit.R
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.databinding.ActivityVhsncBinding
import com.ssf.homevisit.models.AllPhcResponse
import com.ssf.homevisit.models.SubCVillResponse
import com.ssf.homevisit.models.SubcentersFromPHCResponse
import com.ssf.homevisit.viewmodel.VHSNCViewModel

class VHSNCActivity : AppCompatActivity() {

    private lateinit var view: ActivityVhsncBinding
    private lateinit var navController: NavController
    private val mViewModel: VHSNCViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        AppController.getInstance().currentActivity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        view = ActivityVhsncBinding.inflate(layoutInflater).apply {
            viewModel = mViewModel
            lifecycleOwner = this@VHSNCActivity
        }
        setContentView(view.root)
        initComponents()
        setFragmentChangeListener()
    }

    private fun initComponents() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setFragmentChangeListener() {
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            updateToolbar(
                destination,
                arguments
            )
        }
    }

    private fun updateToolbar(destination: NavDestination, arguments: Bundle?) {
        val toolbar = view.toolbar
        when (destination.id) {
            R.id.vhsncHomeFragment -> {
                toolbar.path = "Programs > ${mViewModel.vhsncOrVhd.value} > "
                toolbar.destination = "Select an Activity for ${mViewModel.vhsncOrVhd.value}"
            }
            R.id.createMeetingFragment, R.id.newVHNDMeetingFragment -> {
                toolbar.path = "Programs > ${mViewModel.vhsncOrVhd.value} > "
                toolbar.destination = "Schedule or Conduct ${mViewModel.vhsncOrVhd.value} Monthly Meeting"
            }
            R.id.newMeetingFragment -> {
                toolbar.path = "Programs > ${mViewModel.vhsncOrVhd.value} > "
                toolbar.destination = "Schedule ${mViewModel.vhsncOrVhd.value} Monthly Meeting"
            }
            R.id.individualSelectionFragment -> {
                toolbar.path = "Programs > ${mViewModel.vhsncOrVhd.value} > "
                toolbar.destination = "Select an Individual and Invite for ${mViewModel.vhsncOrVhd.value} meeting"
            }
            R.id.meetingDetailFragment -> {
                toolbar.path = "Programs > ${mViewModel.vhsncOrVhd.value} > "
                toolbar.destination = "Monthly Meeting Details"
            }
            R.id.selectMeetingParticipantFragment -> {
                toolbar.path = "Programs > ${mViewModel.vhsncOrVhd.value} > "
                toolbar.destination = "Selection of Households to Invite Participants"
            }
            R.id.specialInviteeListFragment -> {
                toolbar.path = "Programs > ${mViewModel.vhsncOrVhd.value} > "
                toolbar.destination = "Add Special Invitees for ${mViewModel.vhsncOrVhd.value} Meeting"
            }
            R.id.reScheduleMeetingFragment -> {
                toolbar.path = "Programs > ${mViewModel.vhsncOrVhd.value} > "
                toolbar.destination = "Re-Schedule ${mViewModel.vhsncOrVhd.value} Monthly Meeting"
            }
            R.id.allInviteeListFragment -> {
                toolbar.path = "Programs > ${mViewModel.vhsncOrVhd.value} > "
                toolbar.destination = "Check List of All Invitees"
            }
            R.id.GPInviteeFragment -> {
                toolbar.path = "Programs > ${mViewModel.vhsncOrVhd.value} > "
                toolbar.destination = "Invite GP Members for ${mViewModel.vhsncOrVhd.value} Meeting"
            }
        }
    }

    companion object {
        const val keyVillage = "village"
        const val keySubCenter = "subCenter"
        const val keyVhc = "phc"
        const val keyVhsncOrVhd = "vhsncOrVhd"

        fun getNewIntent(
            context: Context,
            villageProperties: SubCVillResponse.Content,
            subCenter: SubcentersFromPHCResponse.Content,
            phc: AllPhcResponse.Content,
            vhsncOrvhd: String
        )= Intent(context, VHSNCActivity::class.java).apply {
            putExtra(keyVillage, villageProperties)
            putExtra(keySubCenter, subCenter)
            putExtra(keyVhc, phc)
            putExtra(keyVhsncOrVhd, vhsncOrvhd)
        }
    }
}