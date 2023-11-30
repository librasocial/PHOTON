package com.ssf.homevisit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.WaterResourceCountAdapter
import com.ssf.homevisit.adapters.WaterSourceAdapter
import com.ssf.homevisit.databinding.FragmentWaterSourcesDetailsBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.collectSharedFlowData
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.ResourceCount
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.viewmodel.NaturalResourceViewModel

class WaterSourcesFragment : DialogFragment(), WaterSourceAdapter.OnItemClick,
    WaterResourceCountAdapter.OnCountCheck {
    private lateinit var binding: FragmentWaterSourcesDetailsBinding
    private lateinit var adapter: WaterSourceAdapter
    private lateinit var waterResourceCountAdapter: WaterResourceCountAdapter
    private val naturalResourceViewModel: NaturalResourceViewModel by activityViewModels()
    private val resourceCountList: MutableList<ResourceCount> = mutableListOf()
    private var optionSelectedHashMap: MutableSet<String?> = mutableSetOf()
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private val questionList: MutableList<QuesResponse> = mutableListOf()
    private lateinit var surveyId: String
    private var resourceNameList: MutableList<OptionsData> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaterSourcesDetailsBinding.inflate(layoutInflater, container, false)
        this.isCancelable = false
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initAdapter()
        initClick()
        initObserver()
    }


    private fun initObserver() {
        collectSharedFlowData(naturalResourceViewModel.saveResponseResult) {
            binding.btnSave.visibility = View.GONE
            binding.progressBar.visibility = View.GONE

            when (it) {
                is DataState.Error -> apiSuccessFailureDialog(failureButtonCLick = {
                    findNavController().navigate(R.id.naturalResourceFragment)
                })
                DataState.Loading -> {}
                is DataState.Success -> {
                    val bundle = Bundle()
                    bundle.putString(AppDefines.RETURN_FLOW, AppDefines.WATER_SOURCE)
                    naturalResourceViewModel.selectedSurveyTypeHashMap.add(1)
                    apiSuccessFailureDialog(titleText = "Form has been saved successfully!",
                        buttonText = "Click here to Exit",
                        isSuccess = true,
                        successButtonClick = {
                            findNavController().navigate(R.id.naturalResourceFragment, bundle)
                        })
                }
            }
        }

        collectSharedFlowData(naturalResourceViewModel.villageFormResponse) {
            when (it) {
                is DataState.Success -> {
                    surveyId = it.baseResponseData.data?.id.toString()
                    it.baseResponseData.data?.quesOptions?.let {
                        it.forEach {
                            resourceNameList.add(
                                OptionsData(
                                    question = it.question,
                                    choices = it.choices,
                                    propertyName = it.propertyName,
                                    isSelected = false
                                )
                            )
                        }
                        adapter.data = resourceNameList
                        adapter.notifyDataSetChanged()
                    }
                }
                else -> {}
            }
        }
    }

    private fun initClick() {
        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }
        binding.btnSave.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            naturalResourceViewModel.saveSurveyResponse(
                SaveSurvey(
                    conductedBy = larvaViewModel.villageName,
                    context = Context(
                        hId = "", villageId = larvaViewModel.villageUuid, memberId = ""
                    ),
                    quesResponse = questionList,
                    respondedBy = larvaViewModel.villageName,
                    surveyId = surveyId,
                    surveyName = AppDefines.WATER_SOURCE
                )
            )

        }
    }

    private fun initAdapter() {
        context?.let {
            waterResourceCountAdapter = WaterResourceCountAdapter(this)
            adapter = WaterSourceAdapter(it, this)
            binding.rvWaterSources.layoutManager = GridLayoutManager(context, 5)
            binding.rvWaterSources.adapter = adapter
            binding.rvResourcesCount.adapter = waterResourceCountAdapter
        }
    }

    private fun getData() {
        naturalResourceViewModel.fetchSurveyResponse(AppDefines.WATER_SOURCE)
    }

    override fun onItemClick(optionName: String?, position: Int, data: MutableList<OptionsData>,propertyName:String?) {
        binding.btnSave.let {
            it.isFocusable = true
            it.isClickable = true
            it.isEnabled = true
            it.background = context?.resources?.getDrawable(R.drawable.btn_normal)
        }
        if(optionName.equals("Piped Water") || optionName.equals("Other")){
            var isPresentFromBefore:Boolean=false
            questionList.forEach {
                if(it.question.equals(optionName)){
                    isPresentFromBefore=true
                    return@forEach
                }
            }
            if(!isPresentFromBefore){
                questionList.add(
                        QuesResponse(
                                propertyName = propertyName, question = optionName, response = mutableListOf()
                        )
                )
            }
            data[position].isSelected = !data[position].isSelected
            adapter.notifyItemChanged(position)
        }
        else{
        binding.tvResourcesCount.visible()
        if (!optionSelectedHashMap.contains(optionName)) {
            resourceCountList.add(ResourceCount(optionName, "", propertyName = propertyName))
            optionSelectedHashMap.add(optionName)
            data[position].isSelected = true
            waterResourceCountAdapter.data = resourceCountList
            waterResourceCountAdapter.notifyDataSetChanged()

        } else {
            var selectedPosition = -1
            resourceCountList.forEachIndexed { index, resourceCount ->
                if (resourceCount.menuItem.equals(optionName)) {
                    selectedPosition = index
                    return@forEachIndexed
                }
            }
            if (selectedPosition != -1) {
                resourceCountList.removeAt(selectedPosition)
                waterResourceCountAdapter.data = resourceCountList
                waterResourceCountAdapter.notifyDataSetChanged()
            }
            optionSelectedHashMap.remove(optionName)
            data[position].isSelected = false
        }
        adapter.notifyItemChanged(position)
    }}

    override fun onCountCheck(
        count: String,
        optionName: String?,
        position: Int,
        propertyName: String?
    ) {
        val responses: MutableList<String> = mutableListOf()
        responses.add(count)
        var isPresentFromBefore:Boolean=false
        questionList.forEach {
            if(it.question.equals(optionName)){
                it.response=responses
                isPresentFromBefore=true
                return@forEach
            }
        }
        if(!isPresentFromBefore){
            questionList.add(
                    QuesResponse(
                            propertyName = propertyName, question = optionName, response = responses
                    )
            )
        }
        resourceCountList[position].data=count
    }
}


