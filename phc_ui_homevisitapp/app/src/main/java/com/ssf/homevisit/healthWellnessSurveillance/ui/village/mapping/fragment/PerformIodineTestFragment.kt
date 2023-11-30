package com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentIodineSampleBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.collectSharedFlowData
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.adapter.IodineSampleTestResultAdapter
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.viewModel.WaterSampleViewModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.PlaceItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.TestResultDataItem
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PerformIodineTestFragment : Fragment(), IodineSampleTestResultAdapter.OnItemSelected,
        AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentIodineSampleBinding
    private lateinit var adpapter: IodineSampleTestResultAdapter
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private val viewModel: WaterSampleViewModel by activityViewModels()
    private var shopOwner: String = ""
    private var saltTradeName: String = ""
    private var sampleCount: String = ""
    private var collectionDate: String = ""
    private var placeDetails: PlaceItem? = null
    private lateinit var flowType: String
    private var asset: String = ""
    private lateinit var houseHoldDetails: HouseHoldDataItem
    var calendar: Calendar = Calendar.getInstance()
    var calendarPosition:Int=-1
    private var data:MutableList<TestResultDataItem> = mutableListOf()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_iodine_sample, container, false
        )
        return binding.root
    }


    private val dateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int
        ) {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val month = monthOfYear + 1
            var monthString = ""
            if (month < 10) {
                monthString = "0${month.toString()}"
            } else {
                monthString = month.toString()
            }
            if (dayOfMonth < 10) {
                val x = "0${dayOfMonth.toString()}"
                updateDateInView(year, monthString, x)
            } else {
                updateDateInView(year, monthString, dayOfMonth.toString())
            }


        }
    }

    private fun updateDateInView(year: Int, monthString: String, dayOfMonth: String) {
        val date = "$year-$monthString-$dayOfMonth"
        data[calendarPosition].dateOfSampleSubmitted=date
        adpapter.notifyItemChanged(calendarPosition)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initUi()
        initAdapter()
        initClick()
        initObserver()
    }

    private fun initUi() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        binding.tvCurrentDate.text = currentDate
        binding.tvCurrentDate2.text = currentDate
        collectionDate = currentDate
        if (flowType == AppDefines.HNW_VILLAGE_SURVEILLANCE) {
            binding.surveyType.text = "Village Level"
            binding.tvPlaceName.text = placeDetails?.name
            binding.tvImageTitle.text=placeDetails?.name
        } else {
            binding.surveyType.text = "Household Level"
            binding.tvImageTitle.text=houseHoldDetails.nameHouseHoldHead
            binding.tvPlaceName.text = houseHoldDetails.nameHouseHoldHead
        }
    }


    private fun initObserver() {

        collectSharedFlowData(viewModel.iodineSampleByFilter) {
            if (it.isNotEmpty()) {
                binding.cvTestResultList.visibility = View.VISIBLE
                binding.tvResultStatus.visible()
                data = mutableListOf<TestResultDataItem>()
                it.forEach {IodineSampleContent->
                    val labResults= if(!IodineSampleContent.resultDate.isNullOrEmpty())
                        "Success"
                    else "Select to update lab results"
                    var submissionDate: String? =IodineSampleContent.labSubmittedDate
                    if(labResults.equals("Success")){
                        if(submissionDate.isNullOrEmpty()){
                            submissionDate="-----------"
                        }
                    }
                    else{
                        submissionDate="Select Date"
                    }
                    data.add(
                            TestResultDataItem(
                                    tradeNameOfSalt = IodineSampleContent.saltBrandName,
                                    dateOfSampleCollected = IodineSampleContent.dateCollected,
                                    sampleCount = IodineSampleContent.noOfSamplesCollected.toString(),
                                    dateOfSampleSubmitted = submissionDate,
                                    labResults = labResults,
                                    sampleId = IodineSampleContent.id
                            )
                    )
                }
                binding.cvTestResultList.visible()
                binding.tvResultStatus.visible()
                adpapter.setData(data)
                adpapter.notifyDataSetChanged()
            } else {
                binding.tvResultStatus.gone()
                binding.cvTestResultList.visibility = View.GONE
            }

        }
        collectSharedFlowData(viewModel.isIodineSurveillanceCreated) {
            if (it) {
                viewModel.getIodineSampleByFilter(
                        userId = larvaViewModel.userId, iodineSurveillanceId = viewModel.iodineSurveillanceId
                )
            }
        }
        collectSharedFlowData(viewModel.isIodineSurveillanceAlreadyCreated) {
            if (!it) {
                if (flowType == AppDefines.HNW_VILLAGE_SURVEILLANCE) {
                    placeDetails?.let {
                        viewModel.createIodineSurveillance(
                                villageId = larvaViewModel.villageUuid,
                                placeType = it.assetType,
                                placeId = it.id,
                                userId = larvaViewModel.userId,
                                placeName = it.name,
                                dateOfSurveillance = collectionDate
                        )
                    }
                }
                if (flowType == AppDefines.HNW_HOUSEHOLD_SURVEILLANCE) {
                    houseHoldDetails.let {
                        viewModel.createIodineSurveillance(
                                userId = larvaViewModel.userId,
                                houseHoldId = houseHoldDetails.uuid,
                                villageId = larvaViewModel.villageUuid,
                                dateOfSurveillance = collectionDate
                        )
                    }
                }
            } else {
                viewModel.getIodineSampleByFilter(
                        userId = larvaViewModel.userId,
                        iodineSurveillanceId = viewModel.iodineSurveillanceId
                )
            }
        }
        collectSharedFlowData(viewModel.isIodineSampleCreated) {
            if (it) {
                apiSuccessFailureDialog(isSuccess = true, successButtonClick = {
                    Toast.makeText(
                            context, "Response  Saved Successfully", Toast.LENGTH_SHORT
                    ).show()
                    viewModel.getIodineSampleByFilter(
                            userId = larvaViewModel.userId,
                            iodineSurveillanceId = viewModel.iodineSurveillanceId
                    )
                })
            } else {
                apiSuccessFailureDialog(isSuccess = false, failureButtonCLick = {
                    Toast.makeText(
                            context, "Response  Saved Failed", Toast.LENGTH_SHORT
                    ).show()
                })
            }

        }
    }

    private fun initArguments() {
        flowType = larvaViewModel.flowType
        arguments?.let {
            if (it.containsKey(AppDefines.PLACE_DETAILS)) {
                val type = object : TypeToken<PlaceItem>() {}.type
                placeDetails = Gson().fromJson(it.getString(AppDefines.PLACE_DETAILS), type)
                if (placeDetails?.imageUrl?.isNotEmpty() == true) {
                    Picasso.get().load(placeDetails?.imageUrl)
                            .into(binding.ivPlace)
                } else {
                    binding.tvError.visible()
                    binding.ivError.visible()
                }
            }
            if (it.containsKey(AppDefines.HOUSEHOLD_DETAILS)) {
                val type = object : TypeToken<HouseHoldDataItem>() {}.type
                houseHoldDetails = Gson().fromJson(it.getString(AppDefines.HOUSEHOLD_DETAILS), type)
                if (houseHoldDetails.imageUrl?.isNotEmpty() == true) {
                    if (houseHoldDetails.imageUrl!![0].isNotEmpty()) {
                        Picasso.get().load(houseHoldDetails.imageUrl?.get(0))
                            .into(binding.ivPlace)
                    } else {
                        binding.tvError.visible()
                        binding.ivError.visible()
                    }
                } else {
                    binding.tvError.visible()
                    binding.ivError.visible()
                }
            }
            if (flowType == AppDefines.HNW_VILLAGE_SURVEILLANCE) {
                placeDetails?.let {
                    viewModel.getIodineSurveillanceByFilter(
                            villageId = larvaViewModel.villageUuid,
                            placeId = it.id,
                            placeType = it.assetType,
                            placeName = it.name,
                            userId = larvaViewModel.userId
                    )
                }
            }
            if (flowType == AppDefines.HNW_HOUSEHOLD_SURVEILLANCE) {
                houseHoldDetails.let {
                    viewModel.getIodineSurveillanceByFilter(
                            userId = larvaViewModel.userId, houseHoldId = houseHoldDetails.uuid
                    )
                }
            }
            if (it.containsKey(AppDefines.ASSET_TYPE)) {
                asset = it.getString(AppDefines.ASSET_TYPE).toString()
                asset.let {
                    setImageForAsset(it)
                }
            } else {
                binding.ivSea.background = ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_home)
            }
        }
    }

    private fun initAdapter() {
        adpapter = IodineSampleTestResultAdapter(mutableListOf(), this)
        binding.rvTestResultList.adapter = adpapter
    }

    private fun initClick() {
        binding.spinnerSampleCount.onItemSelectedListener = this
        val data = listOf("Select", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        context?.let { context ->
            val chooseDefectAdapter = ArrayAdapter(
                    context, android.R.layout.simple_spinner_item, data
            )
            chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSampleCount.adapter = chooseDefectAdapter
        }
        binding.etSpecifyOther.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saltTradeName = s.toString()
                checkButtonStatus()
            }

            override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        binding.etShopOwner.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                shopOwner = s.toString()
                checkButtonStatus()
            }

            override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.spinnerTradeName.onItemSelectedListener = this
        val tradeNameData = listOf(
                "Select",
                "Annapurna salt",
                "Shashi salt",
                "TATA salt",
                "Nirma salt",
                "Sadguru salt",
                "M.S.K salt",
                "Sprinkle salt",
                "Arokya salt",
                "Unpackaged Salt",
                "Others (Specify)"
        )
        context?.let { context ->
            val chooseDefectAdapter = ArrayAdapter(
                    context, android.R.layout.simple_spinner_item, tradeNameData
            )
            chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTradeName.adapter = chooseDefectAdapter
        }
        binding.btnContinue.setOnClickListener {
            viewModel.createIodineSampleSurveillance(
                    iodineSurveillanceId = viewModel.iodineSurveillanceId,
                    shopOwnerName = shopOwner,
                    saltBrandName = saltTradeName,
                    noOfSamplesCollected = sampleCount.toInt(),
                    dateCollected = collectionDate,
                    userId = larvaViewModel.userId
            )
        }
        binding.tvCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkButtonStatus() {
        if (shopOwner.isNotBlank() && saltTradeName.isNotBlank() && sampleCount.isNotBlank()) {
            binding.btnContinue.let {
                it.isFocusable = true
                it.isClickable = true
                it.isEnabled = true
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val data = parent?.getItemAtPosition(position).toString()
        if (parent?.equals(binding.spinnerTradeName) == true) {
            if (data == "Others (Specify)") {
                binding.etSpecifyOther.visible()
            } else {
                saltTradeName = data
            }
        }
        if (parent?.equals(binding.spinnerSampleCount) == true) {
            sampleCount = data
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun setImageForAsset(asset: String) {
        when (asset) {
            "Shop" -> {
                setImageDrawable(R.drawable.ic_shop)
            }
            "Home" -> {
                setImageDrawable(R.drawable.ic_home)
            }
            "Hotel" -> {
                setImageDrawable(R.drawable.ic_hotel)
            }
            "Others" -> {
                setImageDrawable(R.drawable.ic_others)
            }
            "WaterBody" -> {
                setImageDrawable(R.drawable.waterbodyicon)
            }
            "School/college" -> {
                setImageDrawable(R.drawable.ic_school_college)
            }
            "Temple" -> {
                setImageDrawable(R.drawable.ic_temple)
            }
            "Mosque" -> {
                setImageDrawable(R.drawable.ic_mosque)
            }
            "Church" -> {
                setImageDrawable(R.drawable.ic_church)
            }
            "Office" -> {
                setImageDrawable(R.drawable.ic_office)
            }
            "Hospital" -> {
                setImageDrawable(R.drawable.ic_hospital)
            }
            "Construction" -> {
                setImageDrawable(R.drawable.ic_construction)
            }
            "Bus Stop" -> {
                setImageDrawable(R.drawable.ic_bus_stop)
            }
            "Toilet" -> {
                setImageDrawable(R.drawable.ic_toilet)
            }
            "Park" -> {
                setImageDrawable(R.drawable.ic_park)
            }
        }

    }

    private fun setImageDrawable(imageId: Int) {
        binding.ivSea.background =
                ContextCompat.getDrawable(this.requireContext(),imageId)
    }

    override fun onItemSelected(data: TestResultDataItem?, dateClick: Boolean?,position: Int) {
        if (dateClick == true) {
            calendarPosition=position
            context?.let {
                DatePickerDialog(
                        it,
                        dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        } else {
            val bundle = Bundle()
            val gson = Gson()
            bundle.putString(AppDefines.TEST_RESULT_DATA, gson.toJson(data))
            if (flowType == AppDefines.HNW_VILLAGE_SURVEILLANCE) {
                bundle.putString(
                        AppDefines.PLACE_DETAILS, gson.toJson(placeDetails)
                )
            } else {
                bundle.putString(
                        AppDefines.HOUSEHOLD_DETAILS, gson.toJson(houseHoldDetails)
                )
            }
            bundle.putString(AppDefines.ASSET_TYPE, asset)
            findNavController().navigate(R.id.updateIodineSampleReportFragment, bundle)
        }
    }

}