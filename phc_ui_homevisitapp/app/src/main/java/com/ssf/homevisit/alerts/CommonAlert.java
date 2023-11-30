package com.ssf.homevisit.alerts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.ssf.homevisit.R;
import com.ssf.homevisit.activity.VHSNCActivity;
import com.ssf.homevisit.adapters.CommonSpinnerAdapter;
import com.ssf.homevisit.app.AndroidApplication;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.AlertLayoutMappingBinding;
import com.ssf.homevisit.databinding.AlertLayoutRmnchMappingBinding;
import com.ssf.homevisit.fragment.HHSESurveyFragment;
import com.ssf.homevisit.fragment.HouseholdDataFragment;
import com.ssf.homevisit.fragment.HouseholdMappingFragment;
import com.ssf.homevisit.fragment.IndividualSESurveyFragment;
import com.ssf.homevisit.fragment.LandingFragment;
import com.ssf.homevisit.fragment.SociaEcoSurveyFragment;
import com.ssf.homevisit.healthWellnessSurveillance.LarvaActivity;
import com.ssf.homevisit.models.AllPhcResponse;
import com.ssf.homevisit.models.PhcResponse;
import com.ssf.homevisit.models.SubCVillResponse;
import com.ssf.homevisit.models.SubCenterResponse;
import com.ssf.homevisit.models.SubcentersFromPHCResponse;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.anc.ancalert.ANCAlertViewModel;
import com.ssf.homevisit.rmncha.ec.ecalert.ECAlertViewModel;
import com.ssf.homevisit.viewmodel.CommonAlertViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommonAlert {
    private List<PhcResponse.Content> phcCenters = new ArrayList<>();
    private List<String> phcCenterName = new ArrayList<>();
    private List<SubcentersFromPHCResponse.Content> subCenter = new ArrayList<>();
    private List<String> subCenterName = new ArrayList<>();

    private List<SubCVillResponse.Content> villages = new ArrayList<>();
    private List<String> villageName = new ArrayList<>();
    private static CommonAlert alert;
    VillageProperties selectedVillage;
    private String selectedPhc;
    private String vhsncOrVhd;
    private Dialog currentDialog;
    private String selectedSubCenter;
    CommonAlertViewModel commonAlertViewModel;
    LiveData<SubcentersFromPHCResponse> subCenterResponseLiveData = new  MutableLiveData<>();
    LiveData<SubCVillResponse> villageResponseLiveData = new  MutableLiveData<>();
    LiveData<PhcResponse> phcResponseLiveData = new MutableLiveData<>();
    Activity activityContext;
    SubcentersFromPHCResponse.Content currentSubCenter;
    AllPhcResponse.Content currentPhc;
    SubCVillResponse.Content currentVillage;
    /**
     * Get the instance of UIController
     *
     * @return
     */

    public CommonAlert(Activity activityContext) {
        this.activityContext = activityContext;
        commonAlertViewModel =  new CommonAlertViewModel(((Activity) activityContext).getApplication());
    }
    @SuppressLint("SetTextI18n")
    public void displayMappingAlert(String tag) {
        AlertLayoutMappingBinding binding = DataBindingUtil.inflate(activityContext.getLayoutInflater(), R.layout.alert_layout_mapping, null, false);
        final Dialog dialog = new Dialog(AppController.getInstance().getMainActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        currentDialog = dialog;
        binding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        phcResponseLiveData = commonAlertViewModel.getPhcLiveData();
        phcResponseLiveData.observe((FragmentActivity) activityContext, phcResponse -> {
            if (phcResponse != null) {
                phcCenters = phcResponse.getContent();
                phcCenterName = new ArrayList<>();
                for (PhcResponse.Content center : phcCenters) {
                    phcCenterName.add(center.getName());
                }
                binding.spinnerPhc.setAdapter(new ArrayAdapter(activityContext, R.layout.layout_rmncha_spinner_textview, phcCenterName));
            } else {
                return;
            }
        });

        binding.spinnerPhc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (phcCenters.size() > 0) {
                    selectedPhc = phcCenters.get(i).getName();
                    String uuid = phcCenters.get(i).getUuid();
                    binding.subcenterLoaderProgressBar.setVisibility(View.VISIBLE);
                    subCenterResponseLiveData = commonAlertViewModel.getSubcentersFromPHCResponseLiveData(uuid);
                    subCenterResponseLiveData.observe((FragmentActivity) activityContext, subcentersFromPHCResponse -> {
                        if (subcentersFromPHCResponse == null) {
                            return;
                        } else {
                            subCenter = subcentersFromPHCResponse.getContent();
                            subCenterName = new ArrayList<>();
                            for (SubcentersFromPHCResponse.Content center : subCenter) {
                                subCenterName.add(center.getTarget().getProperties().getName());
                            }
                            binding.spinSubCenter.setAdapter(new ArrayAdapter(activityContext, R.layout.layout_rmncha_spinner_textview, subCenterName));
                            binding.subcenterLoaderProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinSubCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (subCenter.size() > 0) {
                    selectedSubCenter = subCenter.get(i).getTarget().getProperties().getName();
                    String uuid = subCenter.get(i).getTarget().getProperties().getUuid();
                    binding.villageLoaderProgressBar.setVisibility(View.VISIBLE);
                    villageResponseLiveData = commonAlertViewModel.getVillagesFromSubcentersLiveData(uuid);
                    villageResponseLiveData.observe((FragmentActivity) activityContext, villageFromPhcResponse -> {
                        if (villageFromPhcResponse == null) {
                            return;
                        } else {
                            villages = villageFromPhcResponse.getContent();
                            villageName = new ArrayList<>();
                            for (SubCVillResponse.Content center : villages) {
                                villageName.add(center.getTarget().getVillageProperties().getName());
                            }
                            binding.spinVillage.setAdapter(new ArrayAdapter(activityContext, R.layout.layout_rmncha_spinner_textview, villageName));
                            binding.villageLoaderProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if(villages.size()>0){
                       selectedVillage= villages.get(i).getTarget().getVillageProperties();
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnCancel.setOnClickListener(view -> dialog.dismiss());

        binding.ivClose.setOnClickListener(view -> dialog.dismiss());

        if (tag.equals(AppDefines.VILLAGE_LEVEL_MAPPING)) {
            binding.title.setText("Select Village for Village Level Mapping");
            binding.llCheckbox.setVisibility(View.GONE);
            binding.btnStartMap.setOnClickListener(view -> {
                SociaEcoSurveyFragment fragment = new SociaEcoSurveyFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) activityContext).getSupportFragmentManager();
                fragment.setVillage(selectedVillage);
                fragment.setPhcName(selectedPhc);
                fragment.setSubCenterName(selectedSubCenter);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                dialog.dismiss();
            });
        } else if (tag.equals(AppDefines.HOUSE_LEVEL_MAPPING)) {
            binding.title.setText("Select Village for Household Level Mapping");
            binding.llCheckbox.setVisibility(View.VISIBLE);
            binding.dataAlreadyExistsTv.setOnClickListener(view -> binding.checkbox.setChecked(!binding.checkbox.isChecked()));

            binding.btnStartMap.setOnClickListener(view -> {
                if (binding.checkbox.isChecked()) {
                    Toast toast = Toast.makeText(activityContext.getApplicationContext(), "Checkbox enabled", Toast.LENGTH_SHORT);
                    toast.show();
                    HouseholdDataFragment fragment = new HouseholdDataFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) activityContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(new LandingFragment());
                    fragmentTransaction.replace(R.id.fragment_landing, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    fragment.setVillage(selectedVillage);
                    fragment.setPhcName(selectedPhc);
                    fragment.setSubCenterName(selectedSubCenter);
                    dialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(activityContext.getApplicationContext(), "Checkbox not enabled", Toast.LENGTH_SHORT);
                    toast.show();
                    HouseholdMappingFragment fragment = new HouseholdMappingFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) activityContext).getSupportFragmentManager();
                    fragment.setVillage(selectedVillage);
                    fragment.setSubCenterName(selectedSubCenter);
                    fragment.setPhcName(selectedPhc);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_landing, fragment).addToBackStack(null).commit();
                    dialog.dismiss();
                }
            });

        } else if (tag.equals(AppDefines.HOUSEHOLD_SE_SURVEY)) {
            binding.title.setText("Select Village for Household Socio Economic Survey");
            binding.btnStartMap.setOnClickListener(view -> {
                Toast toast = Toast.makeText(activityContext.getApplicationContext(), "Checkbox enabled", Toast.LENGTH_SHORT);
                toast.show();
                HHSESurveyFragment fragment = new HHSESurveyFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) activityContext).getSupportFragmentManager();
                fragment.setVillage(selectedVillage);
                fragment.setPhcName(selectedPhc);
                fragment.setSubCenterName(selectedSubCenter);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing, fragment);
                fragmentTransaction.addToBackStack("landing");
                fragmentTransaction.commit();
                dialog.dismiss();
            });
        } else if (tag.equals(AppDefines.INDIVIDUAL_SE_SURVEY)) {
            binding.title.setText("Select Village for Individual Socio Economic Survey");
            binding.btnStartMap.setOnClickListener(view -> {
                Toast toast = Toast.makeText(activityContext.getApplicationContext(), "Checkbox enabled", Toast.LENGTH_SHORT);
                toast.show();
                IndividualSESurveyFragment fragment = new IndividualSESurveyFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) activityContext).getSupportFragmentManager();
                fragment.setVillage(selectedVillage);
                fragment.setPhcName(selectedPhc);
                fragment.setSubCenterName(selectedSubCenter);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                dialog.dismiss();
            });
        } else if (tag.equals(AppDefines.VILLAGE_LEVEL_SOCIO_ECONOMIC_SURVEY)) {
            binding.btnStartMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(activityContext, LarvaActivity.class);
                    intent.putExtra(AppDefines.VILLAGE_NAME, selectedVillage.getName());
                    intent.putExtra(AppDefines.VILLAGE_LGD_CODE, selectedVillage.getLgdCode());
                    intent.putExtra(AppDefines.VILLAGE_UUID, selectedVillage.getUuid());
                    intent.putExtra(AppDefines.FLOW_TYPE, AppDefines.VILLAGE_LEVEL_SOCIO_ECONOMIC_SURVEY);
                    activityContext.startActivity(intent);
                    dialog.dismiss();
                }
            });
        } else if (tag.equals(AppDefines.HNW_VILLAGE_SURVEILLANCE)) {
            binding.title.setText("Select Village for Health & Wellness Surveillance");
            binding.btnStartMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(activityContext, LarvaActivity.class);
                    intent.putExtra(AppDefines.FLOW_TYPE, AppDefines.HNW_VILLAGE_SURVEILLANCE);
                    intent.putExtra(AppDefines.VILLAGE_NAME, selectedVillage.getName());
                    intent.putExtra(AppDefines.VILLAGE_LGD_CODE, selectedVillage.getLgdCode());
                    intent.putExtra(AppDefines.VILLAGE_UUID, selectedVillage.getUuid());
                    intent.putExtra(AppDefines.ID_TOKEN, AndroidApplication.prefs.getString(AppDefines.ID_TOKEN, ""));
                    activityContext.startActivity(intent);
                    dialog.dismiss();
                }
            });
        } else if (tag.equals(AppDefines.HNW_HOUSEHOLD_SURVEILLANCE)) {
            binding.title.setText("Select Village for Health & Wellness Surveillance");
            binding.btnStartMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(activityContext, LarvaActivity.class);
                    intent.putExtra(AppDefines.FLOW_TYPE, AppDefines.HNW_HOUSEHOLD_SURVEILLANCE);
                    intent.putExtra(AppDefines.ID_TOKEN, AndroidApplication.prefs.getString(AppDefines.ID_TOKEN, ""));
                    intent.putExtra(AppDefines.VILLAGE_UUID, selectedVillage.getUuid());
                    intent.putExtra(AppDefines.VILLAGE_LGD_CODE, selectedVillage.getLgdCode());
                    intent.putExtra(AppDefines.VILLAGE_NAME, selectedVillage.getName());
                    intent.putExtra(AppDefines.HOUSEHOLD_ID, selectedVillage.getUuid());
                    activityContext.startActivity(intent);
                    dialog.dismiss();
                }
            });
        } else if (tag.equals(AppDefines.HNW_INDIVIDUAL_SURVEILLANCE)) {
            binding.title.setText("Select Village for Health & Wellness Surveillance");
            binding.btnStartMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(activityContext, LarvaActivity.class);
                    intent.putExtra(AppDefines.FLOW_TYPE, AppDefines.HNW_INDIVIDUAL_SURVEILLANCE);
                    intent.putExtra(AppDefines.VILLAGE_NAME, selectedVillage.getName());
                    intent.putExtra(AppDefines.ID_TOKEN, AndroidApplication.prefs.getString(AppDefines.ID_TOKEN, ""));
                    intent.putExtra(AppDefines.VILLAGE_UUID, selectedVillage.getUuid());
                    intent.putExtra(AppDefines.VILLAGE_LGD_CODE, selectedVillage.getLgdCode());
                    activityContext.startActivity(intent);
                    dialog.dismiss();

                }
            });
        }

    }

    public void displayRMNCHAMappingAlert(String tag, AllPhcResponse allPhcResponse) {
        String phcUuid = "8d9392ec-97cf-4a24-a761-8479055424b0";
        if (allPhcResponse == null || allPhcResponse.getContent().isEmpty()) {
            allPhcResponse = buildPhcData(phcUuid);
        }

        CommonSpinnerAdapter adapter;
        final Dialog dialog = new Dialog(AppController.getInstance().getMainActivity());
        AlertLayoutRmnchMappingBinding binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.alert_layout_rmnch_mapping, null, false);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());
        dialog.show();
        currentDialog = dialog;

        binding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        final int[] retries = {0};
        currentPhc = allPhcResponse.getContent().get(0);
        subCenterResponseLiveData = commonAlertViewModel.getSubcentersFromPHCResponseLiveData(phcUuid);
        subCenterResponseLiveData.observe((FragmentActivity) activityContext, subcentersFromPHCResponse -> {
            if (subcentersFromPHCResponse == null) {
                if (retries[0] == 10) {
                    UIController.getInstance().displayToastMessage(activityContext, "cannot get subcenter from PHC from microservice");
                    return;
                }
                retries[0]++;
                commonAlertViewModel.getSubcentersFromPHCResponseLiveData(phcUuid);
            } else {
                List<SubcentersFromPHCResponse.Content> currentSubcenters = subcentersFromPHCResponse.getContent();
                currentSubcenters.sort(new SubcentersFromPHCResponse.SubCenterFromPhcComparator());
                CommonSpinnerAdapter subcenterAdapter = new CommonSpinnerAdapter(subcentersFromPHCResponse, AppController.getInstance().getMainActivity(), currentSubcenters);
                binding.spinSubCenter.setAdapter(subcenterAdapter);
                binding.subcenterLoaderProgressBar.setVisibility(View.GONE);
            }
        });
        villageResponseLiveData = commonAlertViewModel.getVillagesFromSubcentersLiveData("");
        villageResponseLiveData.observe((FragmentActivity) activityContext, subCVillResponse1 -> {
            if (subCVillResponse1 == null) return;
            List<SubCVillResponse.Content> currentVillages = subCVillResponse1.getContent();
            currentVillages.sort(new SubCVillResponse.SubCVillComparator());
            CommonSpinnerAdapter villageAdapter = new CommonSpinnerAdapter(subCVillResponse1, AppController.getInstance().getMainActivity(), currentVillages);
            binding.spinVillage.setAdapter(villageAdapter);
            binding.villageLoaderProgressBar.setVisibility(View.GONE);
        });

        binding.ivClose.setOnClickListener(view -> {
            dialog.dismiss();
        });

        switch (tag) {
            case AppDefines.VHSNC:
                binding.rlVHSNC.setVisibility(View.VISIBLE);
                ArrayList<String> options = new ArrayList<>();
                options.add("VHSNC");
                options.add("VHND");
                ArrayAdapter<String> adapterVHSNC = new ArrayAdapter<>(activityContext, R.layout.spinner_text_layout, R.id.textView, options);
                binding.spinnerVHSNC.setAdapter(adapterVHSNC);
                binding.spinnerVHSNC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        vhsncOrVhd = options.get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                binding.title.setText(R.string.select_village_for_vhsnc);
                binding.btnStartMap.setOnClickListener(view -> {
                    Intent vhsncIntent = VHSNCActivity.Companion.getNewIntent(activityContext, currentVillage, currentSubCenter, currentPhc, vhsncOrVhd);
                    activityContext.startActivity(vhsncIntent);
                    dialog.dismiss();
                });
                break;
            case AppDefines.ELIGIBLE_COUPLE_REGISTRATION:
                binding.title.setText("Select Village for Eligible Couple Process");
                binding.btnStartMap.setOnClickListener(view -> {
                    dialog.dismiss();
                });
                break;
            default:
        }

        adapter = new CommonSpinnerAdapter(allPhcResponse, allPhcResponse.getContent(), AppController.getInstance().getMainActivity(), allPhcResponse.getContent());
        binding.spinnerPhc.setAdapter(adapter);

        AllPhcResponse finalAllPhcResponse = allPhcResponse;
        binding.spinnerPhc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPhc = finalAllPhcResponse.getContent().get(i).getProperties().getPhc();
                currentPhc = finalAllPhcResponse.getContent().get(i);
                commonAlertViewModel.getSubcentersFromPHCResponseLiveData(phcUuid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerPhc.setSelection(0);

        binding.spinSubCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    CommonSpinnerAdapter adapter1 = (CommonSpinnerAdapter) (adapterView.getAdapter());
                    selectedSubCenter = adapter1.getName(i);
                    currentSubCenter = (SubcentersFromPHCResponse.Content) adapter1.getItem(i);
                    commonAlertViewModel.getVillagesFromSubcentersLiveData(adapter1.getUuid(i));
                    binding.villageLoaderProgressBar.setVisibility(View.VISIBLE);

                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    CommonSpinnerAdapter adapter1 = (CommonSpinnerAdapter) (adapterView.getAdapter());
                    currentVillage = (SubCVillResponse.Content) adapter1.getItem(i);
                    selectedVillage = ((SubCVillResponse.Content) adapter1.getItem(i)).getTarget().getVillageProperties();
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private AllPhcResponse buildPhcData(String phcUuid) {
        AllPhcResponse allPhcResponse;
        allPhcResponse = new AllPhcResponse();
        AllPhcResponse.Content content = new AllPhcResponse.Content();
        AllPhcResponse.Properties prop = new AllPhcResponse.Properties();
        prop.setName("Sugganahalli Rural PHC");
        prop.setUuid(phcUuid);
        prop.setPhc("Sugganahalli Rural PHC");
        content.setProperties(prop);
        content.setId(phcUuid);
        List<String> labels = new ArrayList<>();
        labels.add("Phc");
        content.setLabels(labels);
        List<AllPhcResponse.Content> list = new ArrayList<>();
        list.add(content);
        allPhcResponse.setContent(list);
        return allPhcResponse;
    }
}
