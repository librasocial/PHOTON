package com.ssf.homevisit.rmncha.anc.pwtracking;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.RMNCHA_DATE_FORMAT;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getAgeFromDOB;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getAshaWorkerNamesList;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentDate;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentFinancialYear;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentYear;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDateFromDatePicker;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDateToView;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDummyBoolList;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getResidentStatus;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.loadMemberImage;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.setNonNullValue;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showSelectComplicationsAlertDialogue;
import static com.ssf.homevisit.rmncha.anc.selectwomen.SelectWomenForANCActivity.PARAM_ANC_SELECTED_WOMEN;
import static com.ssf.homevisit.rmncha.anc.selectwomen.SelectWomenForANCActivity.PARAM_SUB_CENTER;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ssf.homevisit.R;
import com.ssf.homevisit.databinding.ActivityRmnchaAncPwTrackingBinding;
import com.ssf.homevisit.models.AshaWorkerResponse;
import com.ssf.homevisit.models.FormUtilityResponse;
import com.ssf.homevisit.models.RMNCHACoupleDetailsResponse;
import com.ssf.homevisit.models.RMNCHAANCPWsResponse;
import com.ssf.homevisit.models.RMNCHAANCRegistrationResponse;
import com.ssf.homevisit.models.RMNCHAANCServiceRequest;
import com.ssf.homevisit.models.RMNCHAANCVisitLogRequest;
import com.ssf.homevisit.models.RMNCHAANCVisitLogResponse;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.base.RMNCHABaseActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ANCPWTrackingActivity extends RMNCHABaseActivity {

    private String[] complicationList;
    private String[] riskList;
    private boolean[] complicationSelectedList;
    private boolean[] riskSelectedList;
    private ANCPWTrackingViewModel viewModel;
    private ActivityRmnchaAncPwTrackingBinding binding;
    private List<AshaWorkerResponse.Content> ashaWorkersList = new ArrayList<>();
    private List<RMNCHAANCVisitLogResponse> historyList = new ArrayList<>();
    private RMNCHAANCRegistrationResponse ancRegistrationData;
    private RMNCHAANCPWsResponse.Content selectedWomen;
    private RMNCHACoupleDetailsResponse.Content coupleData;
    private String ancServiceId;
    private String ancRegistrationId;
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
    private int weeksOfPregnancy = 0;
    private String lmpDateString;
    private String eddDateString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rmncha_anc_pw_tracking);
        viewModel = new ViewModelProvider(this).get(ANCPWTrackingViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        initializeViews();
    }

    private void initializeViews() {
        getFormUtilityData();
        complicationList = getResources().getStringArray(R.array.array_possible_death);
        complicationSelectedList = getDummyBoolList(complicationList.length);
        binding.textviewCauseOfDeath.setOnClickListener(view -> showSelectComplicationsAlertDialogue(this, complicationList, complicationSelectedList, (TextView) view, null));
        binding.textViewTtDate.setOnClickListener(view -> getDateFromDatePicker((TextView) view));
        binding.textViewDeliveryDate.setOnClickListener(view -> getDateFromDatePicker((TextView) view));
        binding.textViewAbortionDate.setOnClickListener(view -> getDateFromDatePicker((TextView) view));
        binding.textViewMotherDeathDate.setOnClickListener(view -> getDateFromDatePicker((TextView) view));
        binding.buttonCancel.setOnClickListener(view -> showExitWithoutSavingAlert());
        binding.buttonSubmit.setOnClickListener(view -> validateANCService());
        binding.radioGroupAbortion.setOnCheckedChangeListener((radioGroup, position) -> {
            if (position == binding.radioAbortionNo.getId()) {
                binding.layoutAbortionDetails.setVisibility(View.GONE);
            } else {
                binding.layoutAbortionDetails.setVisibility(View.VISIBLE);

            }
        });
        binding.radioGroupMaternalDeath.setOnCheckedChangeListener((radioGroup, position) -> {
            if (position == binding.radioMaternalDeathNo.getId()) {
                binding.layoutMotherDeathDetails.setVisibility(View.GONE);
            } else {
                binding.layoutMotherDeathDetails.setVisibility(View.VISIBLE);

            }
        });
        binding.spinnerUrineTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = binding.spinnerUrineTest.getSelectedItem() + "";
                if (data.equalsIgnoreCase("done")) {
                    binding.layoutUrineTestDetails.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutUrineTestDetails.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerCovidTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = binding.spinnerCovidTest.getSelectedItem() + "";
                if (data.equalsIgnoreCase("done")) {
                    binding.layoutCovidResult.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutCovidResult.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerBloodSugarTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = binding.spinnerBloodSugarTest.getSelectedItem() + "";
                if (data.equalsIgnoreCase("done")) {
                    binding.layoutSugarDetailsLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutSugarDetailsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.radioPwDeliveredYes.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                binding.layoutDeliveryDate.setVisibility(View.VISIBLE);
            } else {
                binding.layoutDeliveryDate.setVisibility(View.GONE);
            }
        });

        bindViews();
    }

    private void bindViews() {
        currentDate = getCurrentDate();
        binding.visitDate.setText(getDateToView(currentDate, RMNCHA_DATE_FORMAT));
        currentYear = getCurrentYear();
        financialYear = getCurrentFinancialYear();
        binding.financialYear.setText(financialYear);
        getCoupleData();
        setAshaWorkerData(binding.progressBarSelectAsha, binding.spinnerSelectAshaName);
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
                    showMyToast(this, "Error fetching Couple Data");
                }
            });
            binding.hhHeadName.setText(selectedWomen.getSource().getProperties().getHouseHeadName());
        } catch (Exception e) {
            hideProgressBar();
            showMyToast(this, "Error fetching Couple Data");
        }
    }

    private void getFormUtilityData() {
        viewModel.getEcUtilityData(AppDefines.ANC_SERVICE_UTILITY_URL).observe(this, contents -> {
            if (contents != null) {
                for (FormUtilityResponse.SurveyFormResponse content : contents) {
                    if (content.getGroupName().equals("Facility / Place where ANC was done*")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerFacility.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Place Name")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerPlaceName.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Urine Test")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerUrineTest.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Urine Sugar")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerUrineSugar.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));

                    }
                    if (content.getGroupName().equals("Any Other Symptoms of High Risk")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        riskList= data.toArray(new String[0]);
                        riskSelectedList = getDummyBoolList(riskList.length);
                        binding.textviewRiskSymptoms.setOnClickListener(view -> showSelectComplicationsAlertDialogue(this, riskList, riskSelectedList, (TextView) view, null));
                    }
                    if (content.getGroupName().equals("Blood Sugar Test")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerBloodSugarTest.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));

                    }
                    if (content.getGroupName().equals("TT Dose")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerTtTest.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Referral Facility")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerReferral.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Is Covid Test Done?")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerCovidTest.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Covid Test Result")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerCovidResult.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Fetal Presentation")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.foetalPosition.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Foetal Movement")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.foetalMovement.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Is Pregnant Woman Experiencing ILI (Influenza Like Illness)*")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerHavingIli.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Did Pregnant Woman have any contact with Covid-19 Positive Patients in the Last 14 days? *")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerCovidContact.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }

                }
            }
        });
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

            binding.textViewHeaderWomenDetails.setText(wifeName + " - " + wifeAge + " - " + wifeGender);

            binding.textViewRchId.setText(setNonNullValue(wifeProperties.getRchId()));
            getANCServiceHistory();
            getANCRegistrationDetails();
        }
    }

    private void getANCServiceHistory() {
        try {
            ancServiceId = coupleData.getRelationship().getProperties().getAncServiceId();
            if (ancServiceId != null && ancServiceId.length() > 0) {
                showProgressBar();
                viewModel.getANCServiceHistoryLiveData(ancServiceId).observe(this, contents -> {
                    hideProgressBar();
                    if (contents != null) {
                        historyList = contents;
                        setANCServiceHistoryAdapter();
                    } else {
                        showMyToast(this, "Error fetching Visit Logs  : " + viewModel.getErrorMessage());
                    }
                });
            }
        } catch (Exception e) {
            hideProgressBar();
            showMyToast(this, "Error fetching Service History");
        }
    }

    private void getANCRegistrationDetails() {
        try {
            ancRegistrationId = coupleData.getRelationship().getProperties().getAncRegistrationId();
            if (ancRegistrationId != null && ancRegistrationId.length() > 0) {
                showProgressBar();
                viewModel.getANCRegistrationData(ancRegistrationId).observe(this, contents -> {
                    hideProgressBar();
                    if (contents != null) {
                        ancRegistrationData = contents;
                        setRegistryDetails();
                    } else {
                        showMyToast(this, "Error : " + viewModel.getErrorMessage());
                    }
                });
            }
        } catch (Exception e) {
            hideProgressBar();
            showMyToast(this, "Error fetching ANC Registry details");
        }
    }

    private void setRegistryDetails() {
        try {
            if (ancRegistrationData != null) {
                lmpDateString = ancRegistrationData.getMensuralPeriod().getLmpDate() + "";
                eddDateString = ancRegistrationData.getMensuralPeriod().getEddDate() + "";
                binding.textviewLmpDate.setText(lmpDateString);
                binding.textviewEddDate.setText(eddDateString);

                String[] lmpDateArray = lmpDateString.split("-");
                if (lmpDateArray.length == 3) {
                    int lmpYear = Integer.parseInt(lmpDateArray[0]);
                    int lmpMonth = Integer.parseInt(lmpDateArray[1]);
                    int lmpDay = Integer.parseInt(lmpDateArray[2]);

                    Calendar currentDate = Calendar.getInstance();
                    Calendar lmpDate = Calendar.getInstance();
                    lmpDate.set(lmpYear, lmpMonth, lmpDay);
                }
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date parsedDate1 = dateFormat.parse(lmpDateString);
                Date parsedDate2 = new Date();
                Long diff = parsedDate2.getTime() - parsedDate1.getTime();
                Long millisecondsPerDay = (long) (1000 * 60 * 60 * 24);
                Long Days = diff / millisecondsPerDay;
                weeksOfPregnancy = (int) (Days/7);
                binding.weeksOfPregnancy.setText(weeksOfPregnancy + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setANCServiceHistoryAdapter() {
        binding.recyclerviewAncServiceHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewAncServiceHistory.setAdapter(new ANCPWServiceAdapter(this, historyList));
    }

    private void setAshaWorkerData(View progressBar, Spinner spinnerView) {
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

    private void validateANCService() {
        String ashaName = binding.spinnerSelectAshaName.getSelectedItem() + "";
        if (ashaName.equalsIgnoreCase("select")) {
            showMyToast(this, "Select Valid Asha Worker Name");
            return;
        }

        String facility = binding.spinnerFacility.getSelectedItem() + "";
        if (facility.equalsIgnoreCase("select")) {
            showMyToast(this, "Select valid Facility");
            return;
        }

        String place = binding.spinnerPlaceName.getSelectedItem() + "";

        String weight = binding.weightOfPw.getText() + "";
        if (weight.length() < 1) {
            showMyToast(this, "Enter Weight of PW");
            return;
        }

        int bpSystolic = 0;
        String bps = binding.bpSystolic.getText() + "";
        if (bps.length() > 0) {
            bpSystolic = Integer.parseInt(bps);
        }

        int bpDiastolic = 0;
        String bpd = binding.bpDiastolic.getText() + "";
        if (bpd.length() > 0) {
            bpDiastolic = Integer.parseInt(bpd);
        }
        String arm = binding.armCircumference.getText() + "";
        if (arm.length() < 1) {
            showMyToast(this, "Enter Mid-arm Circumference details");
            return;
        }

        int hb = 0;
        if ((binding.hb.getText() + "").length() > 0) {
            hb = Integer.parseInt(binding.hb.getText() + "");
        }

        boolean urineTested = false;
        if ("done".equalsIgnoreCase(binding.spinnerUrineTest.getSelectedItem() + "")) {
            urineTested = true;
        }

        boolean urineAlbumin = false;
        if ("present".equalsIgnoreCase(binding.spinnerUrineAlbumin.getSelectedItem() + "")) {
            urineAlbumin = true;
        }

        Boolean urineSugar = false;
        if ("present".equalsIgnoreCase(binding.spinnerUrineSugar.getSelectedItem() + "")) {
            urineSugar = true;
        }

        boolean bloodSugarTested = false;
        if ("done".equalsIgnoreCase(binding.spinnerBloodSugarTest.getSelectedItem() + "")) {
            bloodSugarTested = true;
        }

        String  foetalPosition;
        foetalPosition= (String) binding.foetalPosition.getSelectedItem();

        String foetalMovement;
        foetalMovement= (String) binding.foetalMovement.getSelectedItem();

        int fasting = 0;
        String f = binding.bloodSugarFasting.getText() + "";
        if (f.length() > 0) {
            fasting = Integer.parseInt(f);
        }

        int postPrandal = 0;
        String p = binding.bloodSugarPostPrandal.getText() + "";
        if (p.length() > 0) {
            postPrandal = Integer.parseInt(p);
        }

        int random = 0;
        String r = binding.bloodSugarRandom.getText() + "";
        if (r.length() > 0) {
            random = Integer.parseInt(r);
        }

        int tsh = 0;
        String t = binding.tsh.getText() + "";
        if (t.length() > 0) {
            tsh = Integer.parseInt(t);
        }

        int gtt = 0;
        String g = binding.gtt.getText() + "";
        if (g.length() > 0) {
            gtt = Integer.parseInt(g);
        }

        int ogtt = 0;
        String o = binding.ogtt.getText() + "";
        if (o.length() > 0) {
            ogtt = Integer.parseInt(o);
        }

        String ttDose = "";
        String ttd = binding.spinnerTtTest.getSelectedItem() + "";
        if (!"select".equalsIgnoreCase(ttd)) {
            ttDose = ttd;
        }

        String ttDate = binding.textViewTtDate.getText() + "";
        ttDate = ttDate.replaceAll("/", "-");

        int folicAcid = 0;
        String fa = binding.noOfFolicAcid.getText() + "";
        if (fa.length() > 0) {
            folicAcid = Integer.parseInt(fa);
        }

        int ironFolicAcid = 0;
        String ifa = binding.noOfIronFolicAcid.getText() + "";
        if (ifa.length() > 0) {
            ironFolicAcid = Integer.parseInt(ifa);
        }
        int fundalHeight = 0;
        String fh = binding.fundalHeight.getText() + "";
        if (fh.length() > 0) {
            fundalHeight = Integer.parseInt(fh);
        }

        int foetalHeartRate = 0;
        String fhr = binding.foetalHeartRate.getText() + "";
        if (fhr.length() > 0) {
            foetalHeartRate = Integer.parseInt(fhr);
        }

        List<String> anyOtherSymptoms = new ArrayList<>();
        if (riskSelectedList.length > 0) {
            anyOtherSymptoms = Arrays.asList((binding.textviewRiskSymptoms.getText() + "").split(","));
        }
        String referralFacility = binding.spinnerReferral.getSelectedItem() + "";

        boolean isPWDelivered = binding.radioPwDeliveredYes.isChecked();

        String deliverDate = binding.textViewDeliveryDate.getText() + "";
        deliverDate = deliverDate.replaceAll("/", "-");

        boolean covidTested = false;
        if ("done".equalsIgnoreCase(binding.spinnerCovidTest.getSelectedItem() + "")) {
            covidTested = true;
        }

        boolean isCovidResultPositive = false;
        if ("positive".equalsIgnoreCase(binding.spinnerCovidResult.getSelectedItem() + "")) {
            isCovidResultPositive = true;
        }

        boolean isILIExperienced = false;
        String ili = binding.spinnerHavingIli.getSelectedItem() + "";
        if ("select".equalsIgnoreCase(ili)) {
            showMyToast(this, "Select ILI details of PW");
            return;
        } else if ("yes".equalsIgnoreCase(ili)) {
            isILIExperienced = true;
        }

        boolean didContactCovidPatient = false;
        String cc = binding.spinnerCovidContact.getSelectedItem() + "";
        if (cc.equalsIgnoreCase("select")) {
            showMyToast(this, "Select Did PW any contact with Covid-19 positive patients in the last 14 days details");
            return;
        } else if (cc.equalsIgnoreCase("yes")) {
            didContactCovidPatient = true;
        }

        boolean hasAborted = binding.radioAbortionYes.isChecked();
        boolean maternalDeath = binding.radioAbortionYes.isChecked();
        RMNCHAANCVisitLogRequest visitLogRequest = new RMNCHAANCVisitLogRequest();
        visitLogRequest.setRchId(rchID);
        visitLogRequest.setServiceId(ancServiceId);
        visitLogRequest.setVisitDate(currentDate);
        visitLogRequest.setFinancialYear(financialYear);
        visitLogRequest.setAshaWorker(ashaName);
        visitLogRequest.setHasDeliveredChild(isPWDelivered);
        visitLogRequest.setWeeksOfPregnancy(weeksOfPregnancy);
        visitLogRequest.setAncFacilityType(facility);
        visitLogRequest.setAncFacilityName(place);
        visitLogRequest.setWeight(Integer.parseInt(weight));
        visitLogRequest.setDeliverDate(deliverDate);
        visitLogRequest.setMidArmCircumference(Integer.parseInt(arm));
        visitLogRequest.setBpSystolic(bpSystolic);
        visitLogRequest.setBpDiastolic(bpDiastolic);
        visitLogRequest.setHb(hb);
        visitLogRequest.setUrineTestDone(urineTested);
        visitLogRequest.setUrineAlbuminPresent(urineAlbumin);
        visitLogRequest.setUrineSugarPresent(urineSugar);
        visitLogRequest.setBloodSugarTestDone(bloodSugarTested);
        visitLogRequest.setFastingSugar(fasting);
        visitLogRequest.setPostPrandialSugar(postPrandal);
        visitLogRequest.setRandomSugar(random);
        visitLogRequest.setTshLevel(tsh);
        visitLogRequest.setGtt(gtt);
        visitLogRequest.setOgtt(ogtt);
        visitLogRequest.setTtDose(ttDose);
        visitLogRequest.setTtDoseTakenDate(ttDate);
        visitLogRequest.setNoFATabletsGiven(folicAcid);
        visitLogRequest.setNoIFATabletsGiven(ironFolicAcid);
        visitLogRequest.setFundalHeightByUterusSizeRatio(fundalHeight);
        visitLogRequest.setFoetalHeartRatePerMin(foetalHeartRate);
        visitLogRequest.setFoetalPosition(foetalPosition);
        visitLogRequest.setFoetalMovements(foetalMovement);
        visitLogRequest.setHighRiskSymptoms(anyOtherSymptoms);
        visitLogRequest.setReferralFacility(referralFacility);
        visitLogRequest.setCovidTestDone(covidTested);
        visitLogRequest.setCovidResultPositive(isCovidResultPositive);
        visitLogRequest.setILIExperienced(isILIExperienced);
        visitLogRequest.setDidContactCovidPatient(didContactCovidPatient);
        visitLogRequest.setHasAborted(hasAborted);
        visitLogRequest.setHasMaternalDealth(maternalDeath);

        RMNCHAANCServiceRequest serviceRequest = new RMNCHAANCServiceRequest(rchID, ashaName,
                lmpDateString, eddDateString,
                facility, place,
                wifeID, husbandID,deliverDate);

        if (ancServiceId != null && ancServiceId.length() > 2) {
            makeANCVisitLogCall(ancServiceId, visitLogRequest);
        } else {
            makeANCServiceCall(serviceRequest, visitLogRequest);
        }

    }

    private void makeANCVisitLogCall(String ancServiceId, RMNCHAANCVisitLogRequest visitLogRequest) {
        showProgressBar();
        viewModel.makeANCVisitLogRequest(visitLogRequest, ancServiceId).observe(this, response -> {
            hideProgressBar();
            if (response != null) {
                showSaveSuccessAlert();
            } else {
                showMyToast(this, "Visit Log failed  : " + viewModel.getErrorMessage());
            }
        });
    }

    private void makeANCServiceCall(RMNCHAANCServiceRequest serviceRequest, RMNCHAANCVisitLogRequest visitLogRequest) {
        showProgressBar();
        viewModel.makeANCServiceRequest(serviceRequest).observe(this, response -> {
            hideProgressBar();
            if (response != null) {
                String id = response.getId();
                if (id != null && id.length() > 2) {
                    visitLogRequest.setServiceId(id);
                    makeANCVisitLogCall(id, visitLogRequest);
                    showMyToast(this, "ANC Service Success");
                } else {
                    showMyToast(this, "ANC Service failed");
                }
            } else {
                showMyToast(this, "ANC Service failed : " + viewModel.getErrorMessage());
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

    @Override
    public void onBackPressed() {
        showExitWithoutSavingAlert();
    }
}
