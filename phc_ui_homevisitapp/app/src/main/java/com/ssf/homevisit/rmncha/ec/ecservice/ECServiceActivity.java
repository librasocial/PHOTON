package com.ssf.homevisit.rmncha.ec.ecservice;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.RMNCHA_DATE_FORMAT;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getAgeFromDOB;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getAshaWorkerNamesList;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentDate;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentFinancialYear;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentYear;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDateFromDatePicker;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDateToView;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getResidentStatus;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.loadMemberImage;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.setNonNullValue;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;
import static com.ssf.homevisit.rmncha.ec.selectwomen.SelectWomenForECActivity.PARAM_SUB_CENTER;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.databinding.ActivityRmnchaEcServiceBinding;
import com.ssf.homevisit.models.AshaWorkerResponse;
import com.ssf.homevisit.models.FormUtilityResponse;
import com.ssf.homevisit.models.RMNCHACoupleResponse;
import com.ssf.homevisit.models.RMNCHACreateECServiceRequest;
import com.ssf.homevisit.models.RMNCHACreateVisitLogRequest;
import com.ssf.homevisit.models.RMNCHAMembersInHouseHoldResponse;
import com.ssf.homevisit.models.RMNCHAVisitLogsResponse;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.base.RMNCHABaseActivity;
import com.ssf.homevisit.rmncha.ec.selectspouse.SelectSpouseForECActivity;
import java.util.ArrayList;
import java.util.List;

public class ECServiceActivity extends RMNCHABaseActivity {

    private ActivityRmnchaEcServiceBinding binding;
    private ECServiceViewModel viewModel;
    private RMNCHAMembersInHouseHoldResponse.Content womenNode;
    private RMNCHACoupleResponse.Content coupleData;
    private List<RMNCHAVisitLogsResponse.VisitLog> visitLogList;
    private List<AshaWorkerResponse.Content> ashaWorkersList = new ArrayList<>();
    private String womenUUID;
    private String serviceID;
    private String rchID;
    private String wifeID;
    private String husbandID;
    private String currentDate;
    private String currentYear;
    private String financialYear;
    private String subCenterUUID;
    private List<String> maleContraceptiveList = new ArrayList<String>();
    private List<String> femaleContraceptiveList = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getInstance().setECServiceActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rmncha_ec_service);
        viewModel = new ViewModelProvider(this).get(ECServiceViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        initializeViews();
    }
    private void getFormUtilityData() {
        viewModel.getEcUtilityData(AppDefines.EC_SERVICE_UTILITY_URL).observe(this, contents -> {
            if (contents != null) {
                for (FormUtilityResponse.SurveyFormResponse content : contents) {
                    if (content.getGroupName().equals("Choose the service type")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerSelectServiceType.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Whom to provide the service?")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.radioHusband.setText(data.get(0));
                        binding.radioWife.setText(data.get(1));
                    }
                    if (content.getGroupName().equals("Quantity of Condoms provided (in numbers)")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerCondomsQuantity.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }

                    if (content.getGroupName().equals("Male (Husband) Type of contraceptives")){
                        ArrayList<String> data = new ArrayList<>();
                        maleContraceptiveList.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            maleContraceptiveList.add(questions.getTitle());
                        }
                    }
                    if (content.getGroupName().equals("Female (Wife) Type of contraceptives")){
                        femaleContraceptiveList.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            femaleContraceptiveList.add(questions.getTitle());
                        }
                    }
                    if (content.getGroupName().equals("Select the services provided")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.checkboxPcCounselling.setText(data.get(0));
                        binding.checkboxPcReferPhc.setText(data.get(1));
                    }
                    if (content.getGroupName().equals("Pregnancy test results")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerBcPregnancyResult.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Quantity of OCP provided (in strips)")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.spinnerOcpQuantity.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Select OCP type")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        binding.radioChaya.setText(data.get(0));
                        binding.radioMalaD.setText(data.get(1));
                        binding.radioMalaN.setText(data.get(2));
                    }
                }
            }
        });
    }
    private void initializeViews() {
        getFormUtilityData();
        try {
            womenNode = (RMNCHAMembersInHouseHoldResponse.Content) getIntent().getSerializableExtra(SelectSpouseForECActivity.SELECTED_WOMEN_KEY);
            womenUUID = womenNode.getTargetNode().getProperties().getUuid();
            rchID = womenNode.getTargetNode().getProperties().getRchId();
            subCenterUUID = getIntent().getExtras().getString(PARAM_SUB_CENTER) + "";
            binding.hhHeadName.setText(setNonNullValue(womenNode.getSourceNode().getProperties().getHouseHeadName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        getCoupleData();

        getAshaWorkerData();

        binding.textViewRchId.setText(setNonNullValue(rchID));
        binding.spinnerSelectServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    setBCViews();
                } else if (position == 1) {
                    setPCViews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        currentDate = getCurrentDate();
        binding.visitDate.setText(getDateToView(currentDate, RMNCHA_DATE_FORMAT));
        currentYear = getCurrentYear();
        financialYear = getCurrentFinancialYear();
        binding.financialYear.setText(financialYear);
        binding.buttonCancel.setOnClickListener(view -> showExitWithoutSavingAlert());
        binding.buttonSave.setOnClickListener(view -> {
            int ashaSelection = binding.spinnerSelectAshaName.getSelectedItemPosition();
            if (ashaSelection < 1) {
                showMyToast(this, "Select Valid Asha Worker Name");
            } else {
                int serviceType = binding.spinnerSelectServiceType.getSelectedItemPosition();
                if (serviceType == 0) {
                    continueBCService();
                } else if (serviceType == 1) {
                    continuePCService();
                }
            }
        });

    }

    private void getAshaWorkerData() {
        viewModel.getAshaWorkerLiveData(subCenterUUID).observe(this, contents -> {
            if (contents != null) {
                ashaWorkersList = contents;
                setAshaWorkersAdapter();
            } else {
                showMyToast(this, "Error fetching Asha workers data  : " + viewModel.getErrorMessage());
            }
        });
    }

    private void getCoupleData() {
        viewModel.getCoupleDetailsLiveData(womenUUID).observe(this, contents -> {
            if (contents != null) {
                coupleData = contents;
                setCoupleView();
                getServiceVisitLogs();
            } else {
                showMyToast(this, "Error fetching Couple Data  : " + viewModel.getErrorMessage());
            }
        });
    }

    private void getServiceVisitLogs() {

        try {
            serviceID = coupleData.getRelationship().getProperties().getServiceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (serviceID != null && serviceID.length() > 0) {
            binding.radioBcPregnantTestTakenNo.setEnabled(true);
            binding.radioBcPregnantTestTakenYes.setEnabled(true);
            binding.spinnerBcPregnancyResult.setEnabled(true);
            viewModel.getBCVisitLogsLiveData(serviceID).observe(this, visitLogs -> {
                if (visitLogs != null) {
                    visitLogList = visitLogs;
                    setVisitLogsAdapter();
                } else {
                    showMyToast(this, "Visit logs error : " + viewModel.getErrorMessage());
                }
            });
        } else {
            binding.radioBcPregnantTestTakenNo.setEnabled(false);
            binding.radioBcPregnantTestTakenYes.setEnabled(false);
            binding.spinnerBcPregnancyResult.setEnabled(false);
        }
    }

    void setBCViews() {
        binding.birthControlServiceLayout.setVisibility(View.VISIBLE);
        binding.birthControlStatusLayout.setVisibility(View.VISIBLE);
        binding.preContraceptiveServiceLayout.setVisibility(View.GONE);
        binding.preContraceptiveStatusLayout.setVisibility(View.GONE);
        binding.layoutBcHistory.setVisibility(View.VISIBLE);
        binding.radioWife.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) setFemaleContraceptiveAdapter();
        });
        binding.radioHusband.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) setMaleContraceptiveAdapter();
        });
        binding.radioWife.setChecked(true);
        binding.spinnerSelectContraceptiveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setContraceptiveTypeViews(binding.spinnerSelectContraceptiveType.getSelectedItem() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setVisitLogsAdapter();
    }

    private void setContraceptiveTypeViews(String selection) {
        binding.layoutOcpTypeAndQuantity.setVisibility(View.GONE);
        binding.layoutCondomsQuantity.setVisibility(View.GONE);
        binding.editTextOtherContraceptiveType.setVisibility(View.GONE);
        if (selection.toLowerCase().contains("specify")) {
            binding.editTextOtherContraceptiveType.setText("");
            binding.editTextOtherContraceptiveType.setVisibility(View.VISIBLE);
            binding.mainScrollview.pageScroll(View.FOCUS_DOWN);
        } else if (selection.toLowerCase().contains("condom")) {
            binding.spinnerCondomsQuantity.setSelection(0);
            binding.layoutCondomsQuantity.setVisibility(View.VISIBLE);
            binding.mainScrollview.pageScroll(View.FOCUS_DOWN);
        } else if (selection.toLowerCase().contains("oc")) {
            binding.spinnerOcpQuantity.setSelection(0);
            binding.layoutOcpTypeAndQuantity.setVisibility(View.VISIBLE);
            binding.mainScrollview.pageScroll(View.FOCUS_DOWN);
        }
    }

    private void setFemaleContraceptiveAdapter() {
        binding.spinnerSelectContraceptiveType.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,femaleContraceptiveList));
    }

    private void setMaleContraceptiveAdapter() {
        binding.spinnerSelectContraceptiveType.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,maleContraceptiveList));
    }

    void setPCViews() {
        binding.birthControlServiceLayout.setVisibility(View.GONE);
        binding.birthControlStatusLayout.setVisibility(View.GONE);
        binding.preContraceptiveServiceLayout.setVisibility(View.VISIBLE);
        binding.preContraceptiveStatusLayout.setVisibility(View.VISIBLE);
        binding.layoutBcHistory.setVisibility(View.GONE);
        binding.textViewContraceptiveStopDate.setOnClickListener(view -> getDateFromDatePicker((TextView) view));

    }

    void setCoupleView() {
        if (coupleData != null) {
            RMNCHACoupleResponse.Content.MemberNode.Properties wifeProperties = coupleData.getSourceNode().getProperties();
            RMNCHACoupleResponse.Content.MemberNode.Properties husbandProperties = coupleData.getTargetNode().getProperties();

            wifeID = wifeProperties.getUuid();
            husbandID = husbandProperties.getUuid();

            List<String> wifeUrlsList = wifeProperties.getImageUrls();
            String wifeURL = "";
            if (wifeUrlsList != null && wifeUrlsList.size() > 0) wifeURL = wifeUrlsList.get(0);
            loadMemberImage(wifeURL, binding.coupleView.wifeImage);

            List<String> husbandUrlsList = husbandProperties.getImageUrls();
            String husbandURL = "";
            if (husbandUrlsList != null && husbandUrlsList.size() > 0)
                husbandURL = husbandUrlsList.get(0);
            loadMemberImage(husbandURL, binding.coupleView.husbandImage);

            String wifeName = setNonNullValue(wifeProperties.getFirstName());
            binding.coupleView.wifeName.setText(wifeName);
            binding.coupleView.husbandName.setText(setNonNullValue(husbandProperties.getFirstName()));

            String wifeGender = setNonNullValue(wifeProperties.getGender());
            binding.coupleView.wifeGender.setText(wifeGender);
            binding.coupleView.husbandGender.setText(setNonNullValue(husbandProperties.getGender()));

            String wifeAge = getAgeFromDOB(wifeProperties.getDateOfBirth());
            binding.coupleView.wifeAge.setText(wifeAge);
            binding.coupleView.husbandAge.setText(getAgeFromDOB(husbandProperties.getDateOfBirth()));

            binding.coupleView.wifeStatus.setText(getResidentStatus(wifeProperties.getResidingInVillage()));
            binding.coupleView.husbandStatus.setText(getResidentStatus(husbandProperties.getResidingInVillage()));

            binding.coupleView.wifePhone.setText("Ph : " + setNonNullValue(wifeProperties.getContact()));
            binding.coupleView.husbandPhone.setText("Ph : " + setNonNullValue(husbandProperties.getContact()));

            binding.coupleView.wifeDob.setText("DOB : " + setNonNullValue(wifeProperties.getDateOfBirth()));
            binding.coupleView.husbandDob.setText("DOB : " + setNonNullValue(husbandProperties.getDateOfBirth()));

            binding.coupleView.wifeHealthId.setText("Health ID : " + setNonNullValue(wifeProperties.getHealthID()));
            binding.coupleView.husbandHealthId.setText("Health ID : " + setNonNullValue(husbandProperties.getHealthID()));

            binding.textViewHeaderWomenName.setText(wifeName + " - " + wifeAge + " - " + wifeGender);

        }
    }

    void setVisitLogsAdapter() {
        binding.recyclerViewBcServiceStatus.setAdapter(new BCServiceStatusAdapter(this, visitLogList));
        binding.recyclerViewBcServiceStatus.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewBcHistory.setAdapter(new BCServiceVisitHistoryAdapter(this, visitLogList));
        binding.recyclerViewBcHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    void setAshaWorkersAdapter() {
        binding.spinnerSelectAshaName.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, getAshaWorkerNamesList(ashaWorkersList)));
    }

    private void continueBCService() {

        String ashaName = binding.spinnerSelectAshaName.getSelectedItem() + "";

        String rchID = womenNode.getTargetNode().getProperties().getRchId();

        boolean radioHusband = binding.radioHusband.isChecked();
        boolean radioWife = binding.radioWife.isChecked();
        if (!(radioWife || radioHusband)) {
            showMyToast(this, "Select a value for Whom to provide the service?");
            return;
        }

        String serviceOfferTo = "wife";
        if (radioHusband) serviceOfferTo = "husband";

        String selection = binding.spinnerSelectContraceptiveType.getSelectedItem() + "";
        String contraceptiveType = selection;
        String contraceptiveQuantity = "";
        if (selection.toLowerCase().contains("select")) {
            contraceptiveType = "";
        } else if (selection.toLowerCase().contains("specify")) {
            contraceptiveType = binding.editTextOtherContraceptiveType.getText() + "";
        } else if (selection.toLowerCase().contains("condom")) {
            contraceptiveQuantity = binding.spinnerCondomsQuantity.getSelectedItem() + "";
        } else if (selection.toLowerCase().contains("oc")) {
            contraceptiveQuantity = binding.spinnerOcpQuantity.getSelectedItem() + "";
        }

        boolean isReferredToPHC = binding.checkboxBcReferPhc.isChecked();

        boolean pregnantTestTaken = false;
        if (binding.radioBcPregnantTestTakenYes.isChecked())
            pregnantTestTaken = true;
        if (binding.radioBcPregnantTestTakenNo.isChecked())
            pregnantTestTaken = false;

        String pregnantTestResult = "NEGATIVE";
        if (binding.spinnerBcPregnancyResult.getSelectedItemPosition() == 1)
            pregnantTestResult = "POSITIVE";

        RMNCHACreateECServiceRequest bcRequest = new RMNCHACreateECServiceRequest(
                ashaName, rchID, "BC",
                currentDate, currentYear,
                isReferredToPHC, serviceOfferTo,
                contraceptiveType, contraceptiveQuantity, pregnantTestResult,
                wifeID, husbandID);


        RMNCHACreateVisitLogRequest visitLogRequest = new RMNCHACreateVisitLogRequest(
                rchID, null, "BC", currentDate,
                isReferredToPHC, contraceptiveType,
                contraceptiveQuantity, pregnantTestTaken, pregnantTestResult, null);

        if (serviceID != null && serviceID.length() > 0) {
            visitLogRequest.setServiceId(serviceID);
            makeECVisitLogServiceCall(serviceID, visitLogRequest);
        } else {
            makeECServiceCall(true, bcRequest, visitLogRequest);
        }

    }

    private void continuePCService() {

        String ashaName = binding.spinnerSelectAshaName.getSelectedItem() + "";

        String rchID = womenNode.getTargetNode().getProperties().getRchId();

        ArrayList<String> pcServiceList = new ArrayList<>();
        if (binding.checkboxPcCounselling.isChecked())
            pcServiceList.add(binding.checkboxPcCounselling.getText() + "");
        if (binding.checkboxPcReferPhc.isChecked())
            pcServiceList.add(binding.checkboxPcReferPhc.getText() + "");

        boolean stopDate = false;
        if (!(binding.textViewContraceptiveStopDate.getText() + "").isEmpty()) {
            stopDate = true;
        }

        boolean pregnantTestTaken = false;
        if (binding.radioPregnantTestTakenYes.isChecked())
            pregnantTestTaken = true;
        if (binding.radioPregnantTestTakenNo.isChecked())
            pregnantTestTaken = false;

        String pregnantTestResult = "NEGATIVE";
        if (binding.spinnerPregnancyResult.getSelectedItemPosition() == 1)
            pregnantTestResult = "POSITIVE";

        RMNCHACreateECServiceRequest pcRequest = new RMNCHACreateECServiceRequest(
                ashaName, rchID, "PC",
                currentDate, currentYear,
                pcServiceList.toArray(new String[0]), stopDate,
                pregnantTestTaken, pregnantTestResult,
                wifeID, husbandID);

        RMNCHACreateVisitLogRequest visitLogRequest = new RMNCHACreateVisitLogRequest(
                rchID, null, "PC", currentDate,
                false, null,
                null, pregnantTestTaken, pregnantTestResult, null);

        if (serviceID != null && serviceID.length() > 0) {
            visitLogRequest.setServiceId(serviceID);
            makeECVisitLogServiceCall(serviceID, visitLogRequest);
        } else {
            makeECServiceCall(true, pcRequest, visitLogRequest);
        }

    }

    private void makeECServiceCall(boolean makeVisitCall, RMNCHACreateECServiceRequest request, RMNCHACreateVisitLogRequest visitLogRequest) {
        showProgressBar();
        viewModel.makeECServiceRequest(request).observe(this, rmnchaCreateECServiceResponse -> {
            hideProgressBar();
            if (rmnchaCreateECServiceResponse != null && rmnchaCreateECServiceResponse.getId() != null
                    && !rmnchaCreateECServiceResponse.getId().isEmpty()) {
                String serviceID = rmnchaCreateECServiceResponse.getId();
                if (makeVisitCall) {
                    visitLogRequest.setServiceId(serviceID);
                    makeECVisitLogServiceCall(serviceID, visitLogRequest);
                }
            } else {
                showMyToast(this, "EC Service Failed : " + viewModel.getErrorMessage());
            }

        });
    }

    private void makeECVisitLogServiceCall(String serviceID, RMNCHACreateVisitLogRequest request) {
        showProgressBar();
        viewModel.makeCreateVisitLogRequest(serviceID, request).observe(this, rMNCHACreateVisitLogResponse -> {
            hideProgressBar();
            if (rMNCHACreateVisitLogResponse != null) {
                if (rMNCHACreateVisitLogResponse.getId() != null
                        && !rMNCHACreateVisitLogResponse.getId().isEmpty()) {
                    showSaveSuccessAlert();
                } else {
                    showMyToast(this, "EC Visit Log Failed");
                }
            } else {
                showMyToast(this, "EC Visit Log Failed : " + viewModel.getErrorMessage());
            }

        });
    }

    @Override
    protected void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void hideProgressBar() {
        binding.progressBar.setVisibility(View.GONE);
    }
}
