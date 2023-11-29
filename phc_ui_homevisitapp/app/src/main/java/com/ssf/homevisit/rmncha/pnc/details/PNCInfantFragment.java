package com.ssf.homevisit.rmncha.pnc.details;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.getDateFromDatePicker;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.FormUtilityResponse;
import com.ssf.homevisit.models.RMNCHAPNCDeliveryOutcomesResponse;
import com.ssf.homevisit.models.RMNCHAPNCInfantRequest;
import com.ssf.homevisit.models.RMNCHAPNCInfantResponse;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.base.RMNCHAUtils;

import java.util.ArrayList;

public class PNCInfantFragment extends Fragment {

    int currentInfantPosition = 0;
    int infantCount = 0;
    private PNCDetailsViewModel viewModel;
    public static final String ARG_VIEW_ONLY = "viewOnly";
    public static final String ARG_POSITION = "position";
    public static final String ARG_TOTAL = "total";
    private String motherName;
    private String pncRegistrationId;
    private PNCInfantFragment.PNCInfantFragmentClickListener listener;
    private View fragmentView;
    private TextView ancVisitTextView;
    private TextView lmpTextView;
    private TextView eddTextView;
    private TextView registrationTextView;
    private TextView dobTextView;
    private TextView opvTextView;
    private TextView bcgTextView;
    private TextView hepTextView;
    private TextView vitTextView;
    private boolean isViewOnly = false;
    private TextView babyTitle;
    private TextView previousButton;
    private TextView nextButton;
    private ImageButton submitButton;
    private EditText childIDEditText;
    private TextView financialYearTextView;
    private TextView infantTermTextView;
    private Spinner infantGenderSpinner;
    private Spinner anyDefectsSpinner;
    private EditText infantWeightEditText;
    private Spinner didBabyCrySpinner;
    private Spinner resuscitationDoneSpinner;
    private Spinner feedingStartedSpinner;
    private Spinner referredSpinner;
    private RMNCHAPNCInfantResponse.Infant infantData;
    private RMNCHAPNCDeliveryOutcomesResponse deliveryOutcomesResponse;
    private String eddDate;
    private String dob;
    private String lastANCVisitDate;
    private String financialYear;
    private String lmpDate;

    public void setPNCInfantFragmentClickListener(String pncRegistrationId, RMNCHAPNCDeliveryOutcomesResponse deliveryOutcomesResponse, PNCInfantFragment.PNCInfantFragmentClickListener listener) {
        this.pncRegistrationId = pncRegistrationId;
        this.deliveryOutcomesResponse = deliveryOutcomesResponse;
        this.listener = listener;
        if (deliveryOutcomesResponse != null) {
            RMNCHAPNCDeliveryOutcomesResponse.Couple coupleData = deliveryOutcomesResponse.getCouple();
            RMNCHAPNCDeliveryOutcomesResponse.MensuralPeriod menstrualPeriod = deliveryOutcomesResponse.getMenstrualPeriod();
            RMNCHAPNCDeliveryOutcomesResponse.DeliveryDetails deliveryDetails = deliveryOutcomesResponse.getDeliveryDetails();
            if (coupleData != null) {
                this.motherName = coupleData.getWifeName();
                this.lastANCVisitDate = RMNCHAUtils.getDateToView(coupleData.getLastANCVisitDate(), RMNCHAUtils.RMNCHA_DATE_FORMAT);
            }
            if (menstrualPeriod != null) {
                this.lmpDate = menstrualPeriod.getLmpDate();
                this.eddDate = menstrualPeriod.getEddDate();
            }
            if (deliveryDetails != null) {
                this.dob = RMNCHAUtils.getDateToView(deliveryDetails.getDeliveryDate(), RMNCHAUtils.RMNCHA_DATE_FORMAT);
                this.financialYear = deliveryDetails.getFinancialYear();
            }
        }
    }

    public void setInfantData(RMNCHAPNCInfantResponse.Infant infantData, String lmpDate, String eddDate, String lastANCVisitDate) {
        this.infantData = infantData;
        this.lmpDate = lmpDate;
        this.eddDate = eddDate;
        this.lastANCVisitDate = RMNCHAUtils.getDateToView(lastANCVisitDate, RMNCHAUtils.RMNCHA_DATE_FORMAT);
        if (infantData != null) {
            this.dob = RMNCHAUtils.getDateToView(infantData.getDateOfBirth(), RMNCHAUtils.RMNCHA_DATE_FORMAT);
            this.financialYear = infantData.getFinancialYear();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.layout_rmncha_infant_details, container, false);
        return fragmentView;
    }
    void getFormUtilityForInfant() {
        viewModel.getEcUtilityData(AppDefines.PNC_INFANT_UTILITY_URL).observe(getViewLifecycleOwner(), contents -> {
            if (contents != null) {
                for (FormUtilityResponse.SurveyFormResponse content : contents) {
                    if (content.getGroupName().equals("Sex of Infant*")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        infantGenderSpinner.setAdapter(new ArrayAdapter(this.getContext(),R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Any defect seen at birth")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        anyDefectsSpinner.setAdapter(new ArrayAdapter(this.getContext(), R.layout.layout_rmncha_spinner_textview,data));
                    }

                    if (content.getGroupName().equals("Did the baby cry immediately after birth? *")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        didBabyCrySpinner.setAdapter(new ArrayAdapter(this.getContext(), R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Was resuscitation done?")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        resuscitationDoneSpinner.setAdapter(new ArrayAdapter(this.getContext(), R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Breast feeding started with 1 hour of delivery?")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        feedingStartedSpinner.setAdapter(new ArrayAdapter(this.getContext(), R.layout.layout_rmncha_spinner_textview,data));
                    }
                    if (content.getGroupName().equals("Referred to higher facility for further management")){
                        ArrayList<String> data = new ArrayList<>();
                        for (FormUtilityResponse.Options questions :content.getQuesOptions()){
                            data.add(questions.getTitle());
                        }
                        referredSpinner.setAdapter(new ArrayAdapter(this.getContext(), R.layout.layout_rmncha_spinner_textview,data));
                    }
                }
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            isViewOnly = getArguments().getBoolean(ARG_VIEW_ONLY, false);
            currentInfantPosition = getArguments().getInt(ARG_POSITION, 0) + 1;
            infantCount = getArguments().getInt(ARG_TOTAL, 0);
        }
        viewModel = new ViewModelProvider(this).get(PNCDetailsViewModel.class);
        getFormUtilityForInfant();
        babyTitle = view.findViewById(R.id.baby_title);
        previousButton = view.findViewById(R.id.button_previous);
        nextButton = view.findViewById(R.id.button_next);
        submitButton = view.findViewById(R.id.button_submit);
        childIDEditText = view.findViewById(R.id.edit_text_child_id);
        financialYearTextView = view.findViewById(R.id.text_view_financial_year);
        infantTermTextView = view.findViewById(R.id.text_view_infant_term);
        infantWeightEditText = view.findViewById(R.id.infant_weight);
        ancVisitTextView = view.findViewById(R.id.text_view_anc_visit_date);
        lmpTextView = view.findViewById(R.id.text_view_lmp_date);
        eddTextView = view.findViewById(R.id.text_view_edd_date);
        registrationTextView = view.findViewById(R.id.text_view_registration_date);
        dobTextView = view.findViewById(R.id.text_view_dob);
        opvTextView = view.findViewById(R.id.text_view_opv_date);
        bcgTextView = view.findViewById(R.id.text_view_bcg_date);
        hepTextView = view.findViewById(R.id.text_view_hep_date);
        vitTextView = view.findViewById(R.id.text_view_vit_date);
        infantGenderSpinner = view.findViewById(R.id.spinner_infant_sex);
        anyDefectsSpinner = view.findViewById(R.id.spinner_any_defect);
        didBabyCrySpinner = view.findViewById(R.id.spinner_did_baby_cry);
        didBabyCrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String didBabyCry = didBabyCrySpinner.getSelectedItem() + "";
                if (didBabyCry.equalsIgnoreCase("No")) {
                    resuscitationDoneSpinner.setVisibility(View.VISIBLE);
                }else{
                    resuscitationDoneSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        resuscitationDoneSpinner = view.findViewById(R.id.spinner_resuscitation_done);
        resuscitationDoneSpinner.setVisibility(View.GONE);
        feedingStartedSpinner = view.findViewById(R.id.spinner_feeding_started);
        referredSpinner = view.findViewById(R.id.spinner_referred);
        submitButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        previousButton.setVisibility(View.GONE);
        babyTitle.setVisibility(View.GONE);

        setCommonDateToView();

        if (!isViewOnly) {
            bindInfantView();
        } else {
            setInfantDateToView();
        }

    }

    private void setCommonDateToView() {
        ancVisitTextView.setText(lastANCVisitDate);
        dobTextView.setText(dob);
        lmpTextView.setText(lmpDate);
        eddTextView.setText(eddDate);
        financialYearTextView.setText(financialYear);
    }

    private void bindInfantView() {
        babyTitle.setVisibility(View.VISIBLE);
        babyTitle.setText("Baby " + currentInfantPosition + " of " + motherName);

        registrationTextView.setOnClickListener(tv -> getDateFromDatePicker((TextView) tv));
        opvTextView.setOnClickListener(tv -> getDateFromDatePicker((TextView) tv));
        bcgTextView.setOnClickListener(tv -> getDateFromDatePicker((TextView) tv));
        hepTextView.setOnClickListener(tv -> getDateFromDatePicker((TextView) tv));
        vitTextView.setOnClickListener(tv -> getDateFromDatePicker((TextView) tv));

        if (currentInfantPosition == infantCount && getActivity() instanceof PNCDetailsActivity) {
            submitButton.setVisibility(View.VISIBLE);
            submitButton.setOnClickListener(view1 -> {
                RMNCHAPNCInfantRequest.Infant infant = getInfantRequest();
                if (infant != null && listener != null) {
                    listener.onSubmitClicked(infant);
                }
            });
        }

        if (currentInfantPosition < infantCount) {
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setOnClickListener(view1 -> {
                RMNCHAPNCInfantRequest.Infant infant = getInfantRequest();
                if (infant != null && listener != null) {
                    listener.onNextClicked(infant);
                }
            });
        }

        if (currentInfantPosition > 1) {
            previousButton.setVisibility(View.VISIBLE);
            previousButton.setOnClickListener(view1 -> {
                RMNCHAPNCInfantRequest.Infant infant = getInfantRequest();
                if (infant != null && listener != null) {
                    listener.onPreviousClicked(infant);
                }
            });
        }
    }

    public void setInfantDateToView() {

        if (infantData != null) {

            childIDEditText.setText(infantData.getChildId() + "");
            childIDEditText.setEnabled(false);
            ancVisitTextView.setText("");//TODO
            lmpTextView.setText("");// TODO
            eddTextView.setText("");//TODO

            registrationTextView.setText(infantData.getRegistrationDate());
            dobTextView.setText(infantData.getDateOfBirth());
            financialYearTextView.setText(infantData.getDateOfBirth());
            infantTermTextView.setText(infantData.getInfantTerm());

            infantWeightEditText.setEnabled(false);
            infantWeightEditText.setText(infantData.getBirthWeight() + "");

            infantGenderSpinner.setEnabled(false);
            infantGenderSpinner.setSelection(getPositionFor(infantGenderSpinner, infantData.getGender()));

            anyDefectsSpinner.setEnabled(false);
            anyDefectsSpinner.setSelection(getPositionFor(anyDefectsSpinner, infantData.getDefectAtBirth()));

            didBabyCrySpinner.setEnabled(false);
            didBabyCrySpinner.setSelection(getPositionFor(didBabyCrySpinner, infantData.isDidBabyCryAtBirth()));

            int resuscitationDone = 0;
            if (infantData.isWasResuscitationDone()) {
                resuscitationDone = 1;
            } else {
                resuscitationDone = 2;
            }
            resuscitationDoneSpinner.setEnabled(false);
            resuscitationDoneSpinner.setSelection(resuscitationDone);

            int feedingStarted = 0;
            if (infantData.isDidBreastFeedingStartWithin1Hr()) {
                feedingStarted = 1;
            } else {
                feedingStarted = 2;
            }
            feedingStartedSpinner.setEnabled(false);
            feedingStartedSpinner.setSelection(feedingStarted);

            referredSpinner.setEnabled(false);
            referredSpinner.setSelection(getPositionFor(referredSpinner, infantData.isReferredToHigherFacility()));

            RMNCHAPNCInfantResponse.Infant.Immunization immunization = infantData.getImmunization();
            if (immunization != null) {
                opvTextView.setText(immunization.getOpv0DoseDate());
                bcgTextView.setText(immunization.getBcgDoseDate());
                hepTextView.setText(immunization.getHepB0DoseDate());
                vitTextView.setText(immunization.getVitKDoseDate());
            }
        }

    }

    private int getPositionFor(Spinner spinner, String value) {
        Integer position= spinner.getSelectedItemPosition();
        if(position==-1){
            return 1;
        }
        return position;
    }

    private RMNCHAPNCInfantRequest.Infant getInfantRequest() {
        if (fragmentView != null) {
            String ancVisitData = (ancVisitTextView.getText() + "").replace("/", "-");
            String lmpData = (lmpTextView.getText() + "").replace("/", "-");
            String eddData = (eddTextView.getText() + "").replace("/", "-");
            String registrationDate = (registrationTextView.getText() + "").replace("/", "-");
            String dob = (dobTextView.getText() + "").replace("/", "-");
            String financialYear = financialYearTextView.getText() + "";
            String infantTerm = infantTermTextView.getText() + "";

            String childID = childIDEditText.getText() + "";
            if (childID.length() != 12) {
                showMyToast(getContext(), "Enter Valid 12 Digit Child ID");
                return null;
            }

            String infantSex = infantGenderSpinner.getSelectedItem() + "";
            if (infantSex.equalsIgnoreCase("select")) {
                showMyToast(getContext(), "Select Infant Sex");
                return null;
            }

            String infantWeight = infantWeightEditText.getText() + "";
            if (infantWeight.length() < 1) {
                showMyToast(getContext(), "Enter Infant Weight");
                return null;
            }

            String defects = anyDefectsSpinner.getSelectedItem() + "";
            String didBabyCry = didBabyCrySpinner.getSelectedItem() + "";
            if (didBabyCry.equalsIgnoreCase("select")) {
                showMyToast(getContext(), "Select Did baby cry");
                return null;
            }

            boolean wasResuscitationDone = resuscitationDoneSpinner.getSelectedItemPosition() == 1;
            boolean feedingStarted = feedingStartedSpinner.getSelectedItemPosition() == 1;

            String referred = referredSpinner.getSelectedItem() + "";
            String opvDate = (opvTextView.getText() + "").replace("/", "-");
            String bcgDate = (bcgTextView.getText() + "").replace("/", "-");
            String hepDate = (hepTextView.getText() + "").replace("/", "-");
            String vitDate = (vitTextView.getText() + "").replace("/", "-");

            RMNCHAPNCInfantRequest.Infant infant = new RMNCHAPNCInfantRequest.Infant();
            infant.setChildId(childID);
            infant.setPncRegistrationId(pncRegistrationId);
            infant.setRegistrationDate(registrationDate);
            infant.setWasResuscitationDone(wasResuscitationDone+"");
            infant.setDateOfBirth(dob);
            infant.setFinancialYear(financialYear);
            infant.setInfantTerm(infantTerm);
            infant.setGender(infantSex);
            infant.setBirthWeight(Double.parseDouble(infantWeight));
            infant.setDefectAtBirth(defects);
            infant.setDidBabyCryAtBirth(didBabyCry);
            infant.setDidBreastFeedingStartWithin1Hr(feedingStarted);
            infant.setReferredToHigherFacility(referred);

            RMNCHAPNCInfantRequest.Infant.Immunization immunization = new RMNCHAPNCInfantRequest.Infant.Immunization();
            immunization.setOpv0DoseDate(opvDate);
            immunization.setBcgDoseDate(bcgDate);
            immunization.setHepB0DoseDate(hepDate);
            immunization.setVitKDoseDate(vitDate);
            infant.setImmunization(immunization);

            return infant;
        }
        return null;
    }

    public interface PNCInfantFragmentClickListener {

        void onSubmitClicked(RMNCHAPNCInfantRequest.Infant infant);

        void onNextClicked(RMNCHAPNCInfantRequest.Infant infant);

        void onPreviousClicked(RMNCHAPNCInfantRequest.Infant infant);

    }

}