package com.ssf.homevisit.rmncha.childCare.immunization

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
import com.ssf.homevisit.databinding.ActivityChildCareImmunizationBinding
import com.ssf.homevisit.models.*
import com.ssf.homevisit.rmncha.childCare.registration.ChildCareRegistrationActivity

class ChildImmunizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChildCareImmunizationBinding
    private lateinit var navController: NavController
    private val viewModel: ChildImmunizationViewModel by viewModels()

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityChildCareImmunizationBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initComponents()
        setFragmentChangeListener()
    }

    private fun initComponents() {
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.child_imm_nav_host_fragment) as NavHostFragment
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
            R.id.childImmunizationFragment -> {
                toolbar.path = "RMNCH+A > Child Care > "
                toolbar.destination = "Provide Child Immunization Services"
            }
            R.id.childHistoryFragment -> {
                toolbar.path = "RMNCH+A > Child Care > "
                toolbar.destination = "View Immunization History"
            }
        }
    }

    companion object {
        const val keyVillage = "village"
        const val keySubCenter = "subCenter"
        const val keyVhc = "phc"
        const val childDetail = "childDetail"

        fun getNewIntent(
            context: Context,
            villageProperties: SubCVillResponse.Content,
            subCenter: SubcentersFromPHCResponse.Content,
            phc: PhcResponse.Content,
            childDetails: CcChildListContent
        ) = Intent(context, ChildImmunizationActivity::class.java).apply {
            putExtra(keyVillage, villageProperties)
            putExtra(keySubCenter, subCenter)
            putExtra(keyVhc, phc)
            putExtra(childDetail, childDetails)
        }
    }
}