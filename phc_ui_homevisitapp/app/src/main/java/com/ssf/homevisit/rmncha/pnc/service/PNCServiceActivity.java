package com.ssf.homevisit.rmncha.pnc.service;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.RMNCHA_DATE_FORMAT;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getAgeFromDOB;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getAshaWorkerNamesList;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentDate;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDateToView;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDummyBoolList;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getTimeToView;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.setNonNullValue;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;
import static com.ssf.homevisit.rmncha.pnc.selectwomen.PNCWomenAdapter.setPNCWomenView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ssf.homevisit.R;
import com.ssf.homevisit.activity.MainActivity;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.databinding.ActivityRmnchaPncServiceBinding;
import com.ssf.homevisit.databinding.DialogueLayoutRmnchaDeliveryOutcomesForPncBinding;
import com.ssf.homevisit.databinding.DialogueLayoutRmnchaInfantDetailsForPncBinding;
import com.ssf.homevisit.models.AshaWorkerResponse;
import com.ssf.homevisit.models.RMNCHACoupleDetailsResponse;
import com.ssf.homevisit.models.RMNCHAMotherInfantDetailsResponse;
import com.ssf.homevisit.models.RMNCHAPNCDeliveryOutcomesResponse;
import com.ssf.homevisit.models.RMNCHAPNCInfantResponse;
import com.ssf.homevisit.models.RMNCHAPNCInfantServiceRequest;
import com.ssf.homevisit.models.RMNCHAPNCInfantVisitLogRequest;
import com.ssf.homevisit.models.RMNCHAPNCMotherServiceRequest;
import com.ssf.homevisit.models.RMNCHAPNCMotherVisitLogRequest;
import com.ssf.homevisit.models.RMNCHAPNCWomenResponse;
import com.ssf.homevisit.rmncha.base.RMNCHABaseActivity;
import com.ssf.homevisit.rmncha.pnc.details.PNCInfantDetailsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PNCServiceActivity extends RMNCHABaseActivity implements PNCServiceAdapter.PNCServiceItemListener {
    public static final String PARAM_1 = "param_1";
    public static final String PARAM_SUB_CENTER = "PARAM_SUB_CENTER";
    public static final String SELECTED_WOMEN_KEY = "SELECTED_WOMEN_KEY";
    private ActivityRmnchaPncServiceBinding binding;
    private String houseHoldUUID;
    private String subCenterUUID;
    private String pncServiceID;
    private PNCServiceViewModel viewModel;
    private String pncRegistrationId;
    private RMNCHAPNCWomenResponse.Content selectedWomen;
    private List<AshaWorkerResponse.Content> ashaWorkersList = new ArrayList<>();
    private int selectedView = -1;
    private final int MOTHER_SELECTED = 0;
    private  Boolean isBabyClicked = false;

    private  Boolean isMotherClicked = false;
    private String[] riskList;
    private boolean[] riskSelectedList;
    private RMNCHAPNCDeliveryOutcomesResponse rmnchapncDeliveryOutcomesResponse;
    private List<RMNCHAPNCInfantResponse.Infant> babiesList;
    private RMNCHACoupleDetailsResponse.Content coupleData;
    private String rchID;
    private String selectedWomenUUID;
    private String husbandUUID;
    private List<RMNCHAMotherInfantDetailsResponse.Content> motherInfantList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppController.getInstance().setPncServiceActivity(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rmncha_pnc_service);
        viewModel = new PNCServiceViewModel(getApplication());
        binding.setViewModel(viewModel);
        initialiseViews();
    }

    private void initialiseViews() {
        try {
            houseHoldUUID = getIntent().getExtras().get(PARAM_1) + "";
            subCenterUUID = getIntent().getExtras().get(PARAM_SUB_CENTER) + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        riskList = getResources().getStringArray(R.array.array_possible_death);
        riskSelectedList = getDummyBoolList(riskList.length);
        resetViews();
        binding.spinnerCovidTestTaken.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data = binding.spinnerCovidTestTaken.getSelectedItem() + "";
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
        binding.buttonSave.setOnClickListener(view -> {
            if (selectedView == -1) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("PNC_MAPPING_FRAGMENT", 2);
                startActivity(intent);
            } else {
                if (isMotherClicked) {
                    continueMotherSaveClick();
                } else if (isBabyClicked) {
                    continueInfantSaveClick();
                }
            }
        });
        binding.buttonCancel.setOnClickListener(view -> {
            isBabyClicked=false;
            isMotherClicked=false;
            selectedView = -1;
            resetViews();
            setAdapterViews();
        });
        setSelectedWomenView();
        getAshaWorkerData();
        setAdapterViews();

    }

    private void resetViews() {
        binding.layoutSelectBeneficiary.setVisibility(View.VISIBLE);
        binding.layoutMotherPnc.setVisibility(View.GONE);
        binding.layoutChildPnc.setVisibility(View.GONE);
        binding.buttonCancel.setVisibility(View.GONE);
    }

    private void setSelectedWomenView() {
        try {
            selectedWomen = (RMNCHAPNCWomenResponse.Content) getIntent().getExtras().getSerializable(SELECTED_WOMEN_KEY);
            rchID = selectedWomen.getSource().getProperties().getRchId();
            selectedWomenUUID = selectedWomen.getSource().getProperties().getUuid();
            setCoupleDetails();
        } catch (Exception e) {
            showMyToast(this, "Selected Women is Invalid");
        }
    }

    private void setCoupleDetails() {
        try {
            showProgressBar();
            viewModel.getRMNCHACoupleDetailsLiveData(selectedWomenUUID).observe(this, contents -> {
                hideProgressBar();
                if (contents != null && contents.size() > 0) {
                    coupleData = contents.get(0);
                    binding.tvHeaderRchID.setText(coupleData.getSource().getProperties().getRchId());
                    binding.hhHeadName.setText(selectedWomen.getSource().getProperties().getHouseHeadName());
                    RMNCHACoupleDetailsResponse.Content.Target.TargetProperties husbandProperties = coupleData.getTarget().getProperties();
                    String husbandAge = getAgeFromDOB(husbandProperties.getDateOfBirth());
                    String husbandGender = setNonNullValue(husbandProperties.getGender());
                    String husbandName = husbandProperties.getFirstName();
                    husbandUUID = husbandProperties.getUuid();
                    binding.headerHusbandName.setText(husbandName + " - " + husbandAge + " - " + husbandGender);
                    pncRegistrationId = coupleData.getRelationship().getProperties().getPncRegistrationId();
                    pncServiceID = coupleData.getRelationship().getProperties().getPncServiceId();
                    getBabiesData();
                } else {
                    showMyToast(this, "Error fetching Couple Data  : " + viewModel.getErrorMessage());
                }
            });
        } catch (Exception e) {
            hideProgressBar();
            showMyToast(this, "Error fetching Couple Data");
        }
    }

    private void getBabiesData() {
        showProgressBar();
        viewModel.getInfantDetailsData(pncRegistrationId).observe(this, contents -> {
            hideProgressBar();
            if (contents != null && contents.getInfants() != null) {
                babiesList = contents.getInfants();
                getMotherInfantData();
            } else {
                showMyToast(this, "No babies to provide service");
            }
            setAdapterViews();
            makeDeliveryOutcomesCall(false);
        });
    }

    private void getMotherInfantData() {
        showProgressBar();
        viewModel.getMotherInfantDetailsData(selectedWomenUUID).observe(this, contents -> {
            hideProgressBar();
            if (contents != null && contents.getContent() != null) {
                motherInfantList = contents.getContent();
            } else {
                showMyToast(this, "No babies to provide service");
            }
        });
    }

    private void setAdapterViews() {
        binding.recyclerView.setAdapter(new PNCServiceAdapter(this, babiesList, selectedWomen, this));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getAshaWorkerData() {
        viewModel.getAshaWorkerLiveData(subCenterUUID).observe(this, contents -> {
            ashaWorkersList = contents;
            setAshaWorkersAdapter();
        });
    }

    void setAshaWorkersAdapter() {
        binding.spinnerSelectAshaName.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, getAshaWorkerNamesList(ashaWorkersList)));
    }

    @Override
    public void onMotherItemClicked() {
        selectedView = MOTHER_SELECTED;
        isMotherClicked=true;
        isBabyClicked=false;
        binding.layoutSelectBeneficiary.setVisibility(View.GONE);
        binding.layoutMotherPnc.setVisibility(View.VISIBLE);
        binding.layoutChildPnc.setVisibility(View.GONE);
        binding.buttonCancel.setVisibility(View.VISIBLE);

        binding.temperatureMinus.setOnClickListener(view -> {
            String data = binding.temperature.getText() + "";
            float temp;
            if (!data.isEmpty()) {
                temp = Float.parseFloat(data);
                temp -= 0.01;
                binding.temperature.setText(temp + "");
            }
        });
        binding.temperaturePlus.setOnClickListener(view -> {
            String data = binding.temperature.getText() + "";
            float temp;
            if (!data.isEmpty()) {
                temp = Float.parseFloat(data);
                temp += 0.01;
                binding.temperature.setText(temp + "");
            }
        });

        binding.tvHeaderRchIDItitle.setText("RCH-ID");
        binding.tvHeaderRchID.setText(selectedWomen.getSource().getProperties().getRchId());

        if (rmnchapncDeliveryOutcomesResponse != null) {
            RMNCHAPNCDeliveryOutcomesResponse.DeliveryDetails deliverDetails = rmnchapncDeliveryOutcomesResponse.getDeliveryDetails();
            if (deliverDetails != null) {
                binding.textViewHeaderTypeOfDelivery.setText(setNonNullValue(deliverDetails.getDeliveryType()));
                binding.textViewHeaderDeliveryDate.setText(getDateToView(deliverDetails.getDeliveryDate(), RMNCHA_DATE_FORMAT));
            }
        }
        setPNCMotherServiceVisitHistory();

        binding.pncDate.setText(getDateToView(getCurrentDate(), RMNCHA_DATE_FORMAT));

    }

    private void continueMotherSaveClick() {

        String ashaName = binding.spinnerSelectAshaName.getSelectedItem() + "";
        if (ashaName.equalsIgnoreCase("select")) {
            showMyToast(this, "Select Valid Asha Worker Name");
            return;
        }

        String deliveryType = binding.textViewHeaderTypeOfDelivery.getText() + "";

        String deliveryDate = (binding.textViewHeaderDeliveryDate.getText() + "").replaceAll("/", "-");

        String pncPeriod = binding.spinnerPncPeriod.getSelectedItem() + "";

        if (pncPeriod.equalsIgnoreCase("select")) {
            showMyToast(this, "Select PNC Period");
            return;
        }

        String pncDate = getCurrentDate();
        if (pncDate.length() < 1) {
            showMyToast(this, "Select PNC Date");
            return;
        }

        int ironFolicAcid = 0;
        String ifa = binding.noOfIronFolicAcid.getText() + "";
        if (ifa.length() > 0) {
            ironFolicAcid = Integer.parseInt(ifa);
        }

        String ppnMethod = binding.spinnerPpcMethod.getSelectedItem() + "";

        String signsOfDanger = binding.spinnerSignOfDanger.getSelectedItem() + "";

        String referralFacility = binding.spinnerReferralFacility.getSelectedItem() + "";

        String referralPlace = binding.spinnerReferralPlace.getSelectedItem() + "";

        String remarks = binding.remarks.getText() + "";

        boolean covidTestTaken = "done".equalsIgnoreCase(binding.spinnerCovidTestTaken.getSelectedItem() + "");

        boolean isCovidResultPositive = "positive".equalsIgnoreCase(binding.spinnerCovidTestResult.getSelectedItem() + "");

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

        String anyComplaints = binding.anyComplaints.getText() + "";
        String pallor = binding.pallor.getText() + "";
        String bloodPressure = binding.bloodPressure.getText() + "";
        String temperature = binding.temperature.getText() + "";

        String breastCondition = binding.spinnerBreastCondition.getSelectedItem() + "";
        String nippleCondition = binding.spinnerNippleCondition.getSelectedItem() + "";
        String uterusTenderness = binding.spinnerUterusTenderness.getSelectedItem() + "";
        String bleeding = binding.spinnerBleeding.getSelectedItem() + "";
        String lochiaCondition = binding.spinnerLochiaCondition.getSelectedItem() + "";
        String episiotomy = binding.spinnerEpisiotomy.getSelectedItem() + "";
        String familyPlanning = binding.spinnerFamilyPlanning.getSelectedItem() + "";
        String anyComplications = binding.anyComplications.getText() + "";
        boolean referToPHC = binding.checkboxReferPhc.isChecked();

        RMNCHAPNCMotherServiceRequest serviceRequest = new RMNCHAPNCMotherServiceRequest(
                rchID, ashaName, deliveryType, deliveryDate, selectedWomenUUID, husbandUUID);

        RMNCHAPNCMotherVisitLogRequest visitLogRequest = new RMNCHAPNCMotherVisitLogRequest();
        pncPeriod= pncPeriod.replace("Day","").trim();
        visitLogRequest.setPncPeriod(Integer.parseInt(pncPeriod));
        visitLogRequest.setPncDate(pncDate);
        visitLogRequest.setNoIFATabletsGiven(ironFolicAcid + "");
        visitLogRequest.setPpcMethod(ppnMethod);
        visitLogRequest.setSignOfMotherDanger(signsOfDanger);
        visitLogRequest.setReferralFacility(referralFacility);
        visitLogRequest.setReferralPlace(referralPlace);
        visitLogRequest.setRemark(remarks);
        visitLogRequest.setCovidTestDone(covidTestTaken);
        visitLogRequest.setCovidResultPositive(isCovidResultPositive);
        visitLogRequest.setILIExperienced(isILIExperienced);
        visitLogRequest.setDidContactCovidPatient(didContactCovidPatient);
        visitLogRequest.setComplaints(anyComplaints);
        visitLogRequest.setPallor(pallor);
        visitLogRequest.setBloodPressure(bloodPressure);
        visitLogRequest.setTemperature(temperature);
        visitLogRequest.setBreastsCondition(breastCondition);
        visitLogRequest.setNippleCondition(nippleCondition);
        visitLogRequest.setUterusTenderness(uterusTenderness);
        visitLogRequest.setBleeding(bleeding);
        visitLogRequest.setLochiaCondition(lochiaCondition);
        visitLogRequest.setEpisiotomyORTear(episiotomy);
        visitLogRequest.setFamilyPlanCounselling(familyPlanning);
        visitLogRequest.setComplications(anyComplications);
        visitLogRequest.setReferredToPHC(referToPHC);
        if (pncServiceID == null || pncServiceID.isEmpty()) {
            makePNCServiceRequest(serviceRequest, visitLogRequest);
        } else {
            visitLogRequest.setPncServiceId(pncServiceID);
            makePNCServiceVisitLogRequest(pncServiceID, visitLogRequest);
        }
    }

    private void makePNCServiceRequest(RMNCHAPNCMotherServiceRequest serviceRequest, RMNCHAPNCMotherVisitLogRequest visitLogRequest) {
        showProgressBar();
        viewModel.makePNCMotherServiceRequest(serviceRequest).observe(this, response -> {
            hideProgressBar();
            if (response != null) {
                String id = response.getId();
                if (id != null && id.length() > 2) {
                    pncServiceID = id;
                    visitLogRequest.setServiceId(id);
                    makePNCServiceVisitLogRequest(id, visitLogRequest);
                    showMyToast(this, "PNC Service Success");
                } else {
                    showMyToast(this, "PNC Service failed");
                }
            } else {
                showMyToast(this, "PNC Service failed : " + viewModel.getErrorMessage());
            }
        });
    }

    private void makePNCServiceVisitLogRequest(String pncServiceId, RMNCHAPNCMotherVisitLogRequest visitLogRequest) {
        showProgressBar();
        viewModel.makePNCMotherVisitLogRequest(visitLogRequest, pncServiceId).observe(this, response -> {
            hideProgressBar();
            if (response != null) {
                showMyToast(this, "Visit Log Success");
                selectedView = -1;
                resetViews();
                setAdapterViews();
            } else {
                showMyToast(this, "Visit Log failed  : " + viewModel.getErrorMessage());
            }
        });
    }

    private void setPNCMotherServiceVisitHistory() {
        if (pncServiceID != null) {
            showProgressBar();
            viewModel.getPNCMotherServiceHistoryLiveData(pncServiceID).observe(this, response -> {
                hideProgressBar();
                if (response != null) {
                    binding.recyclerviewPncMotherServiceHistory.setLayoutManager(new LinearLayoutManager(this));
                    binding.recyclerviewPncMotherServiceHistory.setAdapter(new PNCMotherServiceHistoryAdapter(this, response));
                } else {
                    showMyToast(this, "Visit Log failed  : " + viewModel.getErrorMessage());
                }
            });
        }
    }

    @Override
    public void onBabyItemClicked(int position) {
        isBabyClicked=true;
        isMotherClicked=false;
        selectedView = position;
        binding.layoutSelectBeneficiary.setVisibility(View.GONE);
        binding.layoutMotherPnc.setVisibility(View.GONE);
        binding.layoutChildPnc.setVisibility(View.VISIBLE);
        binding.buttonCancel.setVisibility(View.VISIBLE);

        setPNCInfantServiceVisitHistory();

        binding.childPncDate.setText(getDateToView(getCurrentDate(), RMNCHA_DATE_FORMAT));


        binding.childTempMinus.setOnClickListener(view -> {
            String data = binding.childTemperature.getText() + "";
            float temp;
            if (!data.isEmpty()) {
                temp = Float.parseFloat(data);
                temp -= 0.01;
                binding.childTemperature.setText(temp + "");
            }
        });
        binding.childTempPlus.setOnClickListener(view -> {
            String data = binding.childTemperature.getText() + "";
            float temp;
            if (!data.isEmpty()) {
                temp = Float.parseFloat(data);
                temp += 0.01;
                binding.childTemperature.setText(temp + "");
            }
        });

    }

    private void setPNCInfantServiceVisitHistory() {
        showProgressBar();
        RMNCHAMotherInfantDetailsResponse.Content childItem = null;
        for (RMNCHAMotherInfantDetailsResponse.Content motherInfantItem: motherInfantList){
            if(Objects.equals(motherInfantItem.getRelationship().getProperties().getChildId(), babiesList.get(selectedView-1).getChildId())){
                childItem=motherInfantItem;
            }
        }
        RMNCHAPNCInfantResponse.Infant registrationChild = babiesList.get(selectedView - 1);
        binding.textViewHeaderDateRegistration.setText(getDateToView(registrationChild.getRegistrationDate(), RMNCHA_DATE_FORMAT));
        binding.textViewHeaderChildDeliveryDate.setText(getDateToView(registrationChild.getDateOfBirth(), RMNCHA_DATE_FORMAT));
        String childId = registrationChild.getChildId() + "";
        binding.tvHeaderRchIDItitle.setText("CHILD-ID");
        binding.tvHeaderRchID.setText(childId);
        if (!childId.isEmpty()) {
            viewModel.getPNCInfantServiceHistoryLiveData(childItem.getTarget().getProperties().getUuid()).observe(this, response -> {
                hideProgressBar();
                if (response != null) {
                    binding.recyclerviewPncChildServiceHistory.setLayoutManager(new LinearLayoutManager(this));
                    binding.recyclerviewPncChildServiceHistory.setAdapter(new PNCInfantServiceHistoryAdapter(this, response));
                } else {
                    showMyToast(this, "Visit Log failed  : " + viewModel.getErrorMessage());
                }
            });
        }
    }

    private void continueInfantSaveClick() {

        String ashaName = binding.spinnerSelectAshaName.getSelectedItem() + "";
        if (ashaName.equalsIgnoreCase("select")) {
            showMyToast(this, "Select Valid Asha Worker Name");
            return;
        }

        String deliveryDate = binding.textViewHeaderChildDeliveryDate.getText() + "";
        String pncPeriod = binding.spinnerChildPncPeriod.getSelectedItem() + "";
        pncPeriod= pncPeriod.replace("Day","").trim();

        if (pncPeriod.equalsIgnoreCase("select")) {
            showMyToast(this, "Select PNC Period");
            return;
        }

        String pncDate = (binding.childPncDate.getText() + "").replaceAll("/", "-");
        String danger = binding.spinnerChildSignOfDanger.getSelectedItem() + "";
        String referralFacility = binding.spinnerChildReferralFacility.getSelectedItem() + "";
        String referralPlace = binding.spinnerChildReferralPlace.getSelectedItem() + "";
        String remark = binding.childRemarks.getText() + "";
        String infantWeight = (binding.childWeight.getText() + "");
        if (infantWeight.length() < 1) {
            showMyToast(this, "Enter Infant Weight");
            return;
        }

        String urinePassed = binding.spinnerChildUrinePassed.getSelectedItem() + "";
        String stoolPassed = binding.spinnerChildStoolPassed.getSelectedItem() + "";
        String temperature = binding.childTemperature.getText() + "";
        String jaundice = binding.spinnerJaundice.getSelectedItem() + "";
        String diarrhoea = binding.spinnerDiarrhoea.getSelectedItem() + "";
        String vomiting = binding.spinnerVomiting.getSelectedItem() + "";
        String convolution = binding.spinnerConvolusions.getSelectedItem() + "";
        String activity = binding.spinnerActivity.getSelectedItem() + "";
        String sucking = binding.spinnerSucking.getSelectedItem() + "";
        String breathing = binding.spinnerBreathing.getSelectedItem() + "";
        String chest = binding.spinnerChest.getSelectedItem() + "";
        String skinPustules = binding.spinnerSkinPustules.getSelectedItem() + "";
        String conditionOfUmbilical = binding.conditionOfUmbilical.getText() + "";
        String complications = binding.childAnyComplications.getText() + "";
        RMNCHAPNCInfantVisitLogRequest visitLogRequest = new RMNCHAPNCInfantVisitLogRequest();
        visitLogRequest.setPncPeriod(Integer.parseInt(pncPeriod));
        visitLogRequest.setPncDate(pncDate);
        visitLogRequest.setSignOfInfantDanger(danger);
        visitLogRequest.setReferralFacility(referralFacility);
        visitLogRequest.setReferralPlace(referralPlace);
        visitLogRequest.setRemarks(remark);
        visitLogRequest.setWeight(infantWeight);
        visitLogRequest.setUrinePassed(urinePassed);
        visitLogRequest.setStoolPassed(stoolPassed);
        visitLogRequest.setTemperature(temperature);
        visitLogRequest.setJaundice(jaundice);
        visitLogRequest.setDiarrhea(diarrhoea);
        visitLogRequest.setVomiting(vomiting);
        visitLogRequest.setConvulsions(convolution);
        visitLogRequest.setActivity(activity);
        visitLogRequest.setSucking(sucking);
        visitLogRequest.setUmbilicalStumpCondition(conditionOfUmbilical);
        visitLogRequest.setComplications(complications);
        RMNCHAMotherInfantDetailsResponse.Content childItem = null;
        for (RMNCHAMotherInfantDetailsResponse.Content motherInfantItem: motherInfantList){
            if(Objects.equals(motherInfantItem.getRelationship().getProperties().getChildId(), babiesList.get(selectedView-1).getChildId())){
                childItem=motherInfantItem;
            }
        }
        visitLogRequest.setServiceId(pncServiceID);
        visitLogRequest.setChildId(childItem.getTarget().getProperties().getUuid());
        RMNCHAPNCInfantServiceRequest serviceRequest = new RMNCHAPNCInfantServiceRequest(
                childItem.getTarget().getProperties().getUuid() + "", pncServiceID, ashaName);
        String childServiceID = "";
        if (childItem.getRelationship() != null && childItem.getRelationship().getProperties() != null && childItem.getRelationship().getProperties().getPncInfantServiceId() != null) {
            childServiceID = childItem.getRelationship().getProperties().getPncInfantServiceId();
        }

        if (pncServiceID == null || pncServiceID.isEmpty()) {
            showMyToast(this, "Invalid PNC ServiceId");
            return;
        }

        if (childServiceID.isEmpty()) {
            makePNCChildServiceRequest(serviceRequest, visitLogRequest, pncServiceID);
        } else {
            makePNCChildServiceVisitLogRequest(pncServiceID, visitLogRequest);
        }
    }

    private void makePNCChildServiceRequest(RMNCHAPNCInfantServiceRequest serviceRequest,
                                            RMNCHAPNCInfantVisitLogRequest visitLogRequest, String pncServiceID) {
        showProgressBar();
        viewModel.makePNCInfantServiceRequest(serviceRequest, pncServiceID).observe(this, response -> {
            hideProgressBar();
            if (response != null) {
                String pncServiceId = response.getPncServiceId();
                if (pncServiceId != null && pncServiceId.length() > 2) {
                    visitLogRequest.setServiceId(response.pncServiceId);
                    makePNCChildServiceVisitLogRequest(pncServiceId, visitLogRequest);
                    showMyToast(this, "PNC Service Success");
                } else {
                    showMyToast(this, "PNC Service failed");
                }
            } else {
                showMyToast(this, "PNC Service failed : " + viewModel.getErrorMessage());
            }
        });
    }

    private void makePNCChildServiceVisitLogRequest(String pncServiceId, RMNCHAPNCInfantVisitLogRequest visitLogRequest) {
        showProgressBar();
        viewModel.makePNCInfantVisitLogRequest(visitLogRequest, pncServiceId).observe(this, response -> {
            hideProgressBar();
            if (response != null) {
                showMyToast(this, "Visit Log Success");
                selectedView = -1;
                resetViews();
                setAdapterViews();
            } else {
                showMyToast(this, "Visit Log failed  : " + viewModel.getErrorMessage());
            }
        });
    }

    @Override
    public void onDeliveryOutcomesClicked() {
        if (rmnchapncDeliveryOutcomesResponse == null) {
            makeDeliveryOutcomesCall(true);
        } else {
            showDeliveryOutcomesAlert();
        }
    }

    private void makeDeliveryOutcomesCall(boolean showAlert) {
        showProgressBar();
        viewModel.getDeliveryOutcomesData(pncRegistrationId).observe(this, contents -> {
            hideProgressBar();
            rmnchapncDeliveryOutcomesResponse = contents;
            if (showAlert)
                showDeliveryOutcomesAlert();
        });
    }

    private void showDeliveryOutcomesAlert() {
        final Dialog dialog = new Dialog(this);
        DialogueLayoutRmnchaDeliveryOutcomesForPncBinding deliveryOutcomesForPncBinding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialogue_layout_rmncha_delivery_outcomes_for_pnc, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(deliveryOutcomesForPncBinding.getRoot());
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(1250, WindowManager.LayoutParams.WRAP_CONTENT);
        deliveryOutcomesForPncBinding.closeDialogue.setOnClickListener(v -> dialog.dismiss());
        setPNCWomenView(deliveryOutcomesForPncBinding.womenLayout, selectedWomen);
        deliveryOutcomesForPncBinding.buttonSubmit.setVisibility(View.INVISIBLE);
        if (rmnchapncDeliveryOutcomesResponse != null) {

            deliveryOutcomesForPncBinding.textViewAncVisitDate.setText(rmnchapncDeliveryOutcomesResponse.getCouple().getLastANCVisitDate());

            RMNCHAPNCDeliveryOutcomesResponse.MensuralPeriod menstrualPeriod = rmnchapncDeliveryOutcomesResponse.getMenstrualPeriod();
            if (menstrualPeriod != null) {
                deliveryOutcomesForPncBinding.textViewLmpDate.setText(getDateToView(menstrualPeriod.getLmpDate(), RMNCHA_DATE_FORMAT));
                deliveryOutcomesForPncBinding.textViewEddDate.setText(getDateToView(menstrualPeriod.getEddDate(), RMNCHA_DATE_FORMAT));
            }

            RMNCHAPNCDeliveryOutcomesResponse.DeliveryDetails deliveryDetails = rmnchapncDeliveryOutcomesResponse.getDeliveryDetails();
            if (deliveryDetails != null) {
                String deliveryDate = deliveryDetails.getDeliveryDate();
                if (deliveryDate != null) {
                    deliveryOutcomesForPncBinding.textViewDeliveryDate.setText(getDateToView(deliveryDate, RMNCHA_DATE_FORMAT));
                    deliveryOutcomesForPncBinding.textViewDeliveryTime.setText(getTimeToView(deliveryDate, RMNCHA_DATE_FORMAT));
                }

                String dischargeDate = deliveryDetails.getDischargeDateTime();
                deliveryOutcomesForPncBinding.textViewDateOfDischarge.setText(getDateToView(dischargeDate, RMNCHA_DATE_FORMAT));
                deliveryOutcomesForPncBinding.textViewDischargeTime.setText(getTimeToView(dischargeDate, RMNCHA_DATE_FORMAT));
            }

            deliveryOutcomesForPncBinding.spinnerPlaceOfDelivery.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerPlaceOfDelivery.setSelection(getPositionFor(deliveryOutcomesForPncBinding.spinnerPlaceOfDelivery, deliveryDetails.getPlace()));

            deliveryOutcomesForPncBinding.spinnerLocationOfDelivery.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerLocationOfDelivery.setSelection(getPositionFor(deliveryOutcomesForPncBinding.spinnerLocationOfDelivery, deliveryDetails.getLocation()));

            deliveryOutcomesForPncBinding.spinnerDeliveryConductedBy.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerDeliveryConductedBy.setSelection(getPositionFor(deliveryOutcomesForPncBinding.spinnerDeliveryConductedBy, deliveryDetails.getConductedBy()));

            deliveryOutcomesForPncBinding.spinnerTypeOfDelivery.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerTypeOfDelivery.setSelection(getPositionFor(deliveryOutcomesForPncBinding.spinnerTypeOfDelivery, deliveryDetails.getDeliveryType()));

            deliveryOutcomesForPncBinding.spinnerOutcomesOfDelivery.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerOutcomesOfDelivery.setSelection(getPositionFor(deliveryOutcomesForPncBinding.spinnerOutcomesOfDelivery, deliveryDetails.getDeliveryOutcome() + ""));

            deliveryOutcomesForPncBinding.spinnerLiveBirth.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerLiveBirth.setSelection(getPositionFor(deliveryOutcomesForPncBinding.spinnerLiveBirth, deliveryDetails.getLiveBirthCount() + ""));

            deliveryOutcomesForPncBinding.spinnerStillBirth.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerStillBirth.setSelection(getPositionFor(deliveryOutcomesForPncBinding.spinnerStillBirth, deliveryDetails.getStillBirthCount() + ""));

            deliveryOutcomesForPncBinding.spinnerDeliveryComplications.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerDeliveryComplications.setSelection(getPositionFor(deliveryOutcomesForPncBinding.spinnerDeliveryComplications, deliveryDetails.getComplications()));


            int covidTestDone = 0;
            if (rmnchapncDeliveryOutcomesResponse.isCovidTestDone()) {
                covidTestDone = 1;
            } else {
                covidTestDone = 2;
            }
            deliveryOutcomesForPncBinding.spinnerCovidTestTaken.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerCovidTestTaken.setSelection(covidTestDone);

            int covidResult = 0;
            if (rmnchapncDeliveryOutcomesResponse.isCovidResultPositive()) {
                covidResult = 1;
            }

            deliveryOutcomesForPncBinding.spinnerCovidTestResult.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerCovidTestResult.setSelection(covidResult);

            int ili = 0;
            if (rmnchapncDeliveryOutcomesResponse.isILIExperienced()) {
                ili = 1;
            } else {
                ili = 2;
            }

            deliveryOutcomesForPncBinding.spinnerHavingIli.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerHavingIli.setSelection(ili);

            int covidContact = 0;
            if (rmnchapncDeliveryOutcomesResponse.isDidContactCovidPatient()) {
                covidContact = 1;
            } else {
                covidContact = 2;
            }

            deliveryOutcomesForPncBinding.spinnerCovidContact.setEnabled(false);
            deliveryOutcomesForPncBinding.spinnerCovidContact.setSelection(covidContact);


        }
    }

    private int getPositionFor(Spinner spinner, String value) {
        int position = ((ArrayAdapter) spinner.getAdapter()).getPosition(value);
        if (position == -1) position = 1;
        return position;
    }

    @Override
    public void onInfantDetailsClicked() {
        showInfantDetailsAlert();
    }

    private void showInfantDetailsAlert() {
        final Dialog dialog = new Dialog(this);
        DialogueLayoutRmnchaInfantDetailsForPncBinding infantDetailsForPncBinding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialogue_layout_rmncha_infant_details_for_pnc, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(infantDetailsForPncBinding.getRoot());
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(1250, WindowManager.LayoutParams.WRAP_CONTENT);
        infantDetailsForPncBinding.closeDialogue.setOnClickListener(v -> dialog.dismiss());
        setPNCWomenView(infantDetailsForPncBinding.womenLayout, selectedWomen);
        infantDetailsForPncBinding.mandatoryView.setVisibility(View.GONE);
        PNCInfantDetailsView infantDetailsView = (PNCInfantDetailsView) getSupportFragmentManager().findFragmentById(R.id.infant_fragment);
        infantDetailsForPncBinding.closeDialogue.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().remove(infantDetailsView).commitAllowingStateLoss();
            dialog.dismiss();
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

    public List<RMNCHAPNCInfantResponse.Infant> getInfantDetails() {
        return babiesList;
    }

    public String getLMPDate() {
        return "lmp";
    }

    public String getEDDDate() {
        return "edd";
    }

    public String getLastANCVisitDate() {
        return "anc";
    }

}