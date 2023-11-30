package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentIdspBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.extensions.customAlert
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.individual.multiselectionspinner.MultiSelectionSpinnerDialog
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IDSPViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualSelectionViewModel
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class IDSPFragment : Fragment(), AdapterView.OnItemSelectedListener,
    MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener {

    private lateinit var binding: FragmentIdspBinding
    private lateinit var feverView: View
    private lateinit var coughView: View
    private lateinit var looseStoolsView: View
    private lateinit var animalBiteView: View
    private lateinit var othersView: View
    private lateinit var jaundiceView: View
    private val viewModel: IDSPViewModel by viewModels()
    private val individualSelectionViewModel: IndividualSelectionViewModel by activityViewModels()
    private var isReferredToPhc: String = ""
    private var flowType: String = ""
    private var bloodSeen: String = ""
    private var dehydration: String = ""
    private var feverDuration: String = ""
    private var feverSymptom: String = ""
    private var looseStoolsDuration: String = ""
    private var dehydrationExtent: String = ""
    private var jaundiceDuration: String = ""
    private var otherSymptom: String = ""
    private var animalBiteType: String = ""
    private var reportDeath: Boolean = false
    var calendar: Calendar = Calendar.getInstance()
    private lateinit var currentDate: String
    private lateinit var answer: Response
    private var coughDuration = ""
    private var dateOfDeath: String? = null
    private var symptomsData: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, com.ssf.homevisit.R.layout.fragment_idsp, container, false
        )
        feverView = inflater.inflate(com.ssf.homevisit.R.layout.fever_view, container, false)
        coughView = inflater.inflate(com.ssf.homevisit.R.layout.cough_view, container, false)
        looseStoolsView =
            inflater.inflate(com.ssf.homevisit.R.layout.loose_stools_view, container, false)
        animalBiteView =
            inflater.inflate(com.ssf.homevisit.R.layout.animal_bite_view, container, false)
        othersView = inflater.inflate(com.ssf.homevisit.R.layout.others_view, container, false)
        jaundiceView = inflater.inflate(com.ssf.homevisit.R.layout.jaundice_view, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initClick()
        initObserver()
        setData()
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
        dateOfDeath = date
        binding.tvCalendar.text = date
    }


    private fun initClick() {
        binding.btnContinue.setOnClickListener {
            binding.progress.visible()
            viewModel.saveIdspData(
                requestData = IdspData(
                    individualSelectionViewModel.citizenUuid,
                    symptomType = flowType,
                    fever = FeverRequestBody(
                        suspectedPeriod = feverDuration, additionalSymptoms = feverSymptom
                    ),
                    cough = CoughRequestBody(
                        suspectedPeriod = coughDuration
                    ),
                    jaundice = JaundiceRequestBody(
                        suspectedPeriod = jaundiceDuration
                    ),
                    animalBite = AnimalBiteRequestBody(
                        typeOfBite = animalBiteType
                    ),
                    others = OtherRequestBody(
                        otherSymptom = otherSymptom, additionalSymptoms = null
                    ),
                    looseStools = LooseStoolsRequestBody(
                        suspectedPeriod = looseStoolsDuration,
                        hasDehydration = dehydration,
                        extendOfDehydration = dehydrationExtent,
                        isBloodInStool = bloodSeen
                    ),
                    isReferredToPhc = isReferredToPhc,
                    dateOfSurveillance = currentDate,
                    dateOfDeath = dateOfDeath
                )
            )
        }
        binding.spinnerMultiSpinner.setOnClickListener {
            binding.spinnerMultiSpinner.setAdapterWithOutImage(this.context, symptomsData, this)
            binding.spinnerMultiSpinner.initMultiSpinner(
                this.context, binding.spinnerMultiSpinner
            )
        }
        binding.llCalendar.setOnClickListener {
            context?.let {
                DatePickerDialog(
                    it,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
        binding.tvReportDeath.setOnClickListener {
            customAlert(
                titleText = "Submit Nil Report?",
                subTitleText = "Do you want to report death for this person?",
                icon = R.drawable.ic_nil_report_question,
                continueFlow = true,
                buttonText = "ReportDeath",
                ContinueButtonClick = {
                    reportDeath = true
                    binding.ivSelectHousehold.background =
                        context?.resources?.getDrawable(com.ssf.homevisit.R.drawable.ic_red_menu)
                    binding.tvSelectHousehold.text = "Report Death for IDSP"
                    binding.tvSelectHousehold.setTextColor(android.graphics.Color.parseColor("#DA1010"))
                    binding.llDateOfDeath.visible()
                    binding.tvReportDeath.gone()
                })
        }

        binding.tvCancel.setOnClickListener {
            customAlert(titleText = "Would you like to exit without saving?",
                exitFlow = true,
                icon = R.drawable.exit_error,
                exitButtonClick = {
                    if (reportDeath) {
                        customAlert(titleText = "Submit Death Report?",
                            subTitleText = "You will not be able to edit this form once saved.",
                            buttonText = "Submit",
                            continueFlow = true,
                            icon = R.drawable.ic_nil_report_question,
                            ContinueButtonClick = {
                                customAlert(titleText = "Form has been saved successfully!",
                                    subTitleText = "Click here to Continue",
                                    continueFlow = true,
                                    icon = R.drawable.ic_mobile_success,
                                    ContinueButtonClick = {
                                        findNavController().popBackStack(
                                            R.id.individualSelectionFragment, false
                                        )
                                    })
                            })
                    } else {
                        findNavController().popBackStack(R.id.individualSelectionFragment, false)
                    }
                })
        }
    }


    private fun setData() {
        individualSelectionViewModel.individualDetailPropertyData?.let { data ->
            individualSelectionViewModel.citizenUuid = data.uuid.toString()
            data.imageUrls?.let {
                if (it.isEmpty()) {
                    binding.ecPhoto.background =
                        context?.resources?.getDrawable(R.drawable.ic_image_place_holder)
                } else {
                    Picasso.get().load(it[0]).resize(100, 100).into(binding.ecPhoto)
                }
            }
            if (data.healthID?.isNotEmpty() == true) {
                binding.ecHealthId.text = "Health ID number : " + data.healthID
            }
            binding.ecName.text = data.firstName
            if (data.contact?.isNotEmpty() == true) {
                binding.ecPhoneNo.text = "Ph :" + data.contact
            }
            if (data.dateOfBirth?.isNotEmpty() == true) {
                binding.ecDOB.text = "DOB :" + data.dateOfBirth
            }
            if (data.age == null) {
                if (data.gender?.isNotEmpty() == true) {
                    binding.ecAge.text = data.gender
                } else {
                    binding.ecAge.gone()
                }
            } else {
                if (data.gender?.isNotEmpty() == true) {
                    binding.ecAge.text = data.age.toInt().toString() + "years" + " - " + data.gender
                } else {
                    binding.ecAge.text = data.age.toInt().toString()
                }
            }
        }
    }

    private fun getData() {
        individualSelectionViewModel.getIndividualSurveillanceData(AppDefines.HealthAndWellness_IndividualLevel_IDSP)
    }

    @SuppressLint("CutPasteId")
    private fun initFever(response: Response) {
        response.formItems.forEach {
            if (it.groupName == "How long has it been since the onset of fever?") {
                val feverData = mutableListOf<String>()
                feverData.add("Select")
                it.elements.forEach {
                    feverData.add(it.title)
                }
                feverView.findViewById<Spinner>(R.id.spinnerFeverDuration).onItemSelectedListener =
                    this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, feverData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    feverView.findViewById<Spinner>(R.id.spinnerFeverDuration).adapter =
                        chooseDefectAdapter

                }
            }
            if (it.groupName == "Any additional symptoms?") {
                val feverAdditionalSymptom = mutableListOf<String>()
                feverAdditionalSymptom.add("Select")
                it.elements.forEach {
                    feverAdditionalSymptom.add(it.title)
                }
                feverView.findViewById<Spinner>(R.id.spinnerFeverAdditionalSymptoms).onItemSelectedListener =
                    this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, feverAdditionalSymptom
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    feverView.findViewById<Spinner>(R.id.spinnerFeverAdditionalSymptoms).adapter =
                        chooseDefectAdapter
                }
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun initAnimalBite(response: Response) {
        animalBiteView.findViewById<AppCompatTextView>(R.id.tvAnimalBite).text =
            "Select the type of animal  bite?*"
        response.formItems.forEach {
            if (it.groupName == "Select the type of animal bite?") {
                val animalBiteData = mutableListOf<String>()
                animalBiteData.add("Select")
                it.elements.forEach {
                    animalBiteData.add(it.title)
                }
                animalBiteView.findViewById<Spinner>(R.id.spinnerAnimalBite).onItemSelectedListener =
                    this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, animalBiteData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    animalBiteView.findViewById<Spinner>(R.id.spinnerAnimalBite).adapter =
                        chooseDefectAdapter

                }
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun initCough() {
        answer.formItems.forEach {
            if (it.groupName == "How long has it been since the onset of cough?") {
                val coughSymptom = mutableListOf<String>()
                coughSymptom.add("Select")
                it.elements.forEach {
                    coughSymptom.add(it.title)
                }
                coughView.findViewById<Spinner>(R.id.spinnerSpecifyCoughSymptom).onItemSelectedListener =
                    this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, coughSymptom
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    coughView.findViewById<Spinner>(R.id.spinnerSpecifyCoughSymptom).adapter =
                        chooseDefectAdapter

                }
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun initOtherSymptom(response: Response) {
        othersView.findViewById<AppCompatTextView>(R.id.tvSpecifyOtherSymptoms).text =
            "Specify other symptoms*"
        response.formItems.forEach {
            if (it.groupName == "Specify other symptoms") {
                val otherSymptomData = mutableListOf<String>()
                otherSymptomData.add("Select")
                it.elements.forEach {
                    otherSymptomData.add(it.title)
                }
                othersView.findViewById<Spinner>(R.id.spinnerOtherSymptoms).onItemSelectedListener =
                    this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, otherSymptomData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    othersView.findViewById<Spinner>(R.id.spinnerOtherSymptoms).adapter =
                        chooseDefectAdapter

                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "CutPasteId")
    private fun initJaundice(response: Response) {
        jaundiceView.findViewById<AppCompatTextView>(R.id.tvJaundiceDuration).text =
            "How long has it been since the onset of jaundice?*"
        response.formItems.forEach {
            if (it.groupName == "How long has it been since the onset of jaundice?") {
                val jaundiceData = mutableListOf<String>()
                jaundiceData.add("Select")
                it.elements.forEach {
                    jaundiceData.add(it.title)
                }
                jaundiceView.findViewById<Spinner>(R.id.spinnerJaundiceDuration).onItemSelectedListener =
                    this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, jaundiceData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    jaundiceView.findViewById<Spinner>(R.id.spinnerJaundiceDuration).adapter =
                        chooseDefectAdapter

                }
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun initLooseStools() {
        answer.formItems.forEach {
            if (it.groupName == "How long has it been since the onset of Loose Stools?") {
                val looseStoolSymptomData = mutableListOf<String>()
                looseStoolSymptomData.add("Select")
                it.elements.forEach {
                    looseStoolSymptomData.add(it.title)
                }
                looseStoolsView.findViewById<Spinner>(R.id.spinnerSpecifyLooseStoolsSymptom).onItemSelectedListener =
                    this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, looseStoolSymptomData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    looseStoolsView.findViewById<Spinner>(R.id.spinnerSpecifyLooseStoolsSymptom).adapter =
                        chooseDefectAdapter
                }
            }
            if (it.groupName == "What is the extent of dehydration?") {
                val spinnerExtentDehydrationData = mutableListOf<String>()
                spinnerExtentDehydrationData.add("Select")
                it.elements.forEach {
                    spinnerExtentDehydrationData.add(it.title)
                }
                looseStoolsView.findViewById<Spinner>(R.id.spinnerExtentDehydration).onItemSelectedListener =
                    this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, spinnerExtentDehydrationData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    looseStoolsView.findViewById<Spinner>(R.id.spinnerExtentDehydration).adapter =
                        chooseDefectAdapter
                }
            }
            looseStoolsView.findViewById<RadioButton>(R.id.yes_radio_button_BloodSeen)
                .setOnClickListener {
                    bloodSeen = true.toString()
                }
            looseStoolsView.findViewById<RadioButton>(R.id.no_radio_button_BloodSeen)
                .setOnClickListener {
                    bloodSeen = false.toString()
                }
            looseStoolsView.findViewById<RadioButton>(R.id.yes_radio_button_dehydration)
                .setOnClickListener {
                    dehydration = true.toString()
                    looseStoolsView.findViewById<AppCompatTextView>(R.id.tvExtentDehydration)
                        .visible()
                    looseStoolsView.findViewById<LinearLayout>(R.id.spinExtentDehydration).visible()
                }
            looseStoolsView.findViewById<RadioButton>(R.id.no_radio_button_dehydration)
                .setOnClickListener {
                    dehydration = false.toString()
                }
        }
    }

    private fun initUi(response: Response) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        currentDate = sdf.format(Date())
        binding.tvCurrentDate.text = currentDate.split("T").first()
        response.formItems.forEach {
            if (it.groupName == "Select symptom(s)") {
                it.elements.forEach {
                    symptomsData.add(it.title)
                }
            }
        }
        binding.checkboxReferral.setOnClickListener {
            isReferredToPhc = true.toString()
        }
    }

    private fun initObserver() {
        individualSelectionViewModel.individualSurveillanceData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                if (response != null) {
                    answer = response
                    initUi(response)
                }
            })
        viewModel.saveDataResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progress.gone()
            if (it != null) {
                apiSuccessFailureDialog(isSuccess = true, successButtonClick = {
                    Toast.makeText(
                        context, "Response  Saved Successfully", Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack(R.id.individualSelectionFragment, false)
                })
            } else {
                apiSuccessFailureDialog(isSuccess = false, failureButtonCLick = {
                    Toast.makeText(
                        context, "Response  Saved Failed", Toast.LENGTH_SHORT
                    ).show()
                    activity?.finish()
                })
            }
        })
    }

    @SuppressLint("CutPasteId")
    override fun OnMultiSpinnerItemSelected(chosenItems: MutableList<String>?) {
        binding.llParent.removeAllViews()
        for (i in chosenItems!!.indices) {
            flowType = chosenItems[i]
            if (!reportDeath) {
                binding.tvSelectHousehold.text = "Fill S-Form for IDSP"
            }
            when (flowType) {
                "Jaundice" -> {
                    if (jaundiceView.parent == null) {
                        binding.llParent.addView(jaundiceView)
                        initJaundice(answer)
                    }
                }
                "Animal Bite" -> {
                    if (animalBiteView.parent == null) {
                        binding.llParent.addView(animalBiteView)
                        initAnimalBite(answer)
                    }
                }
                "Fever" -> {
                    if (feverView.parent == null) {
                        binding.llParent.addView(feverView)
                        initFever(answer)
                    }
                }
                "Other Symptoms" -> {
                    if (othersView.parent == null) {
                        binding.llParent.addView(othersView)
                        initOtherSymptom(answer)
                    }
                }
                "Cough" -> {
                    if (coughView.parent == null) {
                        binding.llParent.addView(coughView)
                        initCough()
                    }
                }
                "Loose Stools" -> {
                    if (looseStoolsView.parent == null) {
                        binding.llParent.addView(looseStoolsView)
                        initLooseStools()
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val data = p0?.getItemAtPosition(p2).toString()
        if (p0?.equals(feverView.findViewById(R.id.spinnerFeverDuration)) == true) {
            feverDuration = data
        }
        if (p0?.equals(feverView.findViewById(R.id.spinnerFeverAdditionalSymptoms)) == true) {
            feverSymptom = data
        }
        if (p0?.equals(looseStoolsView.findViewById(R.id.spinnerSpecifyLooseStoolsSymptom)) == true) {
            looseStoolsDuration = data
        }
        if (p0?.equals(looseStoolsView.findViewById(R.id.spinnerExtentDehydration)) == true) {
            dehydrationExtent = data
        }
        if (p0?.equals(coughView.findViewById(R.id.spinnerSpecifyCoughSymptom)) == true) {
            coughDuration = data
        }
        if (p0?.equals(animalBiteView.findViewById(R.id.spinnerAnimalBite)) == true) {
            animalBiteType = data
        }
        if (p0?.equals(jaundiceView.findViewById(R.id.spinnerJaundiceDuration)) == true) {
            jaundiceDuration = data
        }
        if (p0?.equals(othersView.findViewById(R.id.spinnerOtherSymptoms)) == true) {
            otherSymptom = data
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}