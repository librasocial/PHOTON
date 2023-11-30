package com.ssf.homevisit.healthWellnessSurveillance

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.ssf.homevisit.R
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LarvaActivity : AppCompatActivity() {

    private val viewModel: LarvaViewModel by viewModels()
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_larvra)
        initArguments()
    }

    private fun initArguments() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)
        intent?.extras?.let {
            if (it.containsKey(AppDefines.VILLAGE_NAME)) {
                viewModel.villageName = it.getString(AppDefines.VILLAGE_NAME).toString()
            }
            if (it.containsKey(AppDefines.VILLAGE_LGD_CODE)) {
                viewModel.villageLgdCode = it.getString(AppDefines.VILLAGE_LGD_CODE).toString()
            }
            if (it.containsKey(AppDefines.VILLAGE_UUID)) {
                viewModel.villageUuid = it.getString(AppDefines.VILLAGE_UUID).toString()
            }
            if (it.containsKey(AppDefines.ID_TOKEN)) {
                viewModel.userId = it.getString(AppDefines.ID_TOKEN).toString()
            }
            if (it.containsKey(AppDefines.HOUSEHOLD_ID)) {
                viewModel.houseHoldId = it.getString(AppDefines.HOUSEHOLD_ID).toString()
            }
            if (it.containsKey(AppDefines.FLOW_TYPE)) {
                viewModel.flowType = it.getString(AppDefines.FLOW_TYPE).toString()
            }
        }
        if (viewModel.flowType == AppDefines.VILLAGE_LEVEL_SOCIO_ECONOMIC_SURVEY) {
            graph.setStartDestination(R.id.naturalResourceFragment)
        }
        if (viewModel.flowType == AppDefines.HNW_VILLAGE_SURVEILLANCE || viewModel.flowType == AppDefines.HNW_HOUSEHOLD_SURVEILLANCE) {
            graph.setStartDestination(R.id.selectSTFragment)
        }
        if (viewModel.flowType == AppDefines.HNW_INDIVIDUAL_SURVEILLANCE) {
            graph.setStartDestination(R.id.individualSurveillanceTypeFragment)
        }
        val navController = navHostFragment.navController
        navController.graph = graph
    }
}