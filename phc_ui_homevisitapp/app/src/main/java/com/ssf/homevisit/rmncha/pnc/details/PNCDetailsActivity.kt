package com.ssf.homevisit.rmncha.pnc.details

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.activity.MainActivity
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.databinding.*
import com.ssf.homevisit.models.*
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.rmncha.base.RMNCHABaseActivity
import com.ssf.homevisit.rmncha.base.RMNCHAConstants
import com.ssf.homevisit.rmncha.base.RMNCHAUtils
import com.ssf.homevisit.rmncha.pnc.selectwomen.PNCWomenAdapter
import com.ssf.homevisit.rmncha.pnc.service.PNCServiceActivity
import kotlinx.coroutines.*

class PNCDetailsActivity : RMNCHABaseActivity() {
    private var infantDialog: Dialog? = null
    private lateinit var binding: ActivityRmnchaPncDetailsBinding
    private var houseHoldUUID: String? = null
    private var subCenterUUID: String? = null
    private var selectedWomenUUID: String? = null
    private lateinit var viewModel: PNCDetailsViewModel
    private var selectedWomen: RMNCHAPNCWomenResponse.Content? = null
    private var coupleData: RMNCHACoupleDetailsResponse.Content? = null
    var infantDetails: RMNCHAPNCDeliveryOutcomesResponse? = null
        private set
    private var ashaWorkersList: List<AshaWorkerResponse.Content> = ArrayList()
    var pNCRegistrationId: String? = null
        private set
    private var ancRegistrationData: RMNCHAANCRegistrationResponse? = null
    private var lmpDateFromANC: String? = null
    private var eddDateFromANC: String? = null
    private var lastAncVisitDate: String? = null
    private var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rmncha_pnc_details)
        viewModel = PNCDetailsViewModel(application)
        binding.viewModel = viewModel
        AppController.getInstance().pncLandingActivity = this
        initialiseViews()
    }

    private fun initialiseViews() {
        try {
            houseHoldUUID = (intent?.extras?.get(PARAM_1)?.toString() ?: "") + ""
            subCenterUUID = (intent.extras?.get(PARAM_SUB_CENTER)?.toString() ?: "") + ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.buttonDeliveryOutcome.setOnClickListener { view: View? ->
            val ashaName = binding.spinnerSelectAshaName.selectedItem.toString() + ""
            if (ashaName.equals("select", ignoreCase = true)) {
                RMNCHAUtils.showMyToast(this, "Select Valid Asha Worker Name")
                return@setOnClickListener
            }
            if (coupleData == null) {
                RMNCHAUtils.showMyToast(this, "Error: CoupleData not found")
            }
            showDeliveryOutcomesAlert(AppDefines.PNC_DELIVERY_OUTCOME_UTILITY_URL)
        }
        binding.buttonInfant.setOnClickListener { view: View? ->
            val ashaName = binding.spinnerSelectAshaName.selectedItem.toString() + ""
            if (ashaName.equals("select", ignoreCase = true)) {
                RMNCHAUtils.showMyToast(this, "Select Valid Asha Worker Name")
                return@setOnClickListener
            }
            showInfantDetailsAlert()
        }
        ashaWorkerData
        setSelectedWomenView()
    }

    private fun showDeliveryOutcomesAlert(pncDeliveryOutcomeUtilityUrl: String) {
        val dialog = Dialog(this)
        val deliveryOutcomesForPncBinding =
                DataBindingUtil.inflate<DialogueLayoutRmnchaDeliveryOutcomesForPncBinding>(
                        dialog.layoutInflater,
                        R.layout.dialogue_layout_rmncha_delivery_outcomes_for_pnc,
                        null,
                        false
                )
        progressBar = deliveryOutcomesForPncBinding.progressBar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(deliveryOutcomesForPncBinding.root)
        dialog.show()
        val window = dialog.window
        window?.setLayout(1250, WindowManager.LayoutParams.WRAP_CONTENT)
        deliveryOutcomesForPncBinding.textViewLmpDate.text = lmpDateFromANC
        deliveryOutcomesForPncBinding.textViewEddDate.text = eddDateFromANC
        deliveryOutcomesForPncBinding.textViewAncVisitDate.text =
                RMNCHAUtils.getDateToView(lastAncVisitDate + "", RMNCHAUtils.RMNCHA_DATE_FORMAT)
        deliveryOutcomesForPncBinding.textViewFinancialYear.text =
                RMNCHAUtils.getCurrentFinancialYear()
        deliveryOutcomesForPncBinding.textViewDeliveryDate.setOnClickListener { view: View? ->
            RMNCHAUtils.getDateFromDatePicker(
                    view as TextView?
            )
        }
        deliveryOutcomesForPncBinding.textViewDateOfDischarge.setOnClickListener { view: View? ->
            RMNCHAUtils.getDateFromDatePicker(
                    view as TextView?
            )
        }
        deliveryOutcomesForPncBinding.textViewDeliveryTime.setOnClickListener { view: View? ->
            RMNCHAUtils.getTimeFromTimePicker(
                    view as TextView?
            )
        }
        deliveryOutcomesForPncBinding.textViewDischargeTime.setOnClickListener { view: View? ->
            RMNCHAUtils.getTimeFromTimePicker(
                    view as TextView?
            )
        }
        deliveryOutcomesForPncBinding.closeDialogue.setOnClickListener { v: View? -> dialog.dismiss() }
        PNCWomenAdapter.setPNCWomenView(deliveryOutcomesForPncBinding.womenLayout, selectedWomen)
        getFormUtilityForDeliveryOutcome(deliveryOutcomesForPncBinding)
        deliveryOutcomesForPncBinding.spinnerCovidTestTaken.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    val data =
                        deliveryOutcomesForPncBinding.spinnerCovidTestTaken.selectedItem.toString() + ""
                    if (data.equals("done", ignoreCase = true)) {
                        deliveryOutcomesForPncBinding.layoutCovidResult.visibility = View.VISIBLE
                    } else {
                        deliveryOutcomesForPncBinding.layoutCovidResult.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        deliveryOutcomesForPncBinding.buttonSubmit.setOnClickListener { view: View? ->
            validateDeliveryOutcomesDataAndContinue(
                    deliveryOutcomesForPncBinding,
                    dialog,
                    RMNCHAServiceStatus.PNC_OUTCOME_REGISTERED
            )
        }
    }

    private fun getFormUtilityForDeliveryOutcome(deliveryOutcomesForPncBinding: DialogueLayoutRmnchaDeliveryOutcomesForPncBinding) {
        viewModel.getEcUtilityData(AppDefines.PNC_DELIVERY_OUTCOME_UTILITY_URL)
                .observe(this) { contents: List<FormUtilityResponse.SurveyFormResponse>? ->
                    if (contents != null) {
                        for (content in contents) {
                            if (content.groupName == "Place of delivery") {
                                val data = ArrayList<String?>()
                                for (questions in content.quesOptions) {
                                    data.add(questions.title)
                                }
                                deliveryOutcomesForPncBinding.spinnerPlaceOfDelivery.adapter =
                                        ArrayAdapter<String>(
                                                this,
                                                R.layout.layout_rmncha_spinner_textview,
                                                data
                                        )
                            }
                            if (content.groupName == "Location of Delivery") {
                                val data = ArrayList<String?>()
                                for (questions in content.quesOptions) {
                                    data.add(questions.title)
                                }
                                deliveryOutcomesForPncBinding.spinnerLocationOfDelivery.adapter =
                                    ArrayAdapter<String>(
                                        this,
                                        R.layout.layout_rmncha_spinner_textview,
                                        data
                                    )
                            }
                            if (content.groupName == "Delivery Conducted by*") {
                                val data = ArrayList<String?>()
                                for (questions in content.quesOptions) {
                                    data.add(questions.title)
                                }
                                deliveryOutcomesForPncBinding.spinnerDeliveryConductedBy.adapter =
                                        ArrayAdapter<String>(
                                                this,
                                                R.layout.layout_rmncha_spinner_textview,
                                                data
                                        )
                            }
                            if (content.groupName == "Type of Delivery*") {
                                val data = ArrayList<String?>()
                                data.add("Select")
                                for (questions in content.quesOptions) {
                                    data.add(questions.title)
                                }
                                deliveryOutcomesForPncBinding.spinnerTypeOfDelivery.adapter =
                                        ArrayAdapter<String>(
                                                this,
                                                R.layout.layout_rmncha_spinner_textview,
                                                data
                                        )
                            }
                            if (content.groupName == "Outcomes of Delivery*") {
                                val data = ArrayList<String?>()
                                for (questions in content.quesOptions) {
                                    data.add(questions.title)
                                }
                                deliveryOutcomesForPncBinding.spinnerOutcomesOfDelivery.adapter =
                                        ArrayAdapter<String>(
                                                this,
                                                R.layout.layout_rmncha_spinner_textview,
                                                data
                                        )
                            }
                            if (content.groupName == "Live Birth") {
                                val data = ArrayList<String?>()
                                for (questions in content.quesOptions) {
                                    data.add(questions.title)
                                }
                                deliveryOutcomesForPncBinding.spinnerLiveBirth.adapter =
                                        ArrayAdapter<String>(
                                                this,
                                                R.layout.layout_rmncha_spinner_textview,
                                                data
                                        )
                            }
                            if (content.groupName == "Still Birth") {
                                val data = ArrayList<String?>()
                                for (questions in content.quesOptions) {
                                    data.add(questions.title)
                                }
                                deliveryOutcomesForPncBinding.spinnerStillBirth.adapter =
                                        ArrayAdapter<String>(
                                                this,
                                                R.layout.layout_rmncha_spinner_textview,
                                                data
                                        )
                            }
                            if (content.groupName == "Delivery Complections") {
                                val data = ArrayList<String?>()
                                for (questions in content.quesOptions) {
                                    data.add(questions.title)
                                }
                                deliveryOutcomesForPncBinding.spinnerDeliveryComplications.adapter =
                                        ArrayAdapter<String>(
                                                this,
                                                R.layout.layout_rmncha_spinner_textview,
                                                data
                                        )
                            }
                            if (content.groupName == "Is Covid Test Done?") {
                                val data = ArrayList<String?>()
                                for (questions in content.quesOptions) {
                                    data.add(questions.title)
                                }
                                deliveryOutcomesForPncBinding.spinnerCovidTestTaken.adapter =
                                        ArrayAdapter<String>(
                                                this,
                                                R.layout.layout_rmncha_spinner_textview,
                                                data
                                        )
                            }
                            if (content.groupName == "Covid Test Result") {
                                val data = ArrayList<String>()
                                for (questions in content.quesOptions) {
                                    data.add(questions.title)
                                }
                                deliveryOutcomesForPncBinding.spinnerCovidTestResult.adapter =
                                        ArrayAdapter<String>(
                                                this,
                                                R.layout.layout_rmncha_spinner_textview,
                                                data
                                        )
                            }
                        }
                    }
                }
    }

    private fun validateDeliveryOutcomesDataAndContinue(
            deliveryOutcomesForPncBinding: DialogueLayoutRmnchaDeliveryOutcomesForPncBinding,
            dialog: Dialog, pncOutcomeRegistered: RMNCHAServiceStatus
    ) {
        val ashaName = binding.spinnerSelectAshaName.selectedItem.toString() + ""
        if (ashaName.equals("select", ignoreCase = true)) {
            RMNCHAUtils.showMyToast(this, "Select Valid Asha Worker Name")
            return
        }
        val lmpData = (deliveryOutcomesForPncBinding.textViewLmpDate.text.toString() + "").replace(
                "/".toRegex(),
                "-"
        )
        val eddData = (deliveryOutcomesForPncBinding.textViewEddDate.text.toString() + "").replace(
                "/".toRegex(),
                "-"
        )
        val financialYear = deliveryOutcomesForPncBinding.textViewFinancialYear.text.toString() + ""
        val deliveryDate =
                (deliveryOutcomesForPncBinding.textViewDeliveryDate.text.toString() + "").replace(
                        "/".toRegex(),
                        "-"
                )
        if (deliveryDate.isEmpty()) {
            RMNCHAUtils.showMyToast(this, "Select Valid Delivery Date")
            return
        }
        val deliveryTime = deliveryOutcomesForPncBinding.textViewDeliveryTime.text.toString() + ""
        val placeOfDelivery =
                deliveryOutcomesForPncBinding.spinnerPlaceOfDelivery.selectedItem.toString() + ""
        if (placeOfDelivery.equals("select", ignoreCase = true)) {
            RMNCHAUtils.showMyToast(this, "Select Place of Delivery")
            return
        }
        val locationOfDelivery =
                deliveryOutcomesForPncBinding.spinnerLocationOfDelivery.selectedItem.toString() + ""
        val deliveryConductedBy =
                deliveryOutcomesForPncBinding.spinnerDeliveryConductedBy.selectedItem.toString() + ""
        if (deliveryConductedBy.equals("select", ignoreCase = true)) {
            RMNCHAUtils.showMyToast(this, "Select Delivery Conducted By")
            return
        }
        val typeOfDelivery =
                deliveryOutcomesForPncBinding.spinnerTypeOfDelivery.selectedItem.toString() + ""
        if (typeOfDelivery.equals("select", ignoreCase = true)) {
            RMNCHAUtils.showMyToast(this, "Select Type of Delivery")
            return
        }
        val outcomesOfDelivery =
                deliveryOutcomesForPncBinding.spinnerOutcomesOfDelivery.selectedItem.toString() + ""
        if (outcomesOfDelivery.equals("select", ignoreCase = true)) {
            RMNCHAUtils.showMyToast(this, "Select Outcomes of Delivery")
            return
        }
        val liveBirth = deliveryOutcomesForPncBinding.spinnerLiveBirth.selectedItem.toString() + ""
        val stillBirth =
                deliveryOutcomesForPncBinding.spinnerStillBirth.selectedItem.toString() + ""
        val deliveryComplications =
                deliveryOutcomesForPncBinding.spinnerDeliveryComplications.selectedItem.toString() + ""
        if (deliveryComplications.equals("select", ignoreCase = true)) {
            RMNCHAUtils.showMyToast(this, "Select Delivery Complications")
            return
        }
        val dischargeDate =
                (deliveryOutcomesForPncBinding.textViewDateOfDischarge.text.toString() + "").replace(
                        "/".toRegex(),
                        "-"
                )
        val dischargeTime = deliveryOutcomesForPncBinding.textViewDischargeTime.text.toString() + ""
        var covidTestTaken = false
        if ("done".equals(
                        deliveryOutcomesForPncBinding.spinnerCovidTestTaken.selectedItem.toString() + "",
                        ignoreCase = true
                )
        ) {
            covidTestTaken = true
        }
        var isCovidResultPositive = false
        if ("positive".equals(
                        deliveryOutcomesForPncBinding.spinnerCovidTestResult.selectedItem.toString() + "",
                        ignoreCase = true
                )
        ) {
            isCovidResultPositive = true
        }
        var isILIExperienced = false
        val ili = deliveryOutcomesForPncBinding.spinnerHavingIli.selectedItem.toString() + ""
        if ("select".equals(ili, ignoreCase = true)) {
            RMNCHAUtils.showMyToast(this, "Select ILI details of PW")
            return
        } else if ("yes".equals(ili, ignoreCase = true)) {
            isILIExperienced = true
        }
        var didContactCovidPatient = false
        val cc = deliveryOutcomesForPncBinding.spinnerCovidContact.selectedItem.toString() + ""
        if (cc.equals("select", ignoreCase = true)) {
            RMNCHAUtils.showMyToast(
                    this,
                    "Select Did PW any contact with Covid-19 positive patients in the last 14 days details"
            )
            return
        } else if (cc.equals("yes", ignoreCase = true)) {
            didContactCovidPatient = true
        }
        val request = RMNCHAPNCDeliveryOutcomesRequest()
        val husbandProperties = coupleData?.target?.properties
        val couple = RMNCHAPNCDeliveryOutcomesRequest.Couple()
        couple.husbandId = husbandProperties?.uuid
        couple.husbandName = husbandProperties?.firstName
        couple.wifeId = selectedWomen?.source?.properties?.uuid
        couple.wifeName = selectedWomen?.source?.properties?.firstName
        couple.registeredBy = ashaName
        couple.registeredByName = ashaName
        couple.registeredOn = RMNCHAUtils.getCurrentDate()
        couple.lastANCVisitDate = lastAncVisitDate
        request.couple = couple
        val menstrualPeriod = RMNCHAPNCDeliveryOutcomesRequest.MensuralPeriod()
        menstrualPeriod.lmpDate = lmpData
        menstrualPeriod.eddDate = eddData
        request.setMenstrualPeriod(menstrualPeriod)
        val deliveryDetails = RMNCHAPNCDeliveryOutcomesRequest.DeliveryDetails()
        deliveryDetails.deliveryDate = RMNCHAUtils.getTimeStampFrom(deliveryDate, deliveryTime)
        deliveryDetails.financialYear = financialYear
        deliveryDetails.place = placeOfDelivery
        deliveryDetails.location = locationOfDelivery
        deliveryDetails.conductedBy = deliveryConductedBy
        deliveryDetails.deliveryType = typeOfDelivery
        deliveryDetails.deliveryOutcome = outcomesOfDelivery.toInt()
        deliveryDetails.liveBirthCount = liveBirth.toInt()
        deliveryDetails.stillBirthCount = stillBirth.toInt()
        deliveryDetails.complications = deliveryComplications
        if (dischargeDate != null && !dischargeDate.isEmpty() && dischargeTime != null && !dischargeTime.isEmpty()) {
            deliveryDetails.dischargeDateTime =
                    RMNCHAUtils.getTimeStampFrom(dischargeDate, dischargeTime)
        }
        request.deliveryDetails = deliveryDetails
        request.isCovidTestDone = covidTestTaken
        request.isCovidResultPositive = isCovidResultPositive
        request.isILIExperienced = isILIExperienced
        request.isDidContactCovidPatient = didContactCovidPatient
        showSubmitFormAlert(request, dialog, RMNCHAServiceStatus.PNC_OUTCOME_REGISTERED)
    }

    private fun showInfantDetailsAlert() {
        infantDialog = Dialog(this)
        val infantDetailsForPncBinding =
                DataBindingUtil.inflate<DialogueLayoutRmnchaInfantDetailsForPncBinding>(
                        infantDialog!!.layoutInflater,
                        R.layout.dialogue_layout_rmncha_infant_details_for_pnc,
                        null,
                        false
                )
        progressBar = infantDetailsForPncBinding.progressBar
        infantDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        infantDialog?.setCancelable(false)
        infantDialog?.setContentView(infantDetailsForPncBinding.root)
        infantDialog?.show()
        val window = infantDialog?.window
        window?.setLayout(1250, WindowManager.LayoutParams.WRAP_CONTENT)
        val infantDetailsView =
                supportFragmentManager.findFragmentById(R.id.infant_fragment) as PNCInfantDetailsView?
        infantDetailsForPncBinding.closeDialogue.setOnClickListener { v: View? ->
            if (infantDetailsView != null) {
                supportFragmentManager.beginTransaction().remove(infantDetailsView)
                        .commitAllowingStateLoss()
            }
            infantDialog?.dismiss()
        }
        PNCWomenAdapter.setPNCWomenView(infantDetailsForPncBinding.womenLayout, selectedWomen)
    }

    private fun goToPNCServiceScreen() {
        val intent = Intent(this, PNCServiceActivity::class.java)
        val bundle = Bundle()
        bundle.putString(PNCDetailsActivity.PARAM_1, houseHoldUUID)
        bundle.putString(PNCDetailsActivity.PARAM_SUB_CENTER, subCenterUUID)
        bundle.putSerializable(PNCDetailsActivity.SELECTED_WOMEN_KEY, selectedWomen)
        intent.putExtras(bundle)
        startActivity(intent)
        this.finish()
    }

    private val ashaWorkerData: Unit
        get() {
            viewModel.getAshaWorkerLiveData(subCenterUUID)
                    .observe(this) { contents: List<AshaWorkerResponse.Content> ->
                        ashaWorkersList = contents
                        setAshaWorkersAdapter()
                    }
        }

    fun setAshaWorkersAdapter() {
        binding.spinnerSelectAshaName.adapter = ArrayAdapter<String>(
                this,
                R.layout.layout_rmncha_spinner_textview,
                RMNCHAUtils.getAshaWorkerNamesList(ashaWorkersList)
        )
    }

    private fun setSelectedWomenView() {
        try {
            selectedWomen =
                    intent.extras?.getSerializable(SELECTED_WOMEN_KEY) as RMNCHAPNCWomenResponse.Content?
            selectedWomenUUID = selectedWomen?.source?.properties?.uuid
            setCoupleDetails()
            PNCWomenAdapter.setPNCWomenView(binding.root, selectedWomen)
        } catch (e: Exception) {
            RMNCHAUtils.showMyToast(this, "Selected Women is Invalid")
        }
    }

    private fun setCoupleDetails() {
        try {
            showProgressBar()
            viewModel.getRMNCHACoupleDetailsLiveData(selectedWomen?.source?.properties?.uuid)
                    .observe(this) { contents: List<RMNCHACoupleDetailsResponse.Content?>? ->
                        hideProgressBar()
                        if (contents != null && contents.isNotEmpty()) {
                            coupleData = contents[0]
                            selectedWomen?.source?.properties?.rchId=coupleData?.source?.properties?.rchId
                            selectedWomen?.source?.properties?.healthID =
                                    coupleData?.source?.properties?.healthID
                            binding.tvRCHID.text = coupleData?.source?.properties?.rchId
                            binding.hhHeadName.text =
                                    RMNCHAUtils.setNonNullValue(selectedWomen?.source?.properties?.houseHeadName)
                            val husbandProperties = coupleData?.target?.properties
                            val husbandAge = RMNCHAUtils.getAgeFromDOB(husbandProperties?.dateOfBirth)
                            val husbandGender = RMNCHAUtils.setNonNullValue(husbandProperties?.gender)
                            val husbandName = husbandProperties?.firstName
                            pNCRegistrationId = coupleData?.relationship?.properties?.pncRegistrationId
                            binding.headerHusbandName.text =
                                    "$husbandName - $husbandAge - $husbandGender"
                            selectedWomen?.source?.properties?.rmnchaServiceStatus = coupleData?.source?.properties?.rmnchaServiceStatus
                            selectedWomen?.source?.properties?.rmnchaServiceStatus?.let {
                                setNextViewStatus(
                                        it
                                )
                            }
                            aNCRegistrationDetails
                            PNCWomenAdapter.setPNCWomenView(binding.root, selectedWomen)
                        } else {
                            RMNCHAUtils.showMyToast(
                                    this,
                                    "Selected Woman is not Eligible for PNC"
                            )
                        }
                    }
        } catch (e: Exception) {
            hideProgressBar()
            RMNCHAUtils.showMyToast(this, "Error fetching Couple Data")
        }
    }

    private val aNCRegistrationDetails: Unit
        get() {
            try {
                val ancRegistrationId = coupleData?.relationship?.properties?.ancRegistrationId
                val ancServiceId = coupleData?.relationship?.properties?.ancServiceId
                if (ancRegistrationId != null && ancRegistrationId.isNotEmpty()) {
                    showProgressBar()
                    viewModel.getANCRegistrationData(ancRegistrationId)
                            .observe(this) { contents: RMNCHAANCRegistrationResponse? ->
                                getANCLastVisitDate(ancServiceId)
                                if (contents != null) {
                                    ancRegistrationData = contents
                                    if (ancRegistrationData != null && ancRegistrationData?.mensuralPeriod != null) {
                                        lmpDateFromANC = ancRegistrationData?.mensuralPeriod!!.lmpDate
                                        eddDateFromANC = ancRegistrationData?.mensuralPeriod!!.eddDate
                                    }
                                } else {
                                    RMNCHAUtils.showMyToast(this, "Error : " + viewModel.errorMessage)
                                }
                            }
                }
            } catch (e: Exception) {
                hideProgressBar()
                RMNCHAUtils.showMyToast(this, "Error fetching ANC details")
            }
        }

    private fun getANCLastVisitDate(ancServiceId: String?) {
        try {
            if (ancServiceId != null && ancServiceId.length > 0) {
                showProgressBar()
                viewModel.getANCServiceHistoryLiveData(ancServiceId)
                        .observe(this) { contents: List<RMNCHAANCVisitLogResponse>? ->
                            hideProgressBar()
                            if (contents != null && contents.size > 0) {
                                val visitLogResponse = contents[contents.size - 1]
                                lastAncVisitDate = visitLogResponse.getVisitDate()
                            } else {
                                RMNCHAUtils.showMyToast(
                                        this,
                                        "Error fetching Visit Logs  : " + viewModel.errorMessage
                                )
                            }
                        }
            }
        } catch (e: Exception) {
            hideProgressBar()
            RMNCHAUtils.showMyToast(this, "Error fetching ANC Visit date")
        }
    }

    private fun showSubmitFormAlert(
            request: Any,
            mainDialogue: Dialog?,
            type: RMNCHAServiceStatus
    ) {
        val dialog = Dialog(this)
        val binding = DataBindingUtil.inflate<DialogueLayoutRmnchaSubmitFormBinding>(
                dialog.layoutInflater,
                R.layout.dialogue_layout_rmncha_submit_form,
                null,
                false
        )
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.show()
        binding.closeDialogue.setOnClickListener { v: View? -> dialog.dismiss() }
        binding.buttonEdit.setOnClickListener { v: View? -> dialog.dismiss() }
        binding.buttonSubmit.setOnClickListener { view: View? ->
            if (type == RMNCHAServiceStatus.PNC_OUTCOME_REGISTERED) {
                makeOutcomesRequest(mainDialogue, request as RMNCHAPNCDeliveryOutcomesRequest, type)
            } else {
                makeInfantRequest(mainDialogue, request as RMNCHAPNCInfantRequest, type)
            }
            dialog.dismiss()
        }
    }

    private fun makeInfantRequest(
            mainDialogue: Dialog?,
            request: RMNCHAPNCInfantRequest,
            type: RMNCHAServiceStatus
    ) {
        showProgressBar()
        viewModel.makePNCInfantRegistrationRequest(pNCRegistrationId, request)
                .observe(this) { contents: RMNCHAPNCInfantResponse? ->
                    hideProgressBar()
                    if (mainDialogue != null && mainDialogue.isShowing) {
                        mainDialogue.dismiss()
                    }
                    showSaveSuccessAlert(type)
                }
    }

    private fun makeOutcomesRequest(
            mainDialogue: Dialog?,
            request: RMNCHAPNCDeliveryOutcomesRequest,
            type: RMNCHAServiceStatus
    ) {
        showProgressBar()
        viewModel.makePNCDeliveryOutcomesRegistrationRequest(request)
                .observe(this) { contents: RMNCHAPNCDeliveryOutcomesResponse? ->
                    hideProgressBar()
                    if (contents != null) {
                        if (mainDialogue != null && mainDialogue.isShowing) {
                            mainDialogue.dismiss()
                        }
                        showSaveSuccessAlert(type)
                    } else {
                        RMNCHAUtils.showMyToast(
                                this,
                                "Failed Outcomes request" + viewModel.errorMessage
                        )
                    }
                }
    }

    private fun showSaveSuccessAlert(type: RMNCHAServiceStatus) {
        val dialog = Dialog(this)
        val binding = DataBindingUtil.inflate<DialogueLayoutRmnchaSavedSuccessBinding>(
                dialog.layoutInflater,
                R.layout.dialogue_layout_rmncha_saved_success,
                null,
                false
        )
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.show()
        binding.message.text = "Form has been saved successfully!"
        binding.buttonExit.text = "Click here to Continue"
        binding.closeDialogue.setOnClickListener { v: View? ->
            viewModel.viewModelScope.launch {
                delay(4500)
                dialog.dismiss()
                setCoupleDetails()
            }
        }
        binding.buttonExit.setOnClickListener { view: View? ->
            viewModel.viewModelScope.launch {
                delay(4500)
                dialog.dismiss()
                setCoupleDetails()
            }
        }
    }

    private fun setNextViewStatus(type: RMNCHAServiceStatus) {
        if (type == RMNCHAServiceStatus.PNC_INFANT_REGISTERED || type== RMNCHAServiceStatus.PNC_ONGOING) {
            goToPNCServiceScreen()
        } else if (type == RMNCHAServiceStatus.PNC_OUTCOME_REGISTERED) {
            outcomeRegistrationDetails
        } else {
            setDeliveryOutcomeEnabled()
        }
    }

    private val outcomeRegistrationDetails: Unit
        get() {
            showProgressBar()
            if (pNCRegistrationId != null) {
                getDeliveryOutcomeData()
            } else {
                Toast.makeText(this,"Fetching Couple Details",Toast.LENGTH_SHORT).show()
                viewModel.viewModelScope.launch {
                    delay(1000)
                    setCoupleDetails()
                    getDeliveryOutcomeData()
                }
            }
        }

    private fun getDeliveryOutcomeData(){
        setInfantDetailsEnabled()
        viewModel.getDeliveryOutcomesData(pNCRegistrationId)
                .observe(this) { contents: RMNCHAPNCDeliveryOutcomesResponse? ->
                    hideProgressBar()
                    if (contents != null) {
                        infantDetails = contents
                        setInfantDetailsEnabled()
                    } else {
                        RMNCHAUtils.showMyToast(
                                this,
                                "Unable to get OutcomeRegistrationDetails " + viewModel.errorMessage
                        )
                    }
                }
    }

    private fun setInfantDetailsEnabled() {
        binding.layoutInputButtons.visibility = View.VISIBLE
        binding.buttonDeliveryOutcome.setImageDrawable(resources.getDrawable(R.drawable.ic_delivery_outcome_saved))
        binding.buttonInfant.setImageDrawable(resources.getDrawable(R.drawable.ic_infant_details_enabled))
        binding.buttonDeliveryOutcome.isEnabled = false
        binding.buttonInfant.isEnabled = true
        binding.buttonDone.isEnabled = false
        val deliveryOutcomeParams =
                binding.buttonDeliveryOutcome.layoutParams as RelativeLayout.LayoutParams
        deliveryOutcomeParams.width = 250
        binding.buttonDeliveryOutcome.layoutParams = deliveryOutcomeParams
        val infantParams = binding.buttonInfant.layoutParams as RelativeLayout.LayoutParams
        infantParams.width = 300
        binding.buttonInfant.layoutParams = infantParams
    }

    private fun setDeliveryOutcomeEnabled() {
        binding.layoutInputButtons.visibility = View.VISIBLE
        binding.buttonDeliveryOutcome.setImageDrawable(resources.getDrawable(R.drawable.ic_delivery_outcome_enabled))
        binding.buttonInfant.setImageDrawable(resources.getDrawable(R.drawable.ic_infant_details_disabled))
        binding.buttonDeliveryOutcome.isEnabled = true
        val deliveryOutcomeParams =
                binding.buttonDeliveryOutcome.layoutParams as RelativeLayout.LayoutParams
        deliveryOutcomeParams.width = 300
        binding.buttonDeliveryOutcome.layoutParams = deliveryOutcomeParams
        val infantParams = binding.buttonInfant.layoutParams as RelativeLayout.LayoutParams
        infantParams.width = 250
        binding.buttonInfant.layoutParams = infantParams
        binding.buttonInfant.isEnabled = false
        binding.buttonDone.isEnabled = false
    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        if (progressBar != null) progressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        if (progressBar != null) progressBar!!.visibility = View.GONE
    }

    fun onPNCSubmitClicked(request: RMNCHAPNCInfantRequest) {
        showSubmitFormAlert(request, infantDialog, RMNCHAServiceStatus.PNC_INFANT_REGISTERED)
    }

    companion object {
        const val PARAM_1 = "param_1"
        const val PARAM_SUB_CENTER = "PARAM_SUB_CENTER"
        const val SELECTED_WOMEN_KEY = "SELECTED_WOMEN_KEY"
    }
}