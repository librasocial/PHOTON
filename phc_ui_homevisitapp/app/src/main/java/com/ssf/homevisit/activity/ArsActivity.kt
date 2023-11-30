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
import com.ssf.homevisit.databinding.ActivityArsBinding
import com.ssf.homevisit.viewmodel.ARSViewModel

class ArsActivity : AppCompatActivity() {

    private lateinit var view: ActivityArsBinding
    private lateinit var navController: NavController
    private val mViewModel: ARSViewModel by viewModels()

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
        view = ActivityArsBinding.inflate(layoutInflater).apply {
            viewModel = mViewModel
            lifecycleOwner = this@ArsActivity
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
        var path = "Programs > ARS > "
        var dest = ""
        when (destination.id) {
            R.id.ARSMeetingListFragment -> {
                path = "Programs > ARS > "
                dest = "Schedule or Conduct ARS Monthly Meeting"
            }
            R.id.createARSMeetingFragment -> {
                path = "Programs > ARS > "
                dest = "Schedule ARS Monthly Meeting"
            }
            R.id.ARSMeetingDetailFragment -> {
                path = "Programs > ARS > "
                dest = "Monthly Meeting Details"
            }
            R.id.ARSSpecialInviteeListFragment -> {
                path = "Programs > ARS > "
                dest =
                    if (mViewModel.isSpecialInviteeMode.value!!) "Add Special Invitees for ARS Meeting" else "Add ARS Committee Members for ARS Meeting"
            }
            R.id.ARSProcessMeetingFragment -> {
                path = "Programs > ARS > "
                dest = "Process Meeting"
            }
        }
        mViewModel.setToolbarTitle(path, dest)
    }

    companion object {
        fun newIntent(
            context: Context,
        ) = Intent(context, ArsActivity::class.java).apply {

        }
    }
}