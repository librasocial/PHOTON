package com.ssf.homevisit.rmncha.pnc.pncalert;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.databinding.AlertLayoutRmnchMappingBinding;
import com.ssf.homevisit.models.PhcResponse;
import com.ssf.homevisit.models.SubCVillResponse;
import com.ssf.homevisit.models.SubcentersFromPHCResponse;
import com.ssf.homevisit.rmncha.pnc.mapping.PNCMappingFragment;

import java.util.ArrayList;
import java.util.List;

public class PNCAlert {

    private List<PhcResponse.Content> rmnchaPhcCenters = new ArrayList<>();
    private List<SubcentersFromPHCResponse.Content> rmnchaSubcenters = new ArrayList<>();
    private List<SubCVillResponse.Content> rmnchaVillageCenters = new ArrayList<>();
    private List<String> rmnchaPhcCenterNames = new ArrayList<>();
    private List<String> rmnchaPhcSubCenterNames = new ArrayList<>();
    private List<String> rmnchaPhcVillageNames = new ArrayList<>();
    private LiveData<PhcResponse> rmnchaPhcLiveData = new MutableLiveData<>();
    private LiveData<SubcentersFromPHCResponse> rmnchaSubCenterLiveData = new MutableLiveData<>();
    private LiveData<SubCVillResponse> rmnchaVillageLiveData = new MutableLiveData<>();
    PhcResponse.Content rmnchaSelectedPhc;
    SubcentersFromPHCResponse.Content rmnchaSelectedSubCenter;
    SubCVillResponse.Content rmnchaSelectedVillage;

    private Activity activityContext;
    private PNCAlertViewModel pncAlertViewModel;

    public PNCAlert(Activity activityContext) {
        this.activityContext = activityContext;
        pncAlertViewModel = new PNCAlertViewModel(((Activity) activityContext).getApplication());
    }

    public void displayPNCMappingAlert() {

        AlertLayoutRmnchMappingBinding rmnchaBinding = DataBindingUtil.inflate(activityContext.getLayoutInflater(), R.layout.alert_layout_rmnch_mapping, null, false);

        final Dialog dialog = new Dialog(AppController.getInstance().getMainActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(rmnchaBinding.getRoot());
        dialog.show();

        rmnchaBinding.title.setText("Select Village for Post Natal Care");

        rmnchaBinding.btnCancel.setOnClickListener(view -> dialog.dismiss());

        rmnchaBinding.ivClose.setOnClickListener(view -> dialog.dismiss());

        rmnchaBinding.btnStartMap.setOnClickListener(view -> {
            PNCMappingFragment fragment = new PNCMappingFragment();
            fragment.setPhc(rmnchaSelectedPhc);
            fragment.setSubCenter(rmnchaSelectedSubCenter);
            fragment.setVillage(rmnchaSelectedVillage);
            FragmentManager fragmentManager = ((AppCompatActivity) activityContext).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_landing, fragment).addToBackStack(null).commit();
            dialog.dismiss();
        });

        final int[] retries = {0};

        rmnchaPhcLiveData = pncAlertViewModel.getPhcLiveData();
        rmnchaPhcLiveData.observe((FragmentActivity) activityContext, phcResponse -> {
            if (phcResponse != null) {
                rmnchaPhcCenters = new ArrayList<>(phcResponse.getContent());
                rmnchaPhcCenterNames = new ArrayList<>();
                for (PhcResponse.Content center : rmnchaPhcCenters) {
                    rmnchaPhcCenterNames.add(center.getName());
                }
                rmnchaBinding.spinnerPhc.setAdapter(new ArrayAdapter(activityContext, R.layout.layout_rmncha_spinner_textview, rmnchaPhcCenterNames));
            }
        });

        rmnchaBinding.spinnerPhc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rmnchaSelectedPhc = rmnchaPhcCenters.get(i);
                String uuid = rmnchaSelectedPhc.getUuid();
                rmnchaBinding.subcenterLoaderProgressBar.setVisibility(View.VISIBLE);
                rmnchaSubCenterLiveData = pncAlertViewModel.getSubcentersFromPHCResponseLiveData(uuid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rmnchaSubCenterLiveData = pncAlertViewModel.getSubcentersFromPHCResponseLiveData("");
        rmnchaSubCenterLiveData.observe((FragmentActivity) activityContext, subcentersFromPHCResponse -> {
            if (subcentersFromPHCResponse != null) {
                rmnchaSubcenters = subcentersFromPHCResponse.getContent();
                //rmnchaSubcenters.sort(new SubcentersFromPHCResponse.SubCenterFromPhcComparator());
                rmnchaPhcSubCenterNames = new ArrayList<>();
                for (SubcentersFromPHCResponse.Content center : rmnchaSubcenters) {
                    rmnchaPhcSubCenterNames.add(center.getTarget().getProperties().getName());
                }
                rmnchaBinding.spinSubCenter.setAdapter(new ArrayAdapter(activityContext, R.layout.layout_rmncha_spinner_textview, rmnchaPhcSubCenterNames));
                rmnchaBinding.subcenterLoaderProgressBar.setVisibility(View.GONE);
            }
        });

        rmnchaBinding.spinSubCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rmnchaSelectedSubCenter = rmnchaSubcenters.get(i);
                String uuid = rmnchaSelectedSubCenter.getTarget().getProperties().getUuid();
                rmnchaBinding.villageLoaderProgressBar.setVisibility(View.VISIBLE);
                rmnchaVillageLiveData = pncAlertViewModel.getVillagesFromSubcentersLiveData(uuid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rmnchaVillageLiveData = pncAlertViewModel.getVillagesFromSubcentersLiveData("");
        rmnchaVillageLiveData.observe((FragmentActivity) activityContext, villageResponse -> {
            if (villageResponse != null) {
                rmnchaVillageCenters = villageResponse.getContent();
                rmnchaPhcVillageNames = new ArrayList<>();
                //rmnchaVillageCenters.sort(new SubCVillResponse.SubCVillComparator());
                for (SubCVillResponse.Content center : rmnchaVillageCenters) {
                    rmnchaPhcVillageNames.add(center.getTarget().getVillageProperties().getName());
                }
                rmnchaBinding.spinVillage.setAdapter(new ArrayAdapter(activityContext, R.layout.layout_rmncha_spinner_textview, rmnchaPhcVillageNames));
                rmnchaBinding.villageLoaderProgressBar.setVisibility(View.GONE);

            }
        });

        rmnchaBinding.spinVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rmnchaSelectedVillage = rmnchaVillageCenters.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
