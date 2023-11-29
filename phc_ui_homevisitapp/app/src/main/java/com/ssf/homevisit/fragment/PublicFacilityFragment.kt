package com.ssf.homevisit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.PublicFacilityAdapter
import com.ssf.homevisit.databinding.FragmentPublicFacilityBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.collectSharedFlowData
import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.viewmodel.NaturalResourceViewModel

class PublicFacilityFragment : DialogFragment(),
        PublicFacilityAdapter.PublicFacilityAdapterItemClick, PublicFacilityAdapter.OnOtherClick {

    private lateinit var binding: FragmentPublicFacilityBinding
    private lateinit var adapter: PublicFacilityAdapter
    private val naturalResourceViewModel: NaturalResourceViewModel by activityViewModels()
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private val questionList: MutableList<QuesResponse> = mutableListOf()
    private lateinit var surveyId: String
    private var resourceNameList: MutableList<OptionsData> = mutableListOf()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPublicFacilityBinding.inflate(layoutInflater, container, false)
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
        adapter = PublicFacilityAdapter(this, this)
        binding.rvPublicFacilityList.adapter = adapter
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
                    bundle.putString(AppDefines.RETURN_FLOW, AppDefines.PUBLIC_FACILITY)
                    naturalResourceViewModel.selectedSurveyTypeHashMap.add(4)
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
                                            choices = it.choices,
                                            question = it.question,
                                            propertyName = it.propertyName
                                    )
                            )
                        }
                        adapter.data = resourceNameList
                        binding.rvPublicFacilityList.adapter = adapter
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
                            surveyName = AppDefines.PUBLIC_FACILITY
                    )
            )
        }
    }

    private fun getData() {
        naturalResourceViewModel.fetchSurveyResponse(AppDefines.PUBLIC_FACILITY)
    }

    override fun onSelect(
        position: Int,
        item: String?,
        isYesSelected: Boolean,
        data: MutableList<OptionsData>,
        propertyName: String?
    ) {
        var responses: MutableList<String> = mutableListOf()
        data[position].isSelected = true
        data[position].value = isYesSelected
        if (questionList.size == 0) {
            responses.add(isYesSelected.toString())
            questionList.add(
                    QuesResponse(
                            propertyName = propertyName, question = item, response = responses
                    )
            )
        } else {
            var isRepeated = false
            questionList.forEach {
                if (item == it.question) {
                    isRepeated = true
                    responses = mutableListOf()
                    responses.add(isYesSelected.toString())
                    it.response = responses
                    return
                }
            }
            if (!isRepeated) {
                responses.add(isYesSelected.toString())
                questionList.add(
                        QuesResponse(
                                propertyName =propertyName, response = responses, question = item
                        )
                )
            }
        }
        adapter.notifyItemChanged(position)
        binding.btnSave.let {
            it.isFocusable = true
            it.isClickable = true
            it.isEnabled = true
            it.background = context?.resources?.getDrawable(R.drawable.btn_normal)
        }

    }

    override fun onOtherClick(data: String, question: String?, position: Int, propertyName: String?) {
        var responses: MutableList<String> = mutableListOf()
        if (questionList.size == 0) {
            responses.add(data)
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
                    responses.add(data)
                    it.response = responses
                    return
                }
            }
            if (!isRepeated) {
                responses.add(data)
                questionList.add(
                        QuesResponse(
                                propertyName = propertyName, response = responses, question = question
                        )
                )
            }
        }
    }
}