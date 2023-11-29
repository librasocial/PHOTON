package com.ssf.homevisit.alerts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ssf.homevisit.R;
import com.ssf.homevisit.adapters.HouseholdsAdapter;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.LayoutNearbyHouseAlertBinding;
import com.ssf.homevisit.factories.HHSESurveyViewModelFactory;
import com.ssf.homevisit.interfaces.OnHouseholdCreated;
import com.ssf.homevisit.models.CreateHouseholdResponse;
import com.ssf.homevisit.models.HouseHoldInVillageResponse;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.ResponseNearByHouseholds;
import com.ssf.homevisit.models.SearchHouseholdResponse;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.utils.ProgressDialog;
import com.ssf.homevisit.viewmodel.HHSESurveyViewModel;

import java.util.ArrayList;

public class HHSESNearbyHousesAlert {

    public static int INDIVIDUAL_LEVEL_SURVEY = 0;
    public static int HH_LEVEL_SURVEY = 1;
    private static Context context;
    private static com.ssf.homevisit.alerts.HHSESNearbyHousesAlert alert;
    private HHSESurveyViewModel hhseSurveyViewModel;
    private Dialog dialog3;
    private HouseholdsAdapter householdsAdapter;
    private ArrayList<HouseHoldProperties> nearbyHouseholdList;
    private LayoutNearbyHouseAlertBinding binding;
    private boolean showingNearby = true;
    private boolean showingSearched = true;
    private Dialog currentDialog;
    private String villageId;
    private Fragment currentFragment;
    private String villageLgdCode;
    private int surveyType;

    public static HHSESNearbyHousesAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (com.ssf.homevisit.alerts.HouseholdAlert.class) {
                if (alert == null) {
                    alert = new HHSESNearbyHousesAlert();
                    context = mcontext;

                }
            }
        }
        return alert;
    }


    @SuppressLint("SetTextI18n")
    public void displayMappingAlert(Fragment fragment, VillageProperties villageProperties, String subCenterName, String phcName, double latitude, double longitude, int surveyType) {
        this.surveyType = surveyType;
        currentFragment = fragment;
        villageLgdCode = villageProperties.getLgdCode();
        hhseSurveyViewModel = new ViewModelProvider(
                ((AppCompatActivity) context),
                new HHSESurveyViewModelFactory(((Activity) context).getApplication(), villageProperties, subCenterName, phcName)
        ).get(HHSESurveyViewModel.class);
        villageId = villageProperties.getUuid();
        dialog3 = new Dialog(AppController.getInstance().getMainActivity());
        binding = DataBindingUtil.inflate(dialog3.getLayoutInflater(), R.layout.layout_nearby_house_alert, null, false);
        Rect displayRectangle = new Rect();
        dialog3.getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        binding.getRoot().setMinimumHeight((int) (displayRectangle.height() * 0.9f)); // Height
        currentDialog = dialog3;
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog3.setContentView(binding.getRoot());
        dialog3.show();

        binding.ivClose.setOnClickListener(v -> {
            dismiss();
        });

        showNearByHouseholds(latitude, longitude);

        binding.showNearbyButton.setOnClickListener(view -> {
            UIController.getInstance().displayToastMessage(context, "finding nearby");
            if (!showingNearby || showingSearched) {
                showNearByHouseholds(latitude, longitude);
                binding.showNearbyButton.setTextColor(context.getResources().getColor(R.color.selected_grey));
                binding.showAllButton.setTextColor(context.getResources().getColor(R.color.button_blue));
                showingNearby = true;
            }
        });

        binding.searchHouse.setOnClickListener(view -> {
            String houseId = binding.getHouseInput.getText().toString();
            if (houseId.isEmpty()) {
                if (showingNearby)
                    showNearByHouseholds(latitude, longitude);
                else
                    showAllHouseholds();
            } else {
                binding.showNearbyButton.setTextColor(context.getResources().getColor(R.color.button_blue));
                binding.showAllButton.setTextColor(context.getResources().getColor(R.color.button_blue));
                showSearchData(houseId);
            }
        });

        binding.showAllButton.setOnClickListener(view -> {
            UIController.getInstance().displayToastMessage(context, "finding all");
            if (showingNearby || showingSearched) {
                showAllHouseholds();
                binding.showAllButton.setTextColor(context.getResources().getColor(R.color.selected_grey));
                binding.showNearbyButton.setTextColor(context.getResources().getColor(R.color.button_blue));
                showingNearby = false;
            }
        });

        if (surveyType == INDIVIDUAL_LEVEL_SURVEY) {
            binding.existingPr.setText("Individual Level Socio Eco Survey");
        }
    }

    private void showNearByHouseholds(double latitude, double longitude) {
        hhseSurveyViewModel.getNearByHousehold(500, 0, latitude, longitude, 10000.0, villageId).observe((FragmentActivity) context, new Observer<ResponseNearByHouseholds>() {
            @Override
            public void onChanged(ResponseNearByHouseholds responseNearByHouseholds) {
                if (responseNearByHouseholds == null) {
                    UIController.getInstance().displayToastMessage(context, "Cant get nearby households");
                } else if (responseNearByHouseholds.getContent().size() == 0) {
                    UIController.getInstance().displayToastMessage(context, "No nearby households found");
                } else {
                    nearbyHouseholdList = (ArrayList<HouseHoldProperties>) responseNearByHouseholds.getContent();
                    householdsAdapter = new HouseholdsAdapter(context, nearbyHouseholdList, currentFragment, surveyType);
                    binding.recyclerView.setAdapter(householdsAdapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    UIController.getInstance().displayToastMessage(context, "Found " + responseNearByHouseholds.getContent().size() + " Houses around you");
                }
            }
        });
    }

    private void showAllHouseholds() {
        hhseSurveyViewModel.getHouseHoldInVillageData(villageId).observe((FragmentActivity) context, new Observer<HouseHoldInVillageResponse>() {
            @Override
            public void onChanged(HouseHoldInVillageResponse houseHoldInVillageResponse) {
                if (houseHoldInVillageResponse == null) {
                    UIController.getInstance().displayToastMessage(context, "Cant get households");
                } else if (houseHoldInVillageResponse.getContent() == null) {
                    UIController.getInstance().displayToastMessage(context, "No households found");
                } else {
                    if (nearbyHouseholdList == null)
                        nearbyHouseholdList = new ArrayList<>();
                    nearbyHouseholdList.clear();
                    for (HouseHoldInVillageResponse.Content content : houseHoldInVillageResponse.getContent()) {
                        nearbyHouseholdList.add(content.getTarget().getProperties());
                    }
                    householdsAdapter = new HouseholdsAdapter(context, nearbyHouseholdList, currentFragment, surveyType);
                    binding.recyclerView.setAdapter(householdsAdapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    UIController.getInstance().displayToastMessage(context, "Found " + houseHoldInVillageResponse.getContent().size() + " Houses");
                }
            }
        });
    }

    private void onLocationFetched(double latitude, double longitude) {
        hhseSurveyViewModel.createHousehold(latitude, longitude, null, new OnHouseholdCreated() {
            @Override
            public void onHouseholdCreated(CreateHouseholdResponse createHouseholdResponse) {
                if (createHouseholdResponse == null) {
                    UIController.getInstance().displayToastMessage(context, "Cannot create Household");
                    return;
                }
                if (showingNearby) {
                    showNearByHouseholds(latitude, longitude);
                } else {
                    showAllHouseholds();
                }
                HouseholdAlert.getInstance(context).displayMappingAlert(CreateHouseholdResponse.houseHoldResponseToHouseHoldProperties(createHouseholdResponse), currentFragment);
            }

            @Override
            public void onHouseholdCreationFailed() {
                UIController.getInstance().displayToastMessage(context, "Cannot save the household detail");
            }
        });
    }

    private void showSearchData(String data) {

        if (data != null) {
            ProgressDialog.getInstance(context).display();
            ProgressDialog.getInstance(context).setStatus("Searching for " + data);
            hhseSurveyViewModel.getHouseHoldSearchData(data, villageLgdCode).observe((FragmentActivity) context, new Observer<SearchHouseholdResponse>() {
                @Override
                public void onChanged(SearchHouseholdResponse houseHoldInVillageResponse) {
                    nearbyHouseholdList.clear();
                    for (SearchHouseholdResponse.SearchHouseHoldContent content : houseHoldInVillageResponse.getContent()) {
                        try {
                            if (content.getProperties().getUuid() == null) {
                                UIController.getInstance().displayToastMessage(context, "UUID for household not received");
                                return;
                            }
                            HouseHoldProperties houseHoldProperties = new HouseHoldProperties();
                            houseHoldProperties.setUuid(content.getProperties().getUuid());
                            houseHoldProperties.setHouseID(content.getProperties().getHouseID());
                            houseHoldProperties.setTotalFamilyMembers(content.getProperties().getTotalFamilyMembers());
                            houseHoldProperties.setDateModified(content.getProperties().getDateModified());
                            houseHoldProperties.setHouseHeadName(content.getProperties().getHouseHeadName());
                            houseHoldProperties.setLongitude(content.getProperties().getLongitude());
                            houseHoldProperties.setLatitude(content.getProperties().getLatitude());

                            nearbyHouseholdList.add(houseHoldProperties);
                        } catch (Exception e) {
                            Log.e("Error ", "Invalid long, lat " + e.getMessage());
                        }
                    }
                    householdsAdapter = new HouseholdsAdapter(context, nearbyHouseholdList, currentFragment, surveyType);
                    binding.recyclerView.setAdapter(householdsAdapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

                    UIController.getInstance().displayToastMessage(context, "Found " + houseHoldInVillageResponse.getContent().size() + " Houses");
                    ProgressDialog.dismiss();
                }
            });
        }

    }

    public static void dismiss() {
        if (alert != null) {
            alert.currentDialog.dismiss();
            alert.hhseSurveyViewModel.removeObservers((FragmentActivity) context);
            alert = null;
        }
    }
}
