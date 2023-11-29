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
import com.ssf.homevisit.adapters.TransportModeAdapter
import com.ssf.homevisit.databinding.FragmentTransportModesBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.collectSharedFlowData
import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.viewmodel.NaturalResourceViewModel

class TransportModeFragment : DialogFragment(), TransportModeAdapter.TransportModeAdapterItemClick {
    private lateinit var binding: FragmentTransportModesBinding
    private val naturalResourceViewModel: NaturalResourceViewModel by activityViewModels()
    private lateinit var adapter: TransportModeAdapter
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private var questionList: MutableList<QuesResponse> = mutableListOf()
    private lateinit var surveyId: String
    private lateinit var resourceNameList: MutableList<Options>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransportModesBinding.inflate(layoutInflater, container, false)
        this.isCancelable = false
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initAdapter()
        initClick()
    }

    private fun initAdapter() {
        adapter = TransportModeAdapter(this)
    }

    private fun initObserver() {
        collectSharedFlowData(naturalResourceViewModel.saveResponseResult) {
            binding.btnSave.visibility = View.GONE

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
                    bundle.putString(AppDefines.RETURN_FLOW, AppDefines.TRANSPORT_MODE)
                    naturalResourceViewModel.selectedSurveyTypeHashMap.add(3)
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
                        adapter.data = it
                        binding.rvPublicFacilityList.adapter = adapter
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
                            surveyName = AppDefines.TRANSPORT_MODE
                    )
            )
        }
    }

    private fun getData() {
        naturalResourceViewModel.fetchSurveyResponse(AppDefines.TRANSPORT_MODE)

    }

    override fun onCheckItem(position: Int, transport: String, propertyName: String) {
        binding.btnSave.let {
            it.isFocusable = true
            it.isClickable = true
            it.isEnabled = true
            it.background = context?.resources?.getDrawable(R.drawable.btn_normal)
        }
        val responses: MutableList<String> = mutableListOf()
        val option = resourceNameList[position]
        responses.add("Selected")
        questionList.add(
                QuesResponse(
                        propertyName = propertyName,
                        response = responses,
                        question = option.question
                )
        )
        if (transport == "None") {
            resourceNameList.forEach {
                it.checked = it.question == "None"
            }
            questionList= mutableListOf()
            adapter.notifyDataSetChanged()
        } else {
            var isNoneChecked =false
            resourceNameList.forEach {
                if (it.question == "None") {
                    if (it.checked) {
                        isNoneChecked=true
                        it.checked = false
                    }
                }
            }
            if(isNoneChecked){
                resourceNameList[position].checked=true
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onUnCheckItem(position: Int, transport: String, propertyName: String) {
        questionList.removeIf {
            it.question == transport
        }
    }
}