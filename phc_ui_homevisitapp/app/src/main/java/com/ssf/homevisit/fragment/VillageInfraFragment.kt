package com.ssf.homevisit.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.EducationalAdapter
import com.ssf.homevisit.adapters.FinancialAdapter
import com.ssf.homevisit.databinding.FragmentPhysicalInfraBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.collectSharedFlowData
import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.viewmodel.NaturalResourceViewModel

class VillageInfraFragment : DialogFragment(), EducationalAdapter.OnEducationItemClick,
    FinancialAdapter.OnFinanceItemClick {

    private lateinit var binding: FragmentPhysicalInfraBinding
    private lateinit var educationalAdapter: EducationalAdapter
    private lateinit var financialAdapter: FinancialAdapter
    private val naturalResourceViewModel: NaturalResourceViewModel by activityViewModels()
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private lateinit var surveyId: String
    private val questionList: MutableList<QuesResponse> = mutableListOf()
    private lateinit var educationalList: MutableList<Options>
    private lateinit var financialList: MutableList<Options>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhysicalInfraBinding.inflate(layoutInflater, container, false)
        this.isCancelable = false
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            setLayout(500, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initAdapter()
        initClick()
        initObserver()
    }

    private fun initAdapter() {
        context?.let {
            educationalAdapter = EducationalAdapter(this)
            financialAdapter = FinancialAdapter(this)
            binding.rvEducationalList.adapter = educationalAdapter
            binding.rvFinancialList.adapter = financialAdapter
        }
    }


    private fun initObserver() {
        collectSharedFlowData(naturalResourceViewModel.saveResponseResult) {
            binding.progressBar.visibility = View.GONE

            when (it) {
                is DataState.Error -> apiSuccessFailureDialog(failureButtonCLick = {
                    Toast.makeText(
                        context, "Response  Saved Failed", Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.naturalResourceFragment)
                })
                DataState.Loading -> {}
                is DataState.Success -> {
                    val bundle = Bundle()
                    bundle.putString(AppDefines.RETURN_FLOW, AppDefines.PHYSICAL_INFRA)
                    naturalResourceViewModel.selectedSurveyTypeHashMap.add(2)
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
                        educationalAdapter.data = it.subList(2, 6)
                        educationalList = it.subList(2, 6) as MutableList<Options>
                        educationalAdapter.notifyDataSetChanged()
                        financialAdapter.data = it.subList(6, 10)
                        financialList = it.subList(6, 10) as MutableList<Options>
                        financialAdapter.notifyDataSetChanged()
                        binding.radioGroup1.tvFacilityName.text = it[10].question
                        binding.radioGroup2.tvFacilityName.text = it[11].question
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
                    surveyName = AppDefines.PHYSICAL_INFRA
                )
            )
        }
        binding.yesRadioButton.setOnClickListener {
            var responses = mutableListOf<String>()
            if (questionList.size == 0) {
                responses.add("yes")
                questionList.add(
                    QuesResponse(
                        question = "Village Connected to pucca Road",
                        propertyName = "villageConnectedToTheAbovePukkaRoad",
                        response = responses
                    )
                )
            } else {
                var isRepeated = false
                questionList.forEach {
                    if (it.question == "Village Connected to pucca Road") {
                        isRepeated = true
                        responses = mutableListOf()
                        responses.add("yes")
                        it.response = responses
                        return@forEach
                    }
                }
                if (!isRepeated) {
                    responses.add("yes")
                    questionList.add(
                        QuesResponse(
                            propertyName = "villageConnectedToTheAbovePukkaRoad",
                            response = responses,
                            question = "Village Connected to pucca Road"
                        )
                    )
                }
            }
        }
        binding.noRadioButton.setOnClickListener {
            var responses = mutableListOf<String>()
            if (questionList.size == 0) {
                responses.add("no")
                questionList.add(
                    QuesResponse(
                        question = "Village Connected to pucca Road",
                        propertyName = "villageConnectedToTheAbovePukkaRoad",
                        response = responses
                    )
                )
            } else {
                var isRepeated = false
                questionList.forEach {
                    if (it.question == "Village Connected to pucca Road") {
                        isRepeated = true
                        responses = mutableListOf()
                        responses.add("no")
                        it.response = responses
                        return@forEach
                    }
                }
                if (!isRepeated) {
                    responses.add("no")
                    questionList.add(
                        QuesResponse(
                            propertyName = "villageConnectedToTheAbovePukkaRoad",
                            response = responses,
                            question = "Village Connected to pucca Road"
                        )
                    )
                }
            }
        }
        binding.radioGroup1.radioYes.setOnClickListener {
            var responses = mutableListOf<String>()
            if (questionList.size == 0) {
                responses.add("yes")
                questionList.add(
                    QuesResponse(
                        question = "Agriculture Producers Marekting Co-operation Society",
                        propertyName = "agricultureMarketingCo-operationSociety",
                        response = responses
                    )
                )
            } else {
                var isRepeated = false
                questionList.forEach {
                    if (it.question == "Agriculture Producers Marekting Co-operation Society") {
                        isRepeated = true
                        responses = mutableListOf()
                        responses.add("yes")
                        it.response = responses
                        return@forEach
                    }
                }
                if (!isRepeated) {
                    responses.add("yes")
                    questionList.add(
                        QuesResponse(
                            propertyName = "agricultureMarketingCo-operationSociety",
                            response = responses,
                            question = "Agriculture Producers Marekting Co-operation Society"
                        )
                    )
                }
            }
        }
        binding.radioGroup1.radioNo.setOnClickListener {
            var responses = mutableListOf<String>()
            if (questionList.size == 0) {
                responses.add("no")
                questionList.add(
                    QuesResponse(
                        question = "Agriculture Producers Marekting Co-operation Society",
                        propertyName = "agricultureMarketingCo-operationSociety",
                        response = responses
                    )
                )
            } else {
                var isRepeated = false
                questionList.forEach {
                    if (it.question == "Agriculture Producers Marekting Co-operation Society") {
                        isRepeated = true
                        responses = mutableListOf()
                        responses.add("no")
                        it.response = responses
                        return@forEach
                    }
                }
                if (!isRepeated) {
                    responses.add("no")
                    questionList.add(
                        QuesResponse(
                            propertyName = "agricultureMarketingCo-operationSociety",
                            response = responses,
                            question = "Agriculture Producers Marekting Co-operation Society"
                        )
                    )
                }
            }
        }
        binding.radioGroup2.radioYes.setOnClickListener {
            var responses = mutableListOf<String>()
            if (questionList.size == 0) {
                responses.add("yes")
                questionList.add(
                    QuesResponse(
                        question = "milkProducesCo-operationSociety",
                        propertyName = "Milk Producers Co-operative Society",
                        response = responses
                    )
                )
            } else {
                var isRepeated = false
                questionList.forEach {
                    if (it.question == "Milk Producers Co-operative Society") {
                        isRepeated = true
                        responses = mutableListOf()
                        responses.add("yes")
                        it.response = responses
                        return@forEach
                    }
                }
                if (!isRepeated) {
                    responses.add("yes")
                    questionList.add(
                        QuesResponse(
                            propertyName = "milkProducesCo-operationSociety",
                            response = responses,
                            question = "Milk Producers Co-operative Society"
                        )
                    )
                }
            }
        }
        binding.radioGroup2.radioNo.setOnClickListener {
            var responses = mutableListOf<String>()
            if (questionList.size == 0) {
                responses.add("no")
                questionList.add(
                    QuesResponse(
                        question = "Milk Producers Co-operative Society",
                        propertyName = "milkProducesCo-operationSociety",
                        response = responses
                    )
                )
            } else {
                var isRepeated = false
                questionList.forEach {
                    if (it.question == "Milk Producers Co-operative Society") {
                        isRepeated = true
                        responses = mutableListOf()
                        responses.add("no")
                        it.response = responses
                        return@forEach
                    }
                }
                if (!isRepeated) {
                    responses.add("no")
                    questionList.add(
                        QuesResponse(
                            propertyName = "milkProducesCo-operationSociety",
                            response = responses,
                            question = "Milk Producers Co-operative Society"
                        )
                    )
                }
            }
        }
        binding.etDistance.setOnClickListener {
            val dist = binding.etDistance.text.toString()
            val questionResponse = mutableListOf<String>()
            questionResponse.add(dist)
            questionList.add(
                QuesResponse(
                    propertyName = "distanceOfTheVillageFromTheNearestRoad",
                    question = "Distance of the Village from the nearest",
                    response = questionResponse
                )
            )
        }
    }

    private fun getData() {
        naturalResourceViewModel.fetchSurveyResponse(AppDefines.PHYSICAL_INFRA)

    }

    override fun onEducationSelect(
        position: Int,
        item: String,
        isYesSelected: Boolean,
        propertyName: String
    ) {

        binding.btnSave.let {
            it.isFocusable = true
            it.isEnabled = true
            it.isClickable = true
            it.background=context?.resources?.getDrawable(R.drawable.btn_normal)
        }

        var responses: MutableList<String> = mutableListOf()
        val option = educationalList[position]
        if (questionList.size == 0) {
            responses.add(isYesSelected.toString())
            questionList.add(
                QuesResponse(
                    propertyName = propertyName, question = option.question, response = responses
                )
            )
        } else {
            var isRepeated = false
            questionList.forEach {
                if (option.question == it.question) {
                    isRepeated = true
                    responses = mutableListOf()
                    responses.add(isYesSelected.toString())
                    it.response = responses
                    return@forEach
                }
            }
            if (!isRepeated) {
                responses.add(isYesSelected.toString())
                questionList.add(
                    QuesResponse(
                        propertyName = propertyName, response = responses, question = option.question
                    )
                )
            }
        }

    }

    override fun onFinanceSelect(
        position: Int,
        item: String,
        isYesSelected: Boolean,
        propertyName: String
    ) {
        val option = financialList[position]
        var responses: MutableList<String> = mutableListOf()
        if (questionList.size == 0) {
            responses.add(isYesSelected.toString())
            questionList.add(
                QuesResponse(
                    propertyName = propertyName, question = option.question, response = responses
                )
            )
        } else {
            var isRepeated = false
            questionList.forEach {
                if (option.question == it.question) {
                    isRepeated = true
                    responses = mutableListOf()
                    responses.add(isYesSelected.toString())
                    it.response = responses
                    return@forEach
                }
            }
            if (!isRepeated) {
                responses.add(isYesSelected.toString())
                questionList.add(
                    QuesResponse(
                        propertyName = propertyName, response = responses, question = option.question
                    )
                )
            }
        }

        binding.btnSave.let {
            it.isFocusable = true
            it.isClickable = true
            it.isEnabled = true
            context?.resources?.getColor(R.color.button_dark_blue)
                ?.let { it1 -> it.setBackgroundColor(it1) }
        }

    }


}