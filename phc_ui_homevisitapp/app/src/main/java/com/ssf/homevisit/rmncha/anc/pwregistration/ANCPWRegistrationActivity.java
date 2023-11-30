package com.ssf.homevisit.rmncha.anc.pwregistration;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.RMNCHA_DATE_FORMAT;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getAgeFromDOB;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getAshaWorkerNamesList;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentDate;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentFinancialYear;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentYear;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDateFromDatePicker;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDateToView;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDummyBoolList;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getExpectedDeliveryDate;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getResidentStatus;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.loadMemberImage;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.setNonNullValue;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showSelectComplicationsAlertDialogue;
import static com.ssf.homevisit.rmncha.anc.selectwomen.SelectWomenForANCActivity.PARAM_ANC_SELECTED_WOMEN;
import static com.ssf.homevisit.rmncha.anc.selectwomen.SelectWomenForANCActivity.PARAM_SUB_CENTER;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ssf.homevisit.R;
import com.ssf.homevisit.databinding.ActivityRmnchaAncPwRegistrationBinding;
import com.ssf.homevisit.models.AshaWorkerResponse;
import com.ssf.homevisit.models.FormUtilityResponse;
import com.ssf.homevisit.models.RMNCHACoupleDetailsResponse;
import com.ssf.homevisit.models.RMNCHAANCPWsResponse;
import com.ssf.homevisit.models.RMNCHAANCRegistrationRequest;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.base.RMNCHABaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ANCPWRegistrationActivity extends RMNCHABaseActivity {

    private String[] complicationList;
    private boolean[] complicationSelectedListOne;
    private boolean[] complicationSelectedListTwo;
    private ANCPWRegistrationViewModel viewModel;
    private ActivityRmnchaAncPwRegistrationBinding binding;
    private List<AshaWorkerResponse.Content> ashaWorkersList = new ArrayList<>();
    private RMNCHAANCPWsResponse.Content selectedWomen;
    private RMNCHACoupleDetailsResponse.Content coupleData;
    private String rchID;
    private String wifeID;
    private String wifeName;
    private String wifePhone;
    private String husbandID;
    private String husbandName;
    private String husbandPhone;
    private String currentDate;
    private String currentYear;
    private String financialYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rmncha_anc_pw_registration);
        viewModel = new ViewModelProvider(this).get(ANCPWRegistrationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        initializeViews();
    }

    private void initializeViews() {
        getFormUtilityData();
        binding.textViewVdrlTestDate.setOnClickListener(view -> getDateFromDatePicker((TextView) view));
        binding.textViewHivTestDate.setOnClickListener(view -> getDateFromDatePicker((TextView) view));
        binding.textViewHbsTestDate.setOnClickListener(view -> getDateFromDatePicker((TextView) view));
        binding.textViewLmpDate.setOnClickListener(view -> getDateFromDatePicker((TextView) view, (year, month, day) -> {
            //EDD
            binding.textviewEddDate.setText(getExpectedDeliveryDate(year, month, day));
            //Within 12 Weeks
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                Calendar calender = Calendar.getInstance();
                Date lmpDate = sdf.parse(month + "/" + day + "/" + year);
                Date currDate = sdf.parse((calender.get(Calendar.MONTH) + 1) + "/" + calender.get(Calendar.DAY_OF_MONTH) + "/" + calender.get(Calendar.YEAR));
                long diffInMillis = Math.abs(currDate.getTime() - lmpDate.getTime());
                long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                long diffInWeeks = diffInDays / (7);
                if (diffInWeeks < 12) {
                    binding.textViewRegisteredWithIn.setText("Yes");
                } else {
                    binding.textViewRegisteredWithIn.setText("No");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }));
        binding.textviewComplicationsOne.setOnClickListener(view -> showSelectComplicationsAlertDialogue(this, complicationList, complicationSelectedListOne, (TextView) view, binding.textviewLastComplicationsSummary));
        binding.textviewComplicationsTwo.setOnClickListener(view -> showSelectComplicationsAlertDialogue(this, complicationList, complicationSelectedListTwo, (TextView) view, binding.textviewLastToLastComplicationsSummary));
        binding.buttonCancel.setOnClickListener(view -> finish());
        binding.buttonSubmit.setOnClickListener(view -> validateANCRegistration());
        binding.textviewOthers.setVisibility(View.GONE);
        binding.checkboxDiabetes.setOnCheckedChangeListener((compoundButton, isChecked) -> clearIfNoneChecked(isChecked));
        binding.checkboxHeartDisease.setOnCheckedChangeListener((compoundButton, isChecked) -> clearIfNoneChecked(isChecked));
        binding.checkboxAsthma.setOnCheckedChangeListener((compoundButton, isChecked) -> clearIfNoneChecked(isChecked));
        binding.checkboxHypertension.setOnCheckedChangeListener((compoundButton, isChecked) -> clearIfNoneChecked(isChecked));
        binding.checkboxTuberculosis.setOnCheckedChangeListener((compoundButton, isChecked) -> clearIfNoneChecked(isChecked));
        binding.checkboxEpileptic.setOnCheckedChangeListener((compoundButton, isChecked) -> clearIfNoneChecked(isChecked));
        binding.checkboxHepatitis.setOnCheckedChangeListener((compoundButton, isChecked) -> clearIfNoneChecked(isChecked));
        binding.checkboxJaundice.setOnCheckedChangeListener((compoundButton, isChecked) -> clearIfNoneChecked(isChecked));
        binding.checkboxHiv.setOnCheckedChangeListener((compoundButton, isChecked) -> clearIfNoneChecked(isChecked));
        binding.checkboxSti.setOnCheckedChangeListener((compoundButton, isChecked) -> clearIfNoneChecked(isChecked));
        binding.checkboxOthers.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            clearIfNoneChecked(isChecked);
            if (isChecked) {
                binding.textviewOthers.setVisibility(View.VISIBLE);
            } else {
                binding.textviewOthers.setVisibility(View.GONE);
                binding.textviewOthers.setText("");
            }
        });
        binding.checkboxNone.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                binding.checkboxDiabetes.setChecked(false);
                binding.checkboxHeartDisease.setChecked(false);
                binding.checkboxAsthma.setChecked(false);
                binding.checkboxHypertension.setChecked(false);
                binding.checkboxTuberculosis.setChecked(false);
                binding.checkboxEpileptic.setChecked(false);
                binding.checkboxHepatitis.setChecked(false);
                binding.checkboxJaundice.setChecked(false);
                binding.checkboxHiv.setChecked(false);
                binding.checkboxSti.setChecked(false);
                binding.checkboxOthers.setChecked(false);
                binding.textviewOthers.setVisibility(View.GONE);
                binding.textviewOthers.setText("");
            }
        });
        binding.spinnerNoOfPregnancies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    binding.textViewLastPregnancyTitle.setVisibility(View.GONE);
                    binding.layoutLastPregnancyDetails.setVisibility(View.GONE);
                    binding.layoutLastToLastPregnancyDetails.setVisibility(View.GONE);
                } else if (position == 1) {
                    binding.textViewLastPregnancyTitle.setText("Details of Last Pregnancy");
                    binding.textViewLastPregnancyTitle.setVisibility(View.VISIBLE);
                    binding.layoutLastPregnancyDetails.setVisibility(View.VISIBLE);
                    binding.layoutLastToLastPregnancyDetails.setVisibility(View.GONE);
                } else {
                    binding.textViewLastPregnancyTitle.setText("Details of Last Two Pregnancy");
                    binding.textViewLastPregnancyTitle.setVisibility(View.VISIBLE);
                    binding.layoutLastPregnancyDetails.setVisibility(View.VISIBLE);
                    binding.layoutLastToLastPregnancyDetails.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerVdrlTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = binding.spinnerVdrlTest.getSelectedItem() + "";
                if (data.equalsIgnoreCase("done")) {
                    binding.layoutVrdlDetails.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutVrdlDetails.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerHivTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = binding.spinnerHivTest.getSelectedItem() + "";
                if (data.equalsIgnoreCase("done")) {
                    binding.layoutHivDetails.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutHivDetails.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerHbsTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = binding.spinnerHbsTest.getSelectedItem() + "";
                if (data.equalsIgnoreCase("done")) {
                    binding.layoutHbsDetails.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutHbsDetails.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerBloodTestDone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = binding.spinnerBloodTestDone.getSelectedItem() + "";
                if (data.equalsIgnoreCase("done")) {
                    binding.layoutBloodGroup.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutBloodGroup.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        bindViews();
    }


    private void getFormUtilityData() {
        viewModel.getEcUtilityData(AppDefines.ANC_REGISTRATION_UTILITY_URL).observe(this, contents -> {
            if (contents != null) {
                for (FormUtilityResponse.SurveyFormResponse content : contents) {
                    if (content.getGroupName().equals("Blood Group of Mother")) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerBloodTestDone.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));
                    }
                    if (content.getGroupName().equals("Select Blood Group - Additional field")) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerBloodGroup.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));
                    }

                    if (content.getGroupName().equals("Previous disease Information*")) {
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            if (questions.getTitle() == "Diabetes") {
                                binding.checkboxDiabetes.setText(questions.getTitle());
                            }
                            if (questions.getTitle() == "Heart Disease") {
                                binding.checkboxHeartDisease.setText(questions.getTitle());
                            }
                            if (questions.getTitle() == "Asthma") {
                                binding.checkboxAsthma.setText(questions.getTitle());
                            }
                            if (questions.getTitle() == "Hypertension") {
                                binding.checkboxHypertension.setText(questions.getTitle());
                            }
                            if (questions.getTitle() == "Tuberculosis") {
                                binding.checkboxTuberculosis.setText(questions.getTitle());
                            }
                            if (questions.getTitle() == "Epileptic (Convolsion)") {
                                binding.checkboxEpileptic.setText(questions.getTitle());
                            }
                            if (questions.getTitle() == "Hepatities - B") {
                                binding.checkboxHepatitis.setText(questions.getTitle());
                            }
                            if (questions.getTitle() == "Jaundice") {
                                binding.checkboxJaundice.setText(questions.getTitle());
                            }
                            if (questions.getTitle() == "HIV Positive") {
                                binding.checkboxHiv.setText(questions.getTitle());
                            }
                            if (questions.getTitle() == " Others") {
                                binding.checkboxOthers.setText(questions.getTitle());
                            }
                            if (questions.getTitle() == "None of the above") {
                                binding.checkboxNone.setText(questions.getTitle());
                            }
                        }
                    }
                    if (content.getGroupName().equals("Past Obstetrics History Total No pf pregnancies (Previous)*")) {
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerNoOfPregnancies.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));

                    }
                    if (content.getGroupName().equals("Last Pregnancy Complections")) {
                        complicationList = new String[content.getQuesOptions().size()];
                        for (int i = 0; i < content.getQuesOptions().size(); i++) {
                            complicationList[i] = content.getQuesOptions().get(i).getTitle();
                        }
                        complicationSelectedListOne = getDummyBoolList(complicationList.length);
                        complicationSelectedListTwo = getDummyBoolList(complicationList.length);
                    }

                    if (content.getGroupName().equals("Pregnancy Outcome")) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerOutcomeTwo.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));
                        binding.spinnerOutcomeOne.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));

                    }

                    if (content.getGroupName().equals("VDRL/RPR test")) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerVdrlTest.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));
                    }
                    if (content.getGroupName().equals("VDRL/RPR test result")) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerVdrlTestResult.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));
                    }
                    if (content.getGroupName().equals("HIV Screening test")) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerHivTest.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));
                    }
                    if (content.getGroupName().equals("HIV screening test result")) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerHivTestResult.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));
                    }
                    if (content.getGroupName().equals("HBsAg test")) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerHbsTest.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));
                    }
                    if (content.getGroupName().equals("HBsAg test result")) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerHbsTestResult.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));
                    }
                    if (content.getGroupName().equals("Place Name")) {
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions : content.getQuesOptions()) {
                            data.add(questions.getTitle());
                        }
                        binding.spinnerPlaceName.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, data));
                    }
                }
            }
        });
    }

    private void bindViews() {
        currentDate = getCurrentDate();
        binding.registrationDate.setText(getDateToView(currentDate, RMNCHA_DATE_FORMAT));
        currentYear = getCurrentYear();
        financialYear = getCurrentFinancialYear();
        binding.financialYear.setText(financialYear);
        getAshaWorkerData(binding.progressBarSelectAsha, binding.spinnerSelectAshaName);
        getCoupleData();
    }

    private void getAshaWorkerData(View progressBar, Spinner spinnerView) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            String subCenterUUID = getIntent().getExtras().get(PARAM_SUB_CENTER) + "";
            viewModel.getAshaWorkerLiveData(subCenterUUID).observe(this, contents -> {
                progressBar.setVisibility(View.GONE);
                if (contents != null) {
                    ashaWorkersList = contents;
                    spinnerView.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, getAshaWorkerNamesList(ashaWorkersList)));
                } else {
                    showMyToast(this, "Error fetching Asha workers data  : " + viewModel.getErrorMessage());
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            showMyToast(this, "Error fetching Asha Workers Data");
        }
    }

    private void getCoupleData() {
        try {
            showProgressBar();
            selectedWomen = (RMNCHAANCPWsResponse.Content) getIntent().getExtras().getSerializable(PARAM_ANC_SELECTED_WOMEN);
            viewModel.getRMNCHACoupleDetailsLiveData(selectedWomen.getSource().getProperties().getUuid()).observe(this, contents -> {
                hideProgressBar();
                if (contents != null && contents.size() > 0) {
                    coupleData = contents.get(0);
                    setCoupleView();
                } else {
                    showMyToast(this, "Error fetching Couple Data  : " + viewModel.getErrorMessage());
                }
            });
            binding.hhHeadName.setText(selectedWomen.getSource().getProperties().getHouseHeadName());
        } catch (Exception e) {
            hideProgressBar();
            showMyToast(this, "Error fetching Couple Data");
        }
    }

    private void setCoupleView() {
        if (coupleData != null) {
            RMNCHACoupleDetailsResponse.Content.Source.SourceProperties wifeProperties = coupleData.getSource().getProperties();
            RMNCHACoupleDetailsResponse.Content.Target.TargetProperties husbandProperties = coupleData.getTarget().getProperties();

            rchID = wifeProperties.getRchId();

            wifeID = wifeProperties.getUuid();
            husbandID = husbandProperties.getUuid();

            wifeName = wifeProperties.getFirstName();
            husbandName = husbandProperties.getFirstName();

            wifePhone = wifeProperties.getContact();
            husbandPhone = husbandProperties.getContact();

            List<String> wifeUrlsList = wifeProperties.getImageUrls();
            String wifeURL = "";
            if (wifeUrlsList != null && wifeUrlsList.size() > 0) wifeURL = wifeUrlsList.get(0);
            loadMemberImage(wifeURL, binding.coupleView.wifeImage);

            List<String> husbandUrlsList = husbandProperties.getImageUrls();
            String husbandURL = "";
            if (husbandUrlsList != null && husbandUrlsList.size() > 0)
                husbandURL = husbandUrlsList.get(0);
            loadMemberImage(husbandURL, binding.coupleView.husbandImage);

            binding.coupleView.wifeName.setText(wifeName);
            binding.coupleView.husbandName.setText(husbandName);

            String wifeGender = setNonNullValue(wifeProperties.getGender());
            binding.coupleView.wifeGender.setText(wifeGender);
            binding.coupleView.husbandGender.setText(setNonNullValue(husbandProperties.getGender()));

            String wifeAge = getAgeFromDOB(wifeProperties.getDateOfBirth());
            binding.coupleView.wifeAge.setText(wifeAge);
            binding.coupleView.husbandAge.setText(getAgeFromDOB(husbandProperties.getDateOfBirth()));

            binding.coupleView.wifeStatus.setText(getResidentStatus(wifeProperties.getResidingInVillage()));
            binding.coupleView.husbandStatus.setText(getResidentStatus(husbandProperties.getResidingInVillage()));

            binding.coupleView.wifePhone.setText("Ph : " + setNonNullValue(wifePhone));
            binding.coupleView.husbandPhone.setText("Ph : " + setNonNullValue(husbandPhone));

            binding.coupleView.wifeDob.setText("DOB : " + setNonNullValue(wifeProperties.getDateOfBirth()));
            binding.coupleView.husbandDob.setText("DOB : " + setNonNullValue(husbandProperties.getDateOfBirth()));

            binding.coupleView.wifeHealthId.setText("Health ID : " + setNonNullValue(wifeProperties.getHealthID()));
            binding.coupleView.husbandHealthId.setText("Health ID : " + setNonNullValue(husbandProperties.getHealthID()));

            String wifeContact = wifeProperties.getContact() + "";
            String husbandContact = husbandProperties.getContact() + "";
            if (wifeContact.length() > 9) {
                binding.textviewMobileNumber.setText(wifeContact);
            } else if (husbandContact.length() > 9) {
                binding.textviewMobileNumber.setText(husbandContact);

            }

            binding.textviewWhoseMobileNumber.setText("");
            binding.textviewJsyBeneficiary.setText("");

            binding.textViewRchId.setText(setNonNullValue(wifeProperties.getRchId()));

            binding.textViewHeaderWomenDetails.setText(wifeName + " - " + wifeAge + " - " + wifeGender);

        }
    }

    private void clearIfNoneChecked(boolean isChecked) {
        if (isChecked && binding.checkboxNone.isChecked())
            binding.checkboxNone.setChecked(false);
    }

    private void validateANCRegistration() {

        String ashaName = binding.spinnerSelectAshaName.getSelectedItem() + "";
        if (ashaName.equalsIgnoreCase("select")) {
            showMyToast(this, "Select Valid Asha Worker Name");
            return;
        }

        String serialNumber = binding.editTextSerialNumber.getText() + "";
        if (serialNumber.length() < 1) {
            showMyToast(this, "Enter Valid Serial Number");
            return;
        }

        String jsyBeneficiary = binding.textviewJsyBeneficiary.getText() + "";
        String contact = binding.textviewMobileNumber.getText() + "";
        String whoseMobile = binding.textviewWhoseMobileNumber.getText() + "";

        String address = binding.editTextAddress.getText() + "";
        if (address.length() < 1) {
            showMyToast(this, "Enter Valid Address");
            return;
        }

        String lmp = binding.textViewLmpDate.getText() + "";
        if (lmp.length() < 1) {
            showMyToast(this, "Select LMP Date");
            return;
        }
        lmp = lmp.replaceAll("/", "-");

        String edd = binding.textviewEddDate.getText() + "";
        edd = edd.replaceAll("/", "-");

        String tempWithIn = binding.textViewRegisteredWithIn.getText() + "";
        boolean withInPeriod = false;
        if (tempWithIn.equalsIgnoreCase("Yes"))
            withInPeriod = true;

        boolean isReferredToPHC = binding.checkboxReferPhc.isChecked();

        String w = binding.editTextWeight.getText() + "";
        int weight = 0;
        if (w.length() > 0) {
            weight = Integer.parseInt(w);
        }
        String h = binding.editTextHeight.getText() + "";
        int height = 0;
        if (h.length() > 0) {
            height = Integer.parseInt(h);
        }
        String bloodTest = binding.spinnerBloodTestDone.getSelectedItem() + "";
        String bloodGroup = "";
        if (bloodTest.equalsIgnoreCase("done")) {
            bloodGroup = binding.spinnerBloodGroup.getSelectedItem() + "";
        }

        List<String> medicalHistory = new ArrayList<>();
        if (binding.checkboxDiabetes.isChecked()) {
            medicalHistory.add("Diabetes");
        }
        if (binding.checkboxHeartDisease.isChecked()) {
            medicalHistory.add("HeartDisease");
        }
        if (binding.checkboxAsthma.isChecked()) {
            medicalHistory.add("Asthma");
        }
        if (binding.checkboxHypertension.isChecked()) {
            medicalHistory.add("Hypertension");
        }
        if (binding.checkboxTuberculosis.isChecked()) {
            medicalHistory.add("Tuberculosis");
        }
        if (binding.checkboxEpileptic.isChecked()) {
            medicalHistory.add("Epileptic(Convulsion)");
        }
        if (binding.checkboxHepatitis.isChecked()) {
            medicalHistory.add("Hepatitis-B");
        }
        if (binding.checkboxJaundice.isChecked()) {
            medicalHistory.add("Jaundice");
        }
        if (binding.checkboxHiv.isChecked()) {
            medicalHistory.add("HIV Positive");
        }
        if (binding.checkboxSti.isChecked()) {
            medicalHistory.add("STI/RTI");
        }
        if (binding.checkboxOthers.isChecked()) {
            String other = binding.textviewOthers.getText() + "";
            medicalHistory.add(other);
        }
        if (binding.checkboxNone.isChecked()) {
            medicalHistory.add("None");
        }

        if (medicalHistory.size() < 1) {
            showMyToast(this, "Select Previous disease Information");
            return;
        }

        int totalPregnancy = Integer.parseInt(binding.spinnerNoOfPregnancies.getSelectedItem() + "");
        List<String> complicationsOne = Arrays.asList((binding.textviewComplicationsOne.getText() + "").split(","));
        List<String> complicationsTwo = Arrays.asList((binding.textviewComplicationsTwo.getText() + "").split(","));

        String outcomeOne = binding.spinnerOutcomeOne.getSelectedItem() + "";
        String outcomeTwo = binding.spinnerOutcomeTwo.getSelectedItem() + "";

        String expectedFacility = binding.spinnerFacility.getSelectedItem() + "";
        if (expectedFacility.equalsIgnoreCase("select")) {
            expectedFacility = "";
        }

        String place = binding.spinnerPlaceName.getSelectedItem() + "";
        if (place.equalsIgnoreCase("select")) {
            place = "";
        }

        String vdrlTestTaken = binding.spinnerVdrlTest.getSelectedItem() + "";
        boolean isVdrl = false;
        String vdrlDate = "";
        String vdrlResult = "";
        if (vdrlTestTaken.equalsIgnoreCase("done")) {
            isVdrl = true;
            vdrlDate = binding.textViewVdrlTestDate.getText() + "";
            vdrlDate = vdrlDate.replaceAll("/", "-");
            vdrlResult = binding.spinnerVdrlTestResult.getSelectedItem() + "";
            if (vdrlResult.equalsIgnoreCase("select")) {
                vdrlResult = "";
            }
        }

        String hivTestTaken = binding.spinnerHivTest.getSelectedItem() + "";
        boolean isHiv = false;
        String hivDate = "";
        String hivResult = "";
        if (hivTestTaken.equalsIgnoreCase("done")) {
            isHiv = true;
            hivDate = binding.textViewHivTestDate.getText() + "";
            hivDate = hivDate.replaceAll("/", "-");
            hivResult = binding.spinnerHivTestResult.getSelectedItem() + "";
            if (hivResult.equalsIgnoreCase("select")) {
                hivResult = "";
            }
        }

        String hbsTestTaken = binding.spinnerHbsTest.getSelectedItem() + "";
        boolean isHbs = false;
        String hbsDate = "";
        String hbsResult = "";
        if (hbsTestTaken.equalsIgnoreCase("done")) {
            isHbs = true;
            hbsDate = binding.textViewHbsTestDate.getText() + "";
            hbsDate = hbsDate.replaceAll("/", "-");
            hbsResult = binding.spinnerHbsTestResult.getSelectedItem() + "";
            if (hbsResult.equalsIgnoreCase("select")) {
                hbsResult = "";
            }
        }

        RMNCHAANCRegistrationRequest.Couple couple = new RMNCHAANCRegistrationRequest.Couple(
                husbandID, husbandName, husbandPhone,
                wifeID, wifeName, wifePhone,
                address, serialNumber,
                ashaName, ashaName,
                currentDate,
                rchID);

        RMNCHAANCRegistrationRequest.MensuralPeriod mensuralPeriod = new RMNCHAANCRegistrationRequest.MensuralPeriod(
                lmp, edd, withInPeriod, isReferredToPHC);

        RMNCHAANCRegistrationRequest.PregnantWoman pregnantWoman = new RMNCHAANCRegistrationRequest.PregnantWoman(
                weight, height, bloodGroup);

        List<RMNCHAANCRegistrationRequest.Pregnancy> pregnancies = new ArrayList<>();
        if (totalPregnancy == 1) {
            pregnancies.add(new RMNCHAANCRegistrationRequest.Pregnancy(1, complicationsOne, outcomeOne));
        } else if (totalPregnancy > 1) {
            pregnancies.add(new RMNCHAANCRegistrationRequest.Pregnancy(1, complicationsOne, outcomeOne));
            pregnancies.add(new RMNCHAANCRegistrationRequest.Pregnancy(2, complicationsTwo, outcomeTwo));
        }

        RMNCHAANCRegistrationRequest request = new RMNCHAANCRegistrationRequest(couple, mensuralPeriod,
                pregnantWoman, pregnancies, medicalHistory, totalPregnancy, place, expectedFacility,
                isVdrl, vdrlDate, vdrlResult,
                isHiv, hivDate, hivResult,
                isHbs, hbsDate, hbsResult, "COMPLETED");

        makeANCRegistrationCall(request);

    }

    private void makeANCRegistrationCall(RMNCHAANCRegistrationRequest request) {
        showProgressBar();
        viewModel.makeANCRegistrationRequest(request).observe(this, rmnchaancRegistrationResponse -> {
            hideProgressBar();
            if (rmnchaancRegistrationResponse != null) {
                showMyToast(this, "ANC Registration Successful");
                finish();
            } else {
                showMyToast(this, "ANC Registration Failed : " + viewModel.getErrorMessage());
            }
        });
    }

    @Override
    public void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
    }

}
