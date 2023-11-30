package com.ssf.homevisit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.FragmentVillageLevelSocioEcoSurveyBinding;
import com.ssf.homevisit.factories.VillageSESurveyViewModelFactory;
import com.ssf.homevisit.models.SurveyFilterResponse;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.viewmodel.VillageSESurveyViewModel;
import com.ssf.homevisit.views.SESQuestionResponseCheckBox;
import com.ssf.homevisit.views.SESQuestionResponseView;

public class VillageLevelSocioEcoSurveyFragment extends Fragment {

    private VillageSESurveyViewModel villageSESurveyViewModel;
    private FragmentVillageLevelSocioEcoSurveyBinding binding;
    private VillageProperties villageProperties;

    private int questionIndexToRId(int qestionIndex) {
        switch (qestionIndex) {
            case 0:
                return R.id.water_quality;
            case 1:
                return R.id.air_quality;
            case 2:
                return R.id.drainage_system;
            case 3:
                return R.id.waste_disposal;
            case 4:
                return R.id.source_of_pollution;
            case 5:
                return 0;
            case 6:
                return R.id.water_source_for_village;
            case 7:
                return R.id.piped_water;
            case 8:
                return R.id.well___;
            case 9:
                return R.id.tank___;
            case 10:
                return R.id.tube_well___;
            case 11:
                return R.id.hand_pump___;
            case 12:
                return R.id.canal___;
            case 13:
                return R.id.river___;
            case 14:
                return R.id.lake___;
            case 15:
                return R.id.watershed___;
            case 16:
                return R.id.others___;
            case 17:
                return 0;
            case 18:
                return 0;
            case 19:
                return 0;
            case 20:
                return 0;

        }
        return 0;
    }

    private int questionIndexToRId1(int qestionIndex) {
        switch (qestionIndex) {
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 0;
            case 3:
                return 0;
            case 4:
                return 0;
            case 5:
                return R.id.is_there_any_open_defecation;
            case 6:
                return 0;
            case 7:
                return 0;
            case 8:
                return 0;
            case 9:
                return 0;
            case 10:
                return 0;
            case 11:
                return 0;
            case 12:
                return 0;
            case 13:
                return 0;
            case 14:
                return 0;
            case 15:
                return 0;
            case 16:
                return 0;
            case 17:
                return 0;
            case 18:
                return R.id.is_the_village_connected_by_a_pukka_road;
            case 19:
                return 0;
            case 20:
                return R.id.panchayatbhawan;
                case 21:
                return R.id.library_readingroom;
            case 22:
                return R.id.public_playground;
                case 23:
                return R.id.park___;
            case 24:
                return R.id.public_distribution_system;
            case 25:
                return R.id.forrest___;
            case 26:
                return R.id.aganwadi___;
            case 27:
                return R.id.healthsc_phc___;
            case 28:
                return R.id.primary_school___;
            case 29:
                return R.id.high_school___;
            case 30:
                return R.id.college___;
            case 31:
                return R.id.primary_agriculture_society__;
            case 32:
                return R.id.cooperation_marketing_society;
            case 33:
                return R.id.dairy_pollutry_fisheries___;
            case 34:
                return R.id.banks_rural___;
            case 35:
                return R.id.does_the_village;
            case 36:
                return R.id.any_other___;

        }
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_village_level_socio_eco_survey, container, false);
        View view = binding.getRoot();

        villageSESurveyViewModel = new ViewModelProvider(
                this,
                new VillageSESurveyViewModelFactory(this.getActivity().getApplication(), villageProperties)).get(VillageSESurveyViewModel.class);
        addObserver();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        villageSESurveyViewModel.getSurveyMaster();
    }

    public void setVillageProperties(VillageProperties villageProperties) {
        this.villageProperties = villageProperties;

    }

    private void addObserver() {
        villageSESurveyViewModel.getSurveyMasterResponse().observe(getViewLifecycleOwner(), new Observer<SurveyFilterResponse>() {
            @Override
            public void onChanged(SurveyFilterResponse surveyFilterResponse) {
                if (surveyFilterResponse == null) {
                    UIController.getInstance().displayToastMessage(getContext(), "Cannot get Questions");
                }
                int index = 0;
                for (SurveyFilterResponse.Datum.QuesOption quesOption : surveyFilterResponse.getData().get(0).getQuesOptions()) {
                    int rId = questionIndexToRId(index);
                    index++;
                    if (rId == 0) continue;
                    SESQuestionResponseView sesQuestionResponseView = binding.getRoot().findViewById(rId);
                    sesQuestionResponseView.setQuestion(quesOption.getQuestion());
                    sesQuestionResponseView.setOptions(quesOption.getChoices());
                                  }

                int index1=0;
                for (SurveyFilterResponse.Datum.QuesOption quesOption : surveyFilterResponse.getData().get(0).getQuesOptions()) {
                    int rId1 = questionIndexToRId1(index1);
                    index1++;
                    if (rId1 == 0) continue;
                     SESQuestionResponseCheckBox sesQuestionResponseCheckBox = binding.getRoot().findViewById(rId1);
                    sesQuestionResponseCheckBox.setQuestion(quesOption.getQuestion());
                }
            }
        });
    }

}