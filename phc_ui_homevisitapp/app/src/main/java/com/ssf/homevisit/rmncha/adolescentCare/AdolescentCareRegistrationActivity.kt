package com.ssf.homevisit.rmncha.adolescentCare

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
import com.ssf.homevisit.databinding.ActivityAdolescentCareRegistrationBinding
import com.ssf.homevisit.models.PhcResponse
import com.ssf.homevisit.models.SubCVillResponse
import com.ssf.homevisit.models.SubcentersFromPHCResponse
import com.ssf.homevisit.models.TargetNodeCCHouseHoldProperties

class AdolescentCareRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdolescentCareRegistrationBinding
    private lateinit var navController: NavController
    private val viewModel: AdolescentCareViewModel by viewModels()

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityAdolescentCareRegistrationBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initComponents()
        setFragmentChangeListener()
    }

    private fun initComponents() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.adolescent_reg_nav_host_fragment) as NavHostFragment
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
            R.id.adolescentCareChildSelectionFragment -> {
                toolbar.path = "RMNCH+A > Adolescent Care > "
                toolbar.destination = "Select a Child for Adolescent Care"
            }
            R.id.adolescentCareRegisterFragment -> {
                toolbar.path = "RMNCH+A > Adolescent Care > "
                toolbar.destination = "Register the Child for Adolescent Care"
            }
        }
    }

    companion object {
        const val keyVillage = "village"
        const val keySubCenter = "subCenter"
        const val keyVhc = "phc"
        const val household = "household"

        fun getNewIntent(
            context: Context,
            villageProperties: SubCVillResponse.Content,
            subCenter: SubcentersFromPHCResponse.Content,
            phc: PhcResponse.Content,
            houseHold: TargetNodeCCHouseHoldProperties
        ) = Intent(context, AdolescentCareRegistrationActivity::class.java).apply {
            putExtra(keyVillage, villageProperties)
            putExtra(keySubCenter, subCenter)
            putExtra(keyVhc, phc)
            putExtra(household, houseHold)
        }
    }
}