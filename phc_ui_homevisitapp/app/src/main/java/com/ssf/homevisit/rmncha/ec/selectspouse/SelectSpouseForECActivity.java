package com.ssf.homevisit.rmncha.ec.selectspouse;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getAshaWorkerNamesList;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getCurrentDate;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getHusbandAge;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getTextChangeListener;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.setNonNullValue;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;
import static com.ssf.homevisit.rmncha.ec.selectwomen.ECWomenAdapter.setHouseHoldMemberView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ssf.homevisit.R;
import com.ssf.homevisit.databinding.ActivityRmnchaSelectSpouseForEcBinding;
import com.ssf.homevisit.databinding.DialogueLayoutRmnchaEnterRchIdBinding;
import com.ssf.homevisit.databinding.DialogueLayoutRmnchaRchIdStatusBinding;
import com.ssf.homevisit.databinding.DialogueLayoutRmnchaSelectSpouseForEcBinding;
import com.ssf.homevisit.models.AshaWorkerResponse;
import com.ssf.homevisit.models.FormUtilityResponse;
import com.ssf.homevisit.models.RMNCHAECRegistrationRequest;
import com.ssf.homevisit.models.RMNCHAMembersInHouseHoldResponse;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.base.RMNCHABaseActivity;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectSpouseForECActivity extends RMNCHABaseActivity {

    public static final String SELECTED_WOMEN_KEY = "SELECTED_WOMEN_KEY";
    public static final String PARAM_1 = "param_1";
    public static final String PARAM_SUB_CENTER = "PARAM_SUB_CENTER";
    private ActivityRmnchaSelectSpouseForEcBinding binding;
    private SelectSpouseForECViewModel viewModel;
    private RMNCHAMembersInHouseHoldResponse.Content selectedWomen;
    private RMNCHAMembersInHouseHoldResponse.Content selectedMen;
    private List<RMNCHAMembersInHouseHoldResponse.Content> allMemberList = new ArrayList<>();
    private List<RMNCHAMembersInHouseHoldResponse.Content> menMemberList = new ArrayList<>();
    private List<AshaWorkerResponse.Content> ashaWorkersList = new ArrayList<>();
    private RMNCHAECRegistrationRequest rmnchaECRegistrationRequest = new RMNCHAECRegistrationRequest();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rmncha_select_spouse_for_ec);
        viewModel = new ViewModelProvider(this).get(SelectSpouseForECViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        binding.backButton.setOnClickListener(view -> this.onBackPressed());
        binding.buttonSelectSpouse.setOnClickListener(v -> {
            showSpouseSelectionAlert();
        });
        setSelectedWomenView();
        setHeadName();
    }

    private void setSelectedWomenView() {
        try {
            selectedWomen = (RMNCHAMembersInHouseHoldResponse.Content) getIntent().getExtras().getSerializable(SELECTED_WOMEN_KEY);
            RMNCHAMembersInHouseHoldResponse.Content.TargetNode.TargetProperties properties = selectedWomen.getTargetNode().getProperties();
            String value = setNonNullValue(properties.getFirstName()) + " - " + setNonNullValue(properties.getAge() + "") + " - " + setNonNullValue(properties.getGender());
            binding.headerWomenName.setText(value);
            setHouseHoldMemberView(binding.getRoot(), selectedWomen);
        } catch (Exception e) {
            showMyToast(this, "Selected Women is Invalid");
        }
    }

    private void setHeadName() {
        try {
            String houseHoldUUID = getIntent().getExtras().get(PARAM_1) + "";
            viewModel.getAllMembersInHouseHoldLiveData(houseHoldUUID).observe(this, contents -> {
                if (contents != null) {
                    allMemberList = contents;
                    menMemberList = getAllMenContentList();
                    binding.hhHeadName.setText(getHeadName());
                } else {
                    showMyToast(this, "Error fetching HH Data  : " + viewModel.getErrorMessage());
                }
            });
        } catch (Exception e) {
            showMyToast(this, "Error fetching Mens Data");
        }
    }

    private void setMensAdapter(View progressBar, Spinner spinnerView) {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        if (spinnerView != null)
            spinnerView.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview, getAllMenNamesList()));
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
                    showMyToast(this, "Error fetching Asha Workers Data  : " + viewModel.getErrorMessage());
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            showMyToast(this, "Error fetching Asha Workers Data");
        }
    }

    void showEnterRchIDAlert() {
        final Dialog dialog = new Dialog(this);
        DialogueLayoutRmnchaEnterRchIdBinding binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialogue_layout_rmncha_enter_rch_id, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        binding.closeDialogue.setOnClickListener(v -> dialog.dismiss());
        binding.buttonSubmit.setOnClickListener(view -> {

            RMNCHAECRegistrationRequest.RchGeneration rchGeneration = rmnchaECRegistrationRequest.getRchGeneration();
            if (rchGeneration == null)
                rchGeneration = new RMNCHAECRegistrationRequest.RchGeneration();

            String rchID = binding.editTextRchId.getText() + "";
            if (rchID.length() < 12) {
                showMyToast(this, "Enter Valid RCH ID");
                return;
            }
            rchGeneration.setRchId(rchID);
            rmnchaECRegistrationRequest.setRchGeneration(rchGeneration);
            dialog.dismiss();
            makeECRegistrationCall();
        });
    }

    private void makeECRegistrationCall() {
        showProgressBar();
        viewModel.makeECRegistration(rmnchaECRegistrationRequest).observe(this, rmnchaecRegistrationResponse -> {
            hideProgressBar();
            if (rmnchaecRegistrationResponse != null) {
                showMyToast(this, "EC Registration Successful");
                SelectSpouseForECActivity.this.finish();
            } else {
                showMyToast(this, "EC Registration Failed  : " + viewModel.getErrorMessage());
            }
        });
    }

    void showSaveContinueAlert() {
        final Dialog dialog = new Dialog(this);
        DialogueLayoutRmnchaRchIdStatusBinding binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialogue_layout_rmncha_rch_id_status, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        binding.closeDialogue.setOnClickListener(v -> dialog.dismiss());
    }

    void showSpouseSelectionAlert() {
        final Dialog dialog = new Dialog(this);
        DialogueLayoutRmnchaSelectSpouseForEcBinding bindingSelectSpouseAlert = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.dialogue_layout_rmncha_select_spouse_for_ec, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(bindingSelectSpouseAlert.getRoot());
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(1250, WindowManager.LayoutParams.WRAP_CONTENT);
        getFormUtilityData(bindingSelectSpouseAlert);
        bindingSelectSpouseAlert.closeDialogue.setOnClickListener(v -> dialog.dismiss());
        setMensAdapter(bindingSelectSpouseAlert.progressBarSelectSpouse, bindingSelectSpouseAlert.spinnerSelectSpouse);
        getAshaWorkerData(bindingSelectSpouseAlert.progressBarSelectAsha, bindingSelectSpouseAlert.spinnerSelectAshaName);
        setHouseHoldMemberView(bindingSelectSpouseAlert.womenLayout, selectedWomen);

        bindingSelectSpouseAlert.spinnerSelectSpouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    bindingSelectSpouseAlert.processTitle.setText(getResources().getString(R.string.hh_select_spouse_header_with_rchid_message));
                    bindingSelectSpouseAlert.layoutBasicDetailsContainer.setVisibility(View.VISIBLE);
                    bindingSelectSpouseAlert.buttonSaveAndContinue.setVisibility(View.VISIBLE);
                    bindingSelectSpouseAlert.menLayout.setVisibility(View.VISIBLE);
                    selectedMen = menMemberList.get(position - 1);
                    setHouseHoldMemberView(bindingSelectSpouseAlert.menLayout, selectedMen);
                    if (bindingSelectSpouseAlert.phoneNumber.getText() != null &&
                            bindingSelectSpouseAlert.phoneNumber.getText().length() < 1) {
                        bindingSelectSpouseAlert.phoneNumber.setText(selectedMen.getTargetNode().getProperties().getContact());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bindingSelectSpouseAlert.checkboxRationCardNotAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bindingSelectSpouseAlert.editTextRationCardNumber.setText("");
                    bindingSelectSpouseAlert.editTextRationCardNumber.setEnabled(false);
                    bindingSelectSpouseAlert.editTextRationCardNumber.setBackground(getResources().getDrawable(R.drawable.bg_rounder_corner_half_white));
                } else {
                    bindingSelectSpouseAlert.editTextRationCardNumber.setEnabled(true);
                    bindingSelectSpouseAlert.editTextRationCardNumber.setBackground(getResources().getDrawable(R.drawable.alert_phc_other));
                }
            }
        });

        bindingSelectSpouseAlert.spinnerSelectInfertilityStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 1) {
                    bindingSelectSpouseAlert.layoutInfertilityStatus.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        bindingSelectSpouseAlert.mainScrolview.scrollToDescendant(bindingSelectSpouseAlert.spinnerSelectInfertileReferTo);
                    } else {
                        bindingSelectSpouseAlert.mainScrolview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                } else {
                    bindingSelectSpouseAlert.layoutInfertilityStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Initial view visibility
        bindingSelectSpouseAlert.menLayout.setVisibility(View.GONE);
        bindingSelectSpouseAlert.footerSubmit.setVisibility(View.GONE);
        bindingSelectSpouseAlert.layoutBankDetails.setVisibility(View.GONE);
        bindingSelectSpouseAlert.buttonSaveAndContinue.setVisibility(View.GONE);
        bindingSelectSpouseAlert.layoutBasicDetails.setVisibility(View.VISIBLE);
        bindingSelectSpouseAlert.layoutBasicDetailsContainer.setVisibility(View.GONE);
        bindingSelectSpouseAlert.layoutInfertilityStatus.setVisibility(View.GONE);
        bindingSelectSpouseAlert.textViewAdditionDetails.setText("Additional Details of " + selectedWomen.getTargetNode().getProperties().getFirstName());

        bindingSelectSpouseAlert.phoneNumber.setText(selectedWomen.getTargetNode().getProperties().getContact());

        bindingSelectSpouseAlert.buttonBack.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                bindingSelectSpouseAlert.mainScrolview.scrollToDescendant(bindingSelectSpouseAlert.containerSelectSpouse);
            } else {
                bindingSelectSpouseAlert.mainScrolview.fullScroll(ScrollView.FOCUS_UP);
            }
            bindingSelectSpouseAlert.footerSubmit.setVisibility(View.GONE);
            bindingSelectSpouseAlert.layoutBankDetails.setVisibility(View.GONE);
            bindingSelectSpouseAlert.buttonSaveAndContinue.setVisibility(View.VISIBLE);
            bindingSelectSpouseAlert.layoutBasicDetails.setVisibility(View.VISIBLE);
            bindingSelectSpouseAlert.mandatoryView.setVisibility(View.VISIBLE);
            bindingSelectSpouseAlert.processTitle.setText(getResources().getString(R.string.hh_select_spouse_header_message));
        });

        bindingSelectSpouseAlert.spinnerSelectWomenAgeAtMarriage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = ((TextView) adapterView.getSelectedView()).getText() + "";
                if(!value.equals("Select")){
                int womenAgeAtMarriage = Integer.parseInt(value);
                int husbandAgeAtMarriage = getHusbandAge(selectedWomen.getTargetNode().getProperties().getDateOfBirth(),
                        selectedMen.getTargetNode().getProperties().getDateOfBirth(), womenAgeAtMarriage);
                bindingSelectSpouseAlert.husbandAgeAtMarriage.setText(husbandAgeAtMarriage + "");
            }}

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bindingSelectSpouseAlert.buttonSaveAndContinue.setOnClickListener(v -> {
            if (isBasicDetailsValid(bindingSelectSpouseAlert)) {

                buildBasiDetailsECRequest(bindingSelectSpouseAlert);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    bindingSelectSpouseAlert.mainScrolview.scrollToDescendant(bindingSelectSpouseAlert.editTextRationCardNumber);
                } else {
                    bindingSelectSpouseAlert.mainScrolview.fullScroll(ScrollView.FOCUS_UP);
                }
                bindingSelectSpouseAlert.footerSubmit.setVisibility(View.VISIBLE);
                bindingSelectSpouseAlert.layoutBankDetails.setVisibility(View.VISIBLE);
                bindingSelectSpouseAlert.buttonSaveAndContinue.setVisibility(View.GONE);
                bindingSelectSpouseAlert.layoutBasicDetails.setVisibility(View.GONE);
                bindingSelectSpouseAlert.mandatoryView.setVisibility(View.GONE);
                bindingSelectSpouseAlert.processTitle.setText(getResources().getString(R.string.hh_collect_ank_details));
            }
        });

        String dividerString = " ";
        bindingSelectSpouseAlert.editTextWifeAadharEnrollmentNumber.addTextChangedListener(getTextChangeListener(Arrays.asList(4, 10), dividerString));
        bindingSelectSpouseAlert.editTextHusbandAadharEnrollmentNumber.addTextChangedListener(getTextChangeListener(Arrays.asList(4, 10), dividerString));
        bindingSelectSpouseAlert.editTextWifeAadharNumber.addTextChangedListener(getTextChangeListener(Arrays.asList(4, 9), dividerString));
        bindingSelectSpouseAlert.editTextHusbandAadharNumber.addTextChangedListener(getTextChangeListener(Arrays.asList(4, 9), dividerString));

        bindingSelectSpouseAlert.buttonGenerateRchId.setOnClickListener(v -> {
            dialog.dismiss();
            buildBankDetailsECRequest(bindingSelectSpouseAlert);
            // TODO showSaveContinueAlert();
            showEnterRchIDAlert();
        });

        bindingSelectSpouseAlert.buttonEnterRchId.setOnClickListener(v -> {
            dialog.dismiss();
            buildBankDetailsECRequest(bindingSelectSpouseAlert);
            showEnterRchIDAlert();
        });

    }

    private void getFormUtilityData(DialogueLayoutRmnchaSelectSpouseForEcBinding bindingSelectSpouseAlert) {
        viewModel.getEcUtilityData(AppDefines.EC_REGISTRATION_UTILITY_URL).observe(this, contents -> {
            if (contents != null) {
                for (FormUtilityResponse.SurveyFormResponse content : contents) {
                    if (content.getGroupName().equals("Whose Mobiles Number")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectWhosePhone.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Total number of Children Female")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectFemaleChild.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Total number of Children Male")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectMaleChild.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Number of Children Alive Male")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectMaleChildAlive.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Number of Children Alive Female")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectFemaleChildAlive.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Youngest child age year")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectAgeYear.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Youngest child age Month")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectAgeMonth.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Youngest Child Sex")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectYoungestChildSex.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Wonam Details Age at marriage (in years)")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectWomenAgeAtMarriage.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Infertility status")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectInfertilityStatus.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("If EC is infertile refer to")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectInfertileReferTo.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("If EC is infertile refer to")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectInfertileReferTo.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Bank Name")){
                        ArrayList<String> data = new ArrayList<>();
                        data.add("Select");
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        bindingSelectSpouseAlert.spinnerSelectWifeBankName.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                        bindingSelectSpouseAlert.spinnerSelectHusbandBankBranch.setAdapter(new ArrayAdapter(this, R.layout.layout_rmncha_spinner_textview,data));
                    }
                }
            }
        });
    }
    private boolean isBasicDetailsValid(DialogueLayoutRmnchaSelectSpouseForEcBinding bindingSelectSpouseAlert) {

        int spouseSelection = bindingSelectSpouseAlert.spinnerSelectSpouse.getSelectedItemPosition();
        if (spouseSelection < 1) {
            showMyToast(this, "Select Valid Spouse");
            return false;
        }

        String phoneNumber = bindingSelectSpouseAlert.phoneNumber.getText() + "";
        int whosePhoneSelection = bindingSelectSpouseAlert.spinnerSelectWhosePhone.getSelectedItemPosition();
        if (phoneNumber.length() > 0 && whosePhoneSelection < 1) {
            showMyToast(this, "Select Whose Phone Number");
            return false;
        }

        int ashaSelection = bindingSelectSpouseAlert.spinnerSelectAshaName.getSelectedItemPosition();
        if (ashaSelection < 1) {
            showMyToast(this, "Select Valid Asha Worker Name");
            return false;
        }

        String serialNo = bindingSelectSpouseAlert.editTextSerialNumber.getText() + "";
        if (serialNo.length() < 1) {
            showMyToast(this, "Enter Valid Serial No of EC in RCH Reg");
            return false;
        }

        return true;
    }

    private void buildBasiDetailsECRequest(DialogueLayoutRmnchaSelectSpouseForEcBinding bindingSelectSpouseAlert) {
        int spouseSelection = bindingSelectSpouseAlert.spinnerSelectSpouse.getSelectedItemPosition();
        String husbandAgeAtMarriage = bindingSelectSpouseAlert.husbandAgeAtMarriage.getText() + "";
        String wifeAgeAtMarriage = bindingSelectSpouseAlert.spinnerSelectWomenAgeAtMarriage.getSelectedItem() + "";

        RMNCHAMembersInHouseHoldResponse.Content husband = menMemberList.get(spouseSelection - 1);
        RMNCHAMembersInHouseHoldResponse.Content.TargetNode.TargetProperties husbandProperties = husband.getTargetNode().getProperties();
        RMNCHAECRegistrationRequest.Couple couple = rmnchaECRegistrationRequest.getCouple();
        if (couple == null) couple = new RMNCHAECRegistrationRequest.Couple();
        couple.setHusbandId(husbandProperties.getUuid());
        couple.setHusbandName(husbandProperties.getFirstName());
        couple.setHusbandPhone(husbandProperties.getContact());
        couple.setHusbandAge(husbandProperties.getAge());
        couple.setHusbandDOB(husbandProperties.getDateOfBirth());
        couple.setHusbandHealthId(husbandProperties.getHealthID());
        try {
            couple.setHusbandAgeAtMarriage(Integer.parseInt(husbandAgeAtMarriage));
            couple.setWifeAgeAtMarriage(Integer.parseInt(wifeAgeAtMarriage));
            int maleChild = Integer.parseInt(bindingSelectSpouseAlert.spinnerSelectMaleChild.getSelectedItem() + "");
            if (maleChild > 0)
                couple.setTotalMaleChildren(maleChild);
            int femaleChild = Integer.parseInt(bindingSelectSpouseAlert.spinnerSelectFemaleChild.getSelectedItem() + "");
            if (femaleChild > 0)
                couple.setTotalFemaleChildren(femaleChild);

            int maleChildAlive = Integer.parseInt(bindingSelectSpouseAlert.spinnerSelectMaleChildAlive.getSelectedItem() + "");
            if (maleChildAlive > 0)
                couple.setTotalMaleChildrenAlive(maleChildAlive);

            int femaleChildAlive = Integer.parseInt(bindingSelectSpouseAlert.spinnerSelectFemaleChildAlive.getSelectedItem() + "");
            if (femaleChildAlive > 0)
                couple.setTotalFemaleChildrenAlive(femaleChildAlive);

        }catch (Exception exception){
            exception.printStackTrace();
        }
        RMNCHAMembersInHouseHoldResponse.Content.TargetNode.TargetProperties wifeProperties = selectedWomen.getTargetNode().getProperties();
        couple.setWifeId(wifeProperties.getUuid());
        couple.setWifeName(wifeProperties.getFirstName());
        couple.setWifePhone(wifeProperties.getContact());
        couple.setWifeAge(wifeProperties.getAge());
        couple.setWifeDOB(wifeProperties.getDateOfBirth());
        couple.setWifeHealthId(wifeProperties.getHealthID());
        String religion = bindingSelectSpouseAlert.religion.getText() + "";
        if (religion.length() > 0)
            couple.setReligion(religion);
        String caste = bindingSelectSpouseAlert.caste.getText() + "";
        if (caste.length() > 0)
            couple.setCaste(caste);
        String economicStatus = bindingSelectSpouseAlert.economicStatus.getText() + "";
        if (economicStatus.length() > 0)
            couple.setEconomicStatus(economicStatus);
        String infertilityStatus = bindingSelectSpouseAlert.spinnerSelectInfertilityStatus.getSelectedItem() + "";
        couple.setInfertilityStatus(infertilityStatus);

        if ("Yes".equalsIgnoreCase(infertilityStatus)) {
            // TODO need to add below
            String ecInfer = bindingSelectSpouseAlert.spinnerSelectInfertileReferTo.getSelectedItem() + "";
            //couple.setInfertilityStatusReferTo(ecInfer);
            if (ecInfer.contains("any")) {
                String referText = bindingSelectSpouseAlert.infertilityReferText.getText() + "";
                if (referText.length() > 0) {
                    //couple.setInfertilityStatusReferTo(ecInfer);
                }
            }
        }
        AshaWorkerResponse.Content ashaWorker = ashaWorkersList.get(bindingSelectSpouseAlert.spinnerSelectAshaName.getSelectedItemPosition() - 1);
        couple.setRegisteredBy(ashaWorker.getTargetNode().getProperties().getName());
        couple.setRegisteredByName(ashaWorker.getTargetNode().getProperties().getName());
        couple.setRegisteredOn(getCurrentDate());
        rmnchaECRegistrationRequest.setCouple(couple);
    }

    private void buildBankDetailsECRequest(DialogueLayoutRmnchaSelectSpouseForEcBinding bindingSelectSpouseAlert) {

        RMNCHAECRegistrationRequest.CoupleAdditionalDetails coupleAdditionalDetails = rmnchaECRegistrationRequest.getCoupleAdditionalDetails();
        if (coupleAdditionalDetails == null)
            coupleAdditionalDetails = new RMNCHAECRegistrationRequest.CoupleAdditionalDetails();

        String rationCard = bindingSelectSpouseAlert.editTextRationCardNumber.getText() + "";
        if (rationCard.length() > 0)
            coupleAdditionalDetails.setRationCardNo(rationCard);

        String husbandAadharEnrollmentNo = bindingSelectSpouseAlert.editTextHusbandAadharEnrollmentNumber.getText() + "";
        if (husbandAadharEnrollmentNo.length() > 0)
            coupleAdditionalDetails.setHusbandAadharEnrollmentNo(husbandAadharEnrollmentNo);

        String husbandAadharNo = bindingSelectSpouseAlert.editTextHusbandAadharNumber.getText() + "";
        if (husbandAadharNo.length() > 0)
            coupleAdditionalDetails.setHusbandAadharNo(husbandAadharNo);

        boolean husbandAadharLinkedYes = bindingSelectSpouseAlert.radioHusbandAadhaarLinked.isChecked();
        boolean husbandAadharLinkedNo = bindingSelectSpouseAlert.radioHusbandAadhaarNotLinked.isChecked();
        if (husbandAadharLinkedYes)
            coupleAdditionalDetails.setHusbandBankAadharLinked(true);
        if (husbandAadharLinkedNo)
            coupleAdditionalDetails.setHusbandBankAadharLinked(false);

        String husbandBankName = bindingSelectSpouseAlert.spinnerSelectHusbandBankName.getSelectedItem() + "";
        if (husbandBankName.length() > 1)
            coupleAdditionalDetails.setHusbandBankName(husbandBankName);

        String husbandBranch = bindingSelectSpouseAlert.spinnerSelectHusbandBankBranch.getSelectedItem() + "";
        if (husbandBranch.length() > 0)
            coupleAdditionalDetails.setHusbandBankBranch(husbandBranch);

        String husbandAccNo = bindingSelectSpouseAlert.editTextHusbandAccountNumber.getText() + "";
        if (husbandAccNo.length() > 0)
            coupleAdditionalDetails.setHusbandBankACNo(husbandAccNo);

        String husbandIfsc = bindingSelectSpouseAlert.editTextHusbandIfsc.getText() + "";
        if (husbandIfsc.length() > 0)
            coupleAdditionalDetails.setHusbandBankIFSCCode(husbandIfsc);

        String wifeAadharEnrollmentNo = bindingSelectSpouseAlert.editTextWifeAadharEnrollmentNumber.getText() + "";
        if (wifeAadharEnrollmentNo.length() > 0)
            coupleAdditionalDetails.setWifeAadharEnrollmentNo(wifeAadharEnrollmentNo);

        String wifeAadharNo = bindingSelectSpouseAlert.editTextWifeAadharNumber.getText() + "";
        if (wifeAadharNo.length() > 0)
            coupleAdditionalDetails.setWifeAadharNo(wifeAadharNo);

        boolean wifeAadharLinkedYes = bindingSelectSpouseAlert.radioWifeAadhaarLinked.isChecked();
        boolean wifeAadharLinkedNo = bindingSelectSpouseAlert.radioWifeAadhaarNotLinked.isChecked();
        if (wifeAadharLinkedYes)
            coupleAdditionalDetails.setWifeBankAadharLinked(true);
        if (wifeAadharLinkedNo)
            coupleAdditionalDetails.setWifeBankAadharLinked(false);

        String wifeBankName = bindingSelectSpouseAlert.spinnerSelectWifeBankName.getSelectedItem() + "";
        if (wifeBankName.length() > 1)
            coupleAdditionalDetails.setWifeBankName(wifeBankName);

        String wifeBranchName = bindingSelectSpouseAlert.spinnerSelectWifeBankBranch.getSelectedItem() + "";
        if (wifeBranchName.length() > 1)
            coupleAdditionalDetails.setWifeBankBranch(wifeBranchName);

        String wifeAccNo = bindingSelectSpouseAlert.editTextWifeAccountNumber.getText() + "";
        if (wifeAccNo.length() > 0)
            coupleAdditionalDetails.setWifeBankACNo(wifeAccNo);

        String wifeIfsc = bindingSelectSpouseAlert.editTextWifeIfsc.getText() + "";
        if (wifeIfsc.length() > 0)
            coupleAdditionalDetails.setWifeBankIFSCCode(wifeIfsc);

        AshaWorkerResponse.Content ashaWorker = ashaWorkersList.get(bindingSelectSpouseAlert.spinnerSelectAshaName.getSelectedItemPosition() - 1);
        coupleAdditionalDetails.setAddedBy(ashaWorker.getTargetNode().getProperties().getName());
        coupleAdditionalDetails.setAddedOn(getCurrentDate());

        rmnchaECRegistrationRequest.setCoupleAdditionalDetails(coupleAdditionalDetails);

    }

    private List<RMNCHAMembersInHouseHoldResponse.Content> getAllMenContentList() {
        List<RMNCHAMembersInHouseHoldResponse.Content> list = new ArrayList<>();
        for (RMNCHAMembersInHouseHoldResponse.Content member : allMemberList) {
            RMNCHAMembersInHouseHoldResponse.Content.TargetNode.TargetProperties targetProperties = member.getTargetNode().getProperties();
            String gender = targetProperties.getGender();
            int age = targetProperties.getAge() != null ? targetProperties.getAge() : 0;
            if ("male".equalsIgnoreCase(gender) && age > 14) {
                list.add(member);
            }
        }
        return list;
    }

            private String getHeadName() {
        String headName = null;
        for (RMNCHAMembersInHouseHoldResponse.Content member : allMemberList) {
            RMNCHAMembersInHouseHoldResponse.Content.TargetNode.TargetProperties targetProperties = member.getTargetNode().getProperties();
            if (targetProperties.isHead()) {
                headName = targetProperties.getFirstName();
            }
        }
        return setNonNullValue(headName);
    }

    private ArrayList<String> getAllMenNamesList() {
        ArrayList<String> menList = new ArrayList<>();
        menList.add("Select");
        for (RMNCHAMembersInHouseHoldResponse.Content member : menMemberList) {
            RMNCHAMembersInHouseHoldResponse.Content.TargetNode.TargetProperties targetProperties = member.getTargetNode().getProperties();
            String item = targetProperties.getFirstName() + " - " + targetProperties.getAge() + "y -" + targetProperties.getGender();
            if (targetProperties.isHead()) {
                item += " (H)";
            }
            menList.add(item);
        }
        return menList;
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
