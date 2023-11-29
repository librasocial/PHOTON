package com.ssf.homevisit.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.ssf.homevisit.R;
import com.ssf.homevisit.activity.ArsActivity;
import com.ssf.homevisit.activity.MainActivity;
import com.ssf.homevisit.alerts.CommonAlert;
import com.ssf.homevisit.app.AndroidApplication;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.FragmentLandingBinding;
import com.ssf.homevisit.models.AllPhcResponse;
import com.ssf.homevisit.models.Content;
import com.ssf.homevisit.models.SubCVillResponse;
import com.ssf.homevisit.models.SubCenterResponse;
import com.ssf.homevisit.models.SurveyFilterResponse;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.adolescentCare.AdolescentCareListFragment;
import com.ssf.homevisit.rmncha.anc.ancalert.ANCAlert;
import com.ssf.homevisit.rmncha.childCare.mapping.ChildMappingListFragment;
import com.ssf.homevisit.rmncha.childCare.popup.VillageSelectionDialog;
import com.ssf.homevisit.rmncha.ec.ecalert.ECAlert;
import com.ssf.homevisit.rmncha.pnc.pncalert.PNCAlert;
import com.ssf.homevisit.utils.Util;
import com.ssf.homevisit.viewmodel.MappingViewModel;

import java.util.List;

public class LandingFragment extends Fragment implements View.OnClickListener {

    FragmentLandingBinding binding;
    private View view;

    private MappingViewModel mappingViewModel;
    private AllPhcResponse allPhcResponse;
    private SubCenterResponse subCenterResponse;
    private SubCVillResponse subCVillResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_landing, container, false);
        view = binding.getRoot();
        initialisation();
        TextView textView = getActivity().findViewById(R.id.ussrname);
        textView.setText(AndroidApplication.prefs.getString(AppDefines.USER_NAME, ""));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarVisibility(View.VISIBLE);
    }

    private void initialisation() {
        mappingViewModel = new ViewModelProvider(this).get(MappingViewModel.class);
        binding.setViewModel(mappingViewModel);
        String uuid = Util.getUuid();
        if (uuid != null) {
            binding.sampLogo.setText(Html.fromHtml(getResources().getString(R.string.powered_by_)));
            mappingViewModel.getAllPhcLiveData(uuid).observe(AppController.getInstance().getMainActivity(), response -> {
                if (response != null) {
                    allPhcResponse = response;
                    List<AllPhcResponse.Content> contentList = allPhcResponse.getContent();
                    for (AllPhcResponse.Content content : contentList) {
                        try {
                            TextView textView = getActivity().findViewById(R.id.ussrname);
                            textView.setText(AndroidApplication.prefs.getString(AppDefines.USER_NAME, ""));
                            ((TextView) getActivity().findViewById(R.id.phc_name_text)).setText(content.getProperties().getPhc());
                        } catch (NullPointerException n) {
                            Log.e("NullPointer while recreating all PHC TextView", n.getMessage());
                            n.printStackTrace();
                        }
                    }
                } else {
                }
            });
            mappingViewModel.getSubCenterLiveData().observe(AppController.getInstance().getMainActivity(), response -> {
                if (response != null) subCenterResponse = response;
                else{
                }
            });
            mappingViewModel.getSubCenterVillLiveData().observe(AppController.getInstance().getMainActivity(), response -> {
                if (response != null) subCVillResponse = response;
                else{
                }
            });
        }
        binding.llVillageMap.setOnClickListener(this);
        binding.llHouseMap.setOnClickListener(this);
        binding.llvillagesociosrv.setOnClickListener(this);
        binding.LLHSESurvey.setOnClickListener(this);
        binding.LLISESurvey.setOnClickListener(this);
        binding.hnwsVillage.setOnClickListener(this);
        binding.hnwsHouseHold.setOnClickListener(this);
        binding.hnwsIndividual.setOnClickListener(this);
        binding.eligibleCouple.setOnClickListener(this);
        binding.childCare.setOnClickListener(this);
        binding.adolescentCare.setOnClickListener(this);
        binding.anc.setOnClickListener(this);
        binding.pnc.setOnClickListener(this);
        binding.vhsnc.setOnClickListener(this);
        binding.ars.setOnClickListener(this);
        binding.pulsePolio.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.llVillageMap:
                new CommonAlert(getActivity()).displayMappingAlert(AppDefines.VILLAGE_LEVEL_MAPPING);
                break;
            case R.id.llHouseMap:
                new CommonAlert(getActivity()).displayMappingAlert(AppDefines.HOUSE_LEVEL_MAPPING);
                break;
            case R.id.LLHSE_survey:
                new CommonAlert(getActivity()).displayMappingAlert(AppDefines.HOUSEHOLD_SE_SURVEY);
                break;
            case R.id.llvillagesociosrv:
                new CommonAlert(getActivity()).displayMappingAlert(AppDefines.VILLAGE_LEVEL_SOCIO_ECONOMIC_SURVEY);
                break;
            case R.id.LLISE_survey:
                new CommonAlert(getActivity()).displayMappingAlert(AppDefines.INDIVIDUAL_SE_SURVEY);
                break;
            case R.id.eligibleCouple:
                    new ECAlert(getActivity()).displayECMappingAlert();
                break;
            case R.id.childCare:
                new VillageSelectionDialog(getActivity()).display((phc, subCenter, village) -> {
                    ChildMappingListFragment fragment = ChildMappingListFragment.Companion.getInstance(phc, subCenter, village);
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_landing, fragment).addToBackStack(null).commit();
                    return null;
                });
                break;
            case R.id.adolescentCare:
                new VillageSelectionDialog(getActivity()).display((phc, subCenter, village) -> {
                    AdolescentCareListFragment fragment = AdolescentCareListFragment.Companion.getInstance(phc, subCenter, village);
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_landing, fragment).addToBackStack(null).commit();
                    return null;
                });
                break;
            case R.id.pulsePolio:
                break;
            case R.id.vhsnc:
                if (allPhcResponse != null)
                    new CommonAlert(getActivity()).displayRMNCHAMappingAlert(AppDefines.VHSNC, allPhcResponse);
                break;
            case R.id.ars:
                startActivity(ArsActivity.Companion.newIntent(requireContext()));
                break;
            case R.id.hnws_village:
                new CommonAlert(getActivity()).displayMappingAlert(AppDefines.HNW_VILLAGE_SURVEILLANCE);
                break;
            case R.id.anc:
                    new ANCAlert(getActivity()).displayANCMappingAlert();
                break;
            case R.id.pnc:
                    new PNCAlert(getActivity()).displayPNCMappingAlert();
                break;

            case R.id.hnws_house_hold:
                new CommonAlert(getActivity()).displayMappingAlert(AppDefines.HNW_HOUSEHOLD_SURVEILLANCE);
                break;
            case R.id.hnws_individual:
                new CommonAlert(getActivity()).displayMappingAlert(AppDefines.HNW_INDIVIDUAL_SURVEILLANCE);
                break;
        }
    }
}

