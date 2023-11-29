package com.ssf.homevisit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.EnvironmentQualityAdapter
import com.ssf.homevisit.databinding.FragmentQualityEnvironmentBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.collectSharedFlowData
import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.viewmodel.NaturalResourceViewModel

class EnvironmentQualityFragment : DialogFragment(), AdapterView.OnItemSelectedListener,
    EnvironmentQualityAdapter.EnvironmentQualityItemSelected {
    private lateinit var binding: FragmentQualityEnvironmentBinding
    private val naturalResourceViewModel: NaturalResourceViewModel by activityViewModels()
    private lateinit var adapter: EnvironmentQualityAdapter
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private val questionList: MutableList<QuesResponse> = mutableListOf()
    private lateinit var surveyId: String
    private lateinit var resourceNameList: MutableList<Options>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentQualityEnvironmentBinding.inflate(layoutInflater, container, false)
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

    private fun initAdapter() {
        context?.let {
            adapter = EnvironmentQualityAdapter(it, this)
            binding.rvQualityList.adapter = adapter
        }
    }


    private fun initObserver() {
        collectSharedFlowData(naturalResourceViewModel.saveResponseResult) {
            binding.btnSave.visibility = View.GONE
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
                    bundle.putString(
                        AppDefines.RETURN_FLOW, AppDefines.ENVIRONMENT_SANITATION
                    )
                    naturalResourceViewModel.selectedSurveyTypeHashMap.add(0)
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
                    it.baseResponseData.data?.quesOptions.let {
                        resourceNameList = it as MutableList<Options>
                        adapter.data = it.subList(0, 5)
                        adapter.notifyDataSetChanged()
                        val data = it[6].choices as MutableList<String>
                        if (!data.contains("Select")) {
                            data.add(0, "Select")
                        }
                        binding.spinnerChooseDefect.onItemSelectedListener = this
                        context?.let { context ->
                            val chooseDefectAdapter = ArrayAdapter(
                                context, android.R.layout.simple_spinner_item, data
                            )
                            chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerChooseDefect.adapter = chooseDefectAdapter
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun initClick() {
        binding.yesRadioButton.setOnClickListener {
            binding.btnSave.let {
                it.isFocusable = true
                it.isClickable = true
                it.isEnabled = true
                context?.resources?.getColor(R.color.button_dark_blue)
                    ?.let { it1 -> it.setBackgroundColor(it1) }
            }
            var responses: MutableList<String> = mutableListOf()
            val question = resourceNameList[5].question
            val propertyName=resourceNameList[5].propertyName
            if (questionList.size == 0) {
                responses.add("yes")
                questionList.add(
                    QuesResponse(
                        propertyName = propertyName, question = question, response = responses
                    )
                )
            } else {
                var isRepeated = false
                questionList.forEach {
                    if (question == it.question) {
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
                            propertyName = propertyName, response = responses, question = question
                        )
                    )
                }
                binding.grpChooseDefect.visibility = View.VISIBLE
            }
        }
        binding.noRadioButton.setOnClickListener {
            binding.btnSave.let {
                it.isFocusable = true
                it.isClickable = true
                it.isEnabled = true
                it.background=context?.resources?.getDrawable(R.drawable.btn_normal)
            }
            var responses: MutableList<String> = mutableListOf()
            val question = resourceNameList[5].question
            val propertyName=resourceNameList[5].propertyName
            if (questionList.size == 0) {
                responses.add("no")
                questionList.add(
                    QuesResponse(
                        propertyName = propertyName, question = question, response = responses
                    )
                )
            } else {
                var isRepeated = false
                questionList.forEach {
                    if (question == it.question) {
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
                            propertyName = propertyName, response = responses, question = question
                        )
                    )
                }
                binding.grpChooseDefect.visibility = View.GONE
            }
        }
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
                    surveyName = AppDefines.ENVIRONMENT_SANITATION
                )
            )
        }
    }


    private fun getData() {
        naturalResourceViewModel.fetchSurveyResponse(AppDefines.ENVIRONMENT_SANITATION)
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.getItemAtPosition(p2).toString()
        var responses: MutableList<String> = mutableListOf()
        if (questionList.size == 0) {
            responses.add(item)
            questionList.add(
                QuesResponse(
                    propertyName = resourceNameList.last().propertyName,
                    question = resourceNameList.last().question,
                    response = responses
                )
            )
        } else {
            var isRepeated = false
            questionList.forEach {
                if (resourceNameList.last().question == it.question) {
                    isRepeated = true
                    responses = mutableListOf()
                    responses.add(item)
                    it.response = responses
                    return@forEach
                }
            }
            if (!isRepeated) {
                responses.add(item)
                questionList.add(
                    QuesResponse(
                        propertyName = resourceNameList.last().propertyName,
                        response = responses,
                        question = resourceNameList.last().question
                    )
                )
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelect(item: String, position: Int, propertyName: String) {
        var responses: MutableList<String> = mutableListOf()
        val option = resourceNameList[position]
        if (questionList.size == 0) {
            responses.add(item)
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
                    responses.add(item)
                    it.response = responses
                    return@forEach
                }
            }
            if (!isRepeated) {
                responses.add(item)
                questionList.add(
                    QuesResponse(
                        propertyName = propertyName, response = responses, question = option.question
                    )
                )
            }

        }
    }

}

