package com.ssf.homevisit.rmncha.childCare.registration

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
import com.ssf.homevisit.databinding.ActivityChildCareRegistrationBinding
import com.ssf.homevisit.models.*

class ChildCareRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildCareRegistrationBinding
    private lateinit var navController: NavController
    private val viewModel: ChildCareRegistrationViewModel by viewModels()

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityChildCareRegistrationBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initComponents()
        setFragmentChangeListener()
    }

    private fun initComponents() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.child_reg_nav_host_fragment) as NavHostFragment
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
        val toolbar = binding.toolbar
        when (destination.id) {
            R.id.childRegistrationFragment -> {
                toolbar.path = "RMNCH+A > Baby Care > "
                toolbar.destination = "Register the Child for Immunization"
            }
        }
    }

    companion object {
        const val keyVillage = "village"
        const val keySubCenter = "subCenter"
        const val keyVhc = "phc"
        const val household = "household"
        const val childDetail = "childDetail"

        fun getNewIntent(
            context: Context,
            villageProperties: SubCVillResponse.Content,
            subCenter: SubcentersFromPHCResponse.Content,
            phc: PhcResponse.Content,
            houseHold: TargetNodeCCHouseHoldProperties? = null,
            childDetails: ChildInHouseContent
        ) = Intent(context, ChildCareRegistrationActivity::class.java).apply {
            putExtra(keyVillage, villageProperties)
            putExtra(keySubCenter, subCenter)
            putExtra(keyVhc, phc)
            putExtra(household, houseHold)
            putExtra(childDetail, childDetails)
        }
    }
}