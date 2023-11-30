package com.ssf.homevisit.fragment;

import static com.ssf.homevisit.models.CreateHouseholdResponse.houseHoldResponseToHouseHoldProperties;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.microsoft.appcenter.analytics.Analytics;
import com.ssf.homevisit.R;
import com.ssf.homevisit.alerts.DisplayAlert;
import com.ssf.homevisit.alerts.HouseholdAlert;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.FragmentHouseholdDataBinding;
import com.ssf.homevisit.factories.HouseHoldLevelMappingViewModelFactory;
import com.ssf.homevisit.interfaces.OnHouseholdCreated;
import com.ssf.homevisit.models.CreateHouseholdResponse;
import com.ssf.homevisit.models.HouseHoldInVillageResponse;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.SearchHouseholdResponse;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.utils.AnalyticsEvents;
import com.ssf.homevisit.utils.ProgressDialog;
import com.ssf.homevisit.viewmodel.HouseHoldLevelMappingViewModel;
import com.ssf.homevisit.views.HouseholdPersonDetail;

import java.util.List;


public class HouseholdDataFragment extends Fragment {
    View view;
    private FragmentHouseholdDataBinding binding;
    private HouseHoldLevelMappingViewModel houseHoldLevelMappingViewModel;
    private VillageProperties village;
    private String phcName;
    private boolean completedListOnly = false;
    private int listingBy = ALL;
    private String searchByData;
    private static final int SEARCH = 350;
    private int pageNo = 0;
    private int totalNumberOfPages = 0;
    private static final int ALL = 351;
    public static int SIZE_OF_RESULT = 10;
    public static final int SIZE_OF_SEARCH_RESULT = 5000;
    public String getPhcName() {
        return phcName;
    }
    public void setPhcName(String phcName) {
        this.phcName = phcName;
    }
    public String getSubCenterName() {
        return subCenterName;
    }
    public void setSubCenterName(String subCenterName) {
        this.subCenterName = subCenterName;
    }
    private String subCenterName;
    public HouseholdDataFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_household_data, container, false);
        view = binding.getRoot();
        houseHoldLevelMappingViewModel = new ViewModelProvider(
                this,
                new HouseHoldLevelMappingViewModelFactory(this.getActivity().getApplication(), village, subCenterName, phcName)
        ).get(HouseHoldLevelMappingViewModel.class);
        binding.existingPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHouseholdsInVillage(0, SIZE_OF_RESULT);
            }
        });
        binding.setViewModel(houseHoldLevelMappingViewModel);
        binding.setLifecycleOwner(this);
        binding.backButton.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        binding.searchHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String houseId = binding.getHouseInput.getText().toString();
                if (houseId.equals("")) {
                    getHouseholdsInVillage(pageNo, SIZE_OF_RESULT);
                } else {
                    getHouseholdSearchData(0, SIZE_OF_SEARCH_RESULT, houseId);
                }
            }
        });
        initilization();
        return view;
    }

    public void initilization() {

        getHouseholdsInVillage(0, SIZE_OF_RESULT);
        binding.addHousehold.setOnClickListener(view1 -> {
            DisplayAlert.getInstance(getActivity()).displayMappingAlert(village.getLgdCode(), this::onLocationFetched);
        });
        if (completedListOnly) {
            String collectedPrimaryData = "Collected Primary Data";
            binding.existingPr.setText(collectedPrimaryData);
        }
    }

    private void onLocationFetched(double latitude, double longitude) {
        houseHoldLevelMappingViewModel.createHousehold(latitude, longitude, null, new OnHouseholdCreated() {
            @Override
            public void onHouseholdCreated(CreateHouseholdResponse createHouseholdResponse) {
                houseHoldLevelMappingViewModel.getHouseHoldInVillageData(0, SIZE_OF_RESULT);
                HouseholdAlert.getInstance(getActivity()).displayMappingAlert(houseHoldResponseToHouseHoldProperties(createHouseholdResponse), HouseholdDataFragment.this);
            }
            @Override
            public void onHouseholdCreationFailed() {
                UIController.getInstance().displayToastMessage(getContext(), "Cannot save the household detail");
            }
        });
    }

    private void addObserverToSearchData(LiveData<SearchHouseholdResponse> liveData) {
        liveData.observe(getViewLifecycleOwner(), new Observer<SearchHouseholdResponse>() {
            @Override
            public void onChanged(SearchHouseholdResponse houseHoldInVillageResponse) {
                if (houseHoldInVillageResponse == null) {
                    UIController.getInstance().displayToastMessage(getContext(), "Try again");
                    Analytics.trackEvent(AnalyticsEvents.HOUSEHOLD_IN_VILLAGE_RESPONSE_NULL);
                    ProgressDialog.dismiss();
                    return;
                }
                totalNumberOfPages = houseHoldInVillageResponse.getTotalPages();
                binding.layoutHousehold.removeAllViews();
                for (SearchHouseholdResponse.SearchHouseHoldContent content : houseHoldInVillageResponse.getContent()) {
                    try {
                        if (content.getProperties().getUuid() == null) {
                            UIController.getInstance().displayToastMessage(getContext(), "UUID for household not received");
                            continue;
                        }
                        HouseholdPersonDetail householdPersonDetail = new HouseholdPersonDetail(getContext());
                        householdPersonDetail.setCurrentFragment(HouseholdDataFragment.this);
                        HouseHoldProperties houseHoldProperties = new HouseHoldProperties();
                        houseHoldProperties.setUuid(content.getProperties().getUuid());
                        houseHoldProperties.setHouseID(content.getProperties().getHouseID());
                        houseHoldProperties.setTotalFamilyMembers(content.getProperties().getTotalFamilyMembers());
                        houseHoldProperties.setDateModified(content.getProperties().getDateModified());
                        houseHoldProperties.setHouseHeadName(content.getProperties().getHouseHeadName());
                        if (content.getProperties().getLongitude() == null) {
                            content.getProperties().setLongitude(0d);
                        }
                        if (content.getProperties().getLatitude() == null) {
                            content.getProperties().setLatitude(0d);
                        }
                        houseHoldProperties.setLongitude(content.getProperties().getLongitude());
                        houseHoldProperties.setLatitude(content.getProperties().getLatitude());
                        householdPersonDetail.setHouseHoldProperties(houseHoldProperties);
                        householdPersonDetail.setViewModel(houseHoldLevelMappingViewModel);
                        binding.layoutHousehold.addView(householdPersonDetail);


                    } catch (Exception e) {
                        ProgressDialog.dismiss();
                    }
                }
                ProgressDialog.dismiss();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        houseHoldLevelMappingViewModel.getHouseHoldInVillageData(0, 0).removeObservers(getViewLifecycleOwner());
        houseHoldLevelMappingViewModel.clear();
    }

    public void setVillage(VillageProperties village) {
        this.village = village;
    }


    public void addObserverToData(LiveData<HouseHoldInVillageResponse> liveData) {
        liveData.observe(getViewLifecycleOwner(), new Observer<HouseHoldInVillageResponse>() {
            @Override
            public void onChanged(HouseHoldInVillageResponse houseHoldInVillageResponse) {
                binding.layoutHousehold.removeAllViews();
                if (houseHoldInVillageResponse != null) {
                    List<HouseHoldInVillageResponse.Content> contentList = houseHoldInVillageResponse.getContent();
                    totalNumberOfPages = houseHoldInVillageResponse.getTotalPages();
                    for (HouseHoldInVillageResponse.Content content : contentList) {
                        HouseholdPersonDetail householdPersonDetail = new HouseholdPersonDetail(getContext());
                        householdPersonDetail.setCurrentFragment(HouseholdDataFragment.this);
                        householdPersonDetail.setViewModel(houseHoldLevelMappingViewModel);
                        householdPersonDetail.setHouseHoldProperties(content.getTarget().getProperties());
                        householdPersonDetail.setVillage(village);
                        if (completedListOnly) {
                            if (content.getTarget().getProperties().getLongitude() != 0)
                                binding.layoutHousehold.addView(householdPersonDetail);
                        } else
                            binding.layoutHousehold.addView(householdPersonDetail);
                    }
                } else {
                    UIController.getInstance().displayToastMessage(getContext(), "Unable to get Households");
                    totalNumberOfPages = 0;
                    pageNo = 0;
                }
                ProgressDialog.dismiss();
                showPaginationData();
            }
        });
    }

    public void setCompletedListOnly(boolean b) {
        this.completedListOnly = b;
        SIZE_OF_RESULT = 1000;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Analytics.trackEvent("household after image captured");
        if (requestCode >= HouseholdAlert.cameraResultStart && requestCode <= HouseholdAlert.cameraResultEnd) {
            HouseholdAlert.getInstance(getContext()).onImageCaptured(requestCode - HouseholdAlert.cameraResultStart);
        }
    }

    private void previous() {
        if (pageNo != 0) {
            pageNo--;
            if (listingBy == SEARCH) {
                getHouseholdSearchData(pageNo, SIZE_OF_SEARCH_RESULT, searchByData);
            }
            if (listingBy == ALL) {
                getHouseholdsInVillage(pageNo, SIZE_OF_RESULT);
            }
        }
    }

    private void next() {
        if (pageNo < totalNumberOfPages - 1) {
            pageNo++;
            if (listingBy == SEARCH) {
                ProgressDialog.getInstance(getContext()).display();
                ProgressDialog.getInstance(getContext()).setStatus("Searching for " + searchByData);
                getHouseholdSearchData(pageNo, SIZE_OF_SEARCH_RESULT, searchByData);
                houseHoldLevelMappingViewModel.getHouseHoldSearchData(searchByData, pageNo, SIZE_OF_SEARCH_RESULT, village.getLgdCode());
            }
            if (listingBy == ALL) {
                getHouseholdsInVillage(pageNo, SIZE_OF_RESULT);
            }
        }
    }

    private void getHouseholdSearchData(int pageNo, int size, String data) {
        searchByData = data;
        if (data != null) {
            if (listingBy == ALL) {
                pageNo = 0;
            }
            this.pageNo = pageNo;
            listingBy = SEARCH;
            ProgressDialog.getInstance(getContext()).display();
            ProgressDialog.getInstance(getContext()).setStatus("Searching for " + data);
            LiveData<SearchHouseholdResponse> liveData = houseHoldLevelMappingViewModel.getHouseHoldSearchData(searchByData, pageNo, size, village.getLgdCode());
            if (!liveData.hasObservers()) {
                addObserverToSearchData(liveData);
            }
        }
    }

    private void getHouseholdsInVillage(int pageNo, int size) {
        if (listingBy == SEARCH) {
            pageNo = 0;
        }
        this.pageNo = pageNo;
        listingBy = ALL;
        ProgressDialog.getInstance(getContext()).display();
        ProgressDialog.getInstance(getContext()).setStatus("Fetching Data");
        LiveData<HouseHoldInVillageResponse> liveData = houseHoldLevelMappingViewModel.getHouseHoldInVillageData(pageNo, size);
        if (!liveData.hasObservers()) {
            addObserverToData(liveData);
        }
    }

    private void showPaginationData() {
    }
}