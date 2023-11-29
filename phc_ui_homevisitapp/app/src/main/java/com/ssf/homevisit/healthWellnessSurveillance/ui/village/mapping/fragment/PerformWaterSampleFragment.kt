package com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentPerformSurveillanceTestBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.collectSharedFlowData
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.WaterSample
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.adapter.WaterSampleTestResultAdapter
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.viewModel.WaterSampleViewModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.PlaceItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.TestResultDataItem
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.requestmanager.AppDefines.HNW_VILLAGE_SURVEILLANCE
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class PerformWaterSampleFragment : Fragment(), WaterSampleTestResultAdapter.OnItemSelected,
        AdapterView.OnItemSelectedListener {

    private lateinit var waterSampleTestResultAdapter: WaterSampleTestResultAdapter
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private val viewModel: WaterSampleViewModel by activityViewModels()
    private var placeDetails: PlaceItem? = null
    private var dateOfSubmission: String = ""
    private var sampleCount: String = ""
    private var collectionDate: String = ""
    private var asset:String?=""
    private lateinit var fragmentBinding: FragmentPerformSurveillanceTestBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        fragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_perform_surveillance_test, container, false
        )
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initAdapter()
        initUi()
        initClick()
        initObserver()
    }
    private fun initObserver() {
        collectSharedFlowData(viewModel.waterSurveillanceByFilter) {
            if (it.isNotEmpty()) {
                fragmentBinding.cvTestResultList.visibility = View.VISIBLE
                fragmentBinding.tvResultStatus.visible()
                val data = mutableListOf<TestResultDataItem>()
                it.forEach { waterSampleContent ->
                    waterSampleContent.id?.let { id ->
                        val labResults= if(waterSampleContent.testResult?.resultDate?.isNotEmpty()==true)
                            "Success"
                        else "Select to update lab results"
                        data.add(
                                TestResultDataItem(
                                        dateOfSampleSubmitted = waterSampleContent.sample?.labSubmittedDate,
                                        dateOfSampleCollected = waterSampleContent.sample?.collectionDate,
                                        sampleCount = waterSampleContent.sample?.noOfSamples,
                                        labResults = labResults,
                                        sampleId = id
                                )
                        )
                    }
                }
                waterSampleTestResultAdapter.setData(data)
                waterSampleTestResultAdapter.notifyDataSetChanged()
            } else {
                fragmentBinding.cvTestResultList.visibility = View.GONE
                fragmentBinding.tvResultStatus.gone()
            }
        }
        collectSharedFlowData(viewModel.isWaterSurveillanceCreated) {
            if (it) {
                apiSuccessFailureDialog(isSuccess = true, successButtonClick = {
                    Toast.makeText(
                            context, "Response  Saved Successfully", Toast.LENGTH_SHORT
                    ).show()
                    placeDetails?.let {
                        viewModel.getWaterSampleByFilter(
                                villageId = larvaViewModel.villageUuid,
                                placeId = it.id,
                                placeType = it.assetType
                        )
                    }
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
    private fun initClick() {
        fragmentBinding.btnContinue.setOnClickListener {
            placeDetails?.let { placeDetails ->
                if(sampleCount=="Select"){
                    sampleCount="0"
                }
                viewModel.createWaterSample(
                        villageId = larvaViewModel.villageUuid,
                        placeType = placeDetails.assetType,
                        placeId = placeDetails.id,
                        userId = larvaViewModel.userId,
                        placeName = placeDetails.name,
                        sample = WaterSample(
                                noOfSamples = sampleCount, collectionDate, dateOfSubmission
                        ),
                        dateOfSurveillance = dateOfSubmission
                )
            }
        }

        fragmentBinding.tvCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        fragmentBinding.spinnerSampleCount.onItemSelectedListener = this
        val sampleCountData = listOf("Select", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        context?.let { context ->
            val chooseDefectAdapter = ArrayAdapter(
                    context, android.R.layout.simple_spinner_item, sampleCountData
            )
            chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fragmentBinding.spinnerSampleCount.adapter = chooseDefectAdapter
        }
    }
    private fun initArguments() {
        arguments?.let {
            if (it.containsKey(AppDefines.PLACE_DETAILS)) {
                val type = object : TypeToken<PlaceItem>() {}.type
                placeDetails = Gson().fromJson(it.getString(AppDefines.PLACE_DETAILS), type)
                if (placeDetails?.imageUrl?.isNotEmpty() == true) {
                    Picasso.get().load(placeDetails?.imageUrl).resize(100, 100)
                            .into(fragmentBinding.ivPlace)
                } else {
                    fragmentBinding.tvError.visible()
                    fragmentBinding.ivError.visible()
                }
            }
            placeDetails?.let {
                viewModel.getWaterSampleByFilter(
                        villageId = larvaViewModel.villageUuid,
                        placeId = it.id,
                        placeType = it.assetType
                )
            }
            if (it.containsKey(AppDefines.ASSET_TYPE)) {
                asset = it.getString(AppDefines.ASSET_TYPE)
                asset?.let { setImageForAsset(it) }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initUi() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        fragmentBinding.tvCurrentDate.text = currentDate
        fragmentBinding.tvCurrentDate2.text = currentDate
        collectionDate = currentDate
        dateOfSubmission=currentDate
        fragmentBinding.tvPlaceName.text = placeDetails?.name
        fragmentBinding.tvImageTitle.text = placeDetails?.name

    }


    private fun initAdapter() {
        waterSampleTestResultAdapter = WaterSampleTestResultAdapter(mutableListOf(), this)
        fragmentBinding.rvTestResultList.adapter = waterSampleTestResultAdapter
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val data = p0?.getItemAtPosition(p2).toString()
        if (p0?.equals(fragmentBinding.spinnerSampleCount) == true) {
            sampleCount = data
            checkButtonStatus()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun checkButtonStatus() {
        fragmentBinding.btnContinue.let {
            it.isFocusable = true
            it.isClickable = true
            it.isEnabled = true
        }
    }

    override fun onItemSelected(data: TestResultDataItem) {
        val bundle = Bundle()
        val gson = Gson()
        bundle.putString(AppDefines.TEST_RESULT_DATA, gson.toJson(data))
        bundle.putString(
                AppDefines.PLACE_DETAILS, gson.toJson(placeDetails)
        )
        bundle.putString(AppDefines.ASSET_TYPE,asset)
        findNavController().navigate(R.id.updateWaterSampleReportFragment, bundle)
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
        fragmentBinding.ivSea.background =
                context?.resources?.getDrawable(imageId)
    }

}