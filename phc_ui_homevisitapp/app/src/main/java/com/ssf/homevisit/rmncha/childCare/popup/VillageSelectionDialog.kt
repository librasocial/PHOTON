package com.ssf.homevisit.rmncha.childCare.popup

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.DialogChildCareMappingBinding
import com.ssf.homevisit.models.AllPhcResponse
import com.ssf.homevisit.models.PhcResponse
import com.ssf.homevisit.models.SubCVillResponse
import com.ssf.homevisit.models.SubcentersFromPHCResponse
import com.ssf.homevisit.viewmodel.CommonAlertViewModel

class VillageSelectionDialog(private val activityContext: Activity) {
    private var rmnchaPhcCenters: List<PhcResponse.Content> = ArrayList()
    private var rmnchaSubcenters: List<SubcentersFromPHCResponse.Content> = ArrayList()
    private var rmnchaVillageCenters: List<SubCVillResponse.Content> = ArrayList()
    private var rmnchaPhcCenterNames: MutableList<String?> = ArrayList()
    private var rmnchaPhcSubCenterNames: MutableList<String?> = ArrayList()
    private var rmnchaPhcVillageNames: MutableList<String?> = ArrayList()
    private var rmnchaPhcLiveData: LiveData<PhcResponse> = MutableLiveData()
    private var rmnchaSubCenterLiveData: LiveData<SubcentersFromPHCResponse> = MutableLiveData()
    private var rmnchaVillageLiveData: LiveData<SubCVillResponse> = MutableLiveData()
    private var rmnchaSelectedPhc: PhcResponse.Content? = null
    private var rmnchaSelectedSubCenter: SubcentersFromPHCResponse.Content? = null
    private var rmnchaSelectedVillage: SubCVillResponse.Content? = null
    private val eCAlertViewModel = CommonAlertViewModel(activityContext.application)

    fun display(onSubmit: ((PhcResponse.Content?, SubcentersFromPHCResponse.Content?, SubCVillResponse.Content?) -> Unit)) {
        val binding = DialogChildCareMappingBinding.inflate(LayoutInflater.from(activityContext))
        val dialog = Dialog(activityContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)

        binding.btnCancel.setOnClickListener { dialog.dismiss() }
        binding.btnStartMap.setOnClickListener {
            dialog.dismiss()
            onSubmit(rmnchaSelectedPhc, rmnchaSelectedSubCenter, rmnchaSelectedVillage)
        }
        rmnchaPhcLiveData = eCAlertViewModel.phcLiveData
//        rmnchaPhcLiveData.observe(
//            (activityContext as FragmentActivity)!!,
//            { phcResponse: PhcResponse? ->
//                if (phcResponse != null) {
//                    rmnchaPhcCenters =
//                        java.util.ArrayList(
//                            phcResponse.content
//                        )
//                    rmnchaPhcCenterNames = java.util.ArrayList()
//                    for (center in rmnchaPhcCenters) {
//                        rmnchaPhcCenterNames.add(center.name)
//                    }
//                    rmnchaBinding.spinnerPhc.setAdapter(
//                        ArrayAdapter<Any?>(
//                            activityContext,
//                            R.layout.layout_rmncha_spinner_textview,
//                            rmnchaPhcCenterNames
//                        )
//                    )
//                }
//            })
        val phcUuid = "8d9392ec-97cf-4a24-a761-8479055424b0"
        rmnchaPhcLiveData.observe((activityContext as FragmentActivity)) { phcResponse: PhcResponse? ->
//                val data =
//                    if (phcResponse?.content.isNullOrEmpty()) buildPhcData(phcUuid) else phcResponse
            phcResponse?.content?.let {
                rmnchaPhcCenters = it
                binding.spinnerPhc.adapter = ArrayAdapter(
                    activityContext,
                    R.layout.layout_rmncha_spinner_textview,
                    rmnchaPhcCenters.map { phcCenter -> phcCenter.name }
                )
            }
        }
        binding.spinnerPhc.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    rmnchaSelectedPhc = rmnchaPhcCenters[i]
                    val uuid = rmnchaSelectedPhc!!.uuid
                    binding.subcenterLoaderProgressBar.visibility = View.VISIBLE
                    rmnchaSubCenterLiveData =
                        eCAlertViewModel.getSubcentersFromPHCResponseLiveData(uuid)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        rmnchaSubCenterLiveData =
            eCAlertViewModel.getSubcentersFromPHCResponseLiveData(
                rmnchaSelectedPhc?.uuid ?: "8d9392ec-97cf-4a24-a761-8479055424b0"
            )
        rmnchaSubCenterLiveData.observe(activityContext) { subcentersFromPHCResponse: SubcentersFromPHCResponse? ->
            if (subcentersFromPHCResponse?.content != null) {
                rmnchaSubcenters = subcentersFromPHCResponse.content
                //rmnchaSubcenters.sort(new SubcentersFromPHCResponse.SubCenterFromPhcComparator());
                rmnchaPhcSubCenterNames = ArrayList()
                for (center in rmnchaSubcenters) {
                    rmnchaPhcSubCenterNames.add(center.target.properties.name ?: "NA")
                }
                binding.spinSubCenter.adapter = ArrayAdapter<String?>(
                    activityContext,
                    R.layout.layout_rmncha_spinner_textview,
                    rmnchaPhcSubCenterNames
                )
                binding.subcenterLoaderProgressBar.visibility = View.GONE
            }
        }
        binding.spinSubCenter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    rmnchaSelectedSubCenter = rmnchaSubcenters[i]
                    val uuid = rmnchaSelectedSubCenter!!.target.properties.uuid
                    binding.villageLoaderProgressBar.visibility = View.VISIBLE
                    rmnchaVillageLiveData = eCAlertViewModel.getVillagesFromSubcentersLiveData(uuid)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        rmnchaVillageLiveData =
            eCAlertViewModel.getVillagesFromSubcentersLiveData(/*rmnchaSelectedSubCenter?.target?.properties?.uuid ?:*/
                ""
            )
        rmnchaVillageLiveData.observe(activityContext) { villageResponse: SubCVillResponse? ->
            if (villageResponse?.content != null) {
                rmnchaVillageCenters = villageResponse.content
                rmnchaPhcVillageNames = ArrayList()
                //rmnchaVillageCenters.sort(new SubCVillResponse.SubCVillComparator());
                for (center in rmnchaVillageCenters) {
                    rmnchaPhcVillageNames.add(center.target.villageProperties.name ?: "NA")
                }
                binding.spinVillage.adapter = ArrayAdapter<String?>(
                    activityContext,
                    R.layout.layout_rmncha_spinner_textview,
                    rmnchaPhcVillageNames
                )
                binding.villageLoaderProgressBar.visibility = View.GONE
            }
        }
        binding.spinVillage.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    rmnchaSelectedVillage = rmnchaVillageCenters[i]
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        dialog.show()
    }

    private fun buildPhcData(phcUuid: String): AllPhcResponse {
        val allPhcResponse = AllPhcResponse()
        val content = AllPhcResponse.Content()
        val prop = AllPhcResponse.Properties()
        prop.name = "Sugganahalli Rural PHC"
        prop.uuid = phcUuid
        prop.phc = "Sugganahalli Rural PHC"
        content.properties = prop
        content.id = phcUuid
        val labels: MutableList<String> = java.util.ArrayList()
        labels.add("Phc")
        content.labels = labels
        val list: MutableList<AllPhcResponse.Content> = java.util.ArrayList()
        list.add(content)
        allPhcResponse.content = list
        return allPhcResponse
    }
}