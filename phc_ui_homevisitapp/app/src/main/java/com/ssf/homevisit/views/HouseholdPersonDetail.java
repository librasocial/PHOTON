package com.ssf.homevisit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ssf.homevisit.R;
import com.ssf.homevisit.alerts.DisplayAlert;
import com.ssf.homevisit.alerts.HouseholdAlert;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.interfaces.OnHouseholdCreated;
import com.ssf.homevisit.models.CreateHouseholdResponse;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.utils.Util;
import com.ssf.homevisit.viewmodel.HouseHoldLevelMappingViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HouseholdPersonDetail extends LinearLayout {

    private Fragment currentFragment;
    TextView savedOnTextView;
    TextView houseHoldName;
    HouseHoldProperties houseHoldProperties;
    VillageProperties village;
    String lgdCode;
    TextView lgdCodeTextView;
    TextView latlngTextview;
    TextView numberOfResidentTextView;
    TextView HouseHoldNameTextView;
    ImageButton markLocationImageButton;
    HouseHoldLevelMappingViewModel viewModel;

    public HouseholdPersonDetail(Context context) {
        this(context, null);
    }

    public HouseholdPersonDetail(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View villagerDetail = inflater.inflate(R.layout.household_view, this, true);

        ImageButton editData = villagerDetail.findViewById(R.id.editdata);
        numberOfResidentTextView = villagerDetail.findViewById(R.id.familymem);
        houseHoldName = villagerDetail.findViewById(R.id.householdname);
        lgdCodeTextView = villagerDetail.findViewById(R.id.lgdcode001);
        latlngTextview = villagerDetail.findViewById(R.id.latlng001);
        markLocationImageButton = findViewById(R.id.markgpsloc);
        savedOnTextView = villagerDetail.findViewById(R.id.time);
        editData.setOnClickListener(view -> {

            HouseholdAlert.getInstance(context).displayMappingAlert(houseHoldProperties, currentFragment);
        });

    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public HouseholdPersonDetail(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HouseholdPersonDetail(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setHouseHoldProperties(HouseHoldProperties _houseHoldProperties) {
        houseHoldProperties = _houseHoldProperties;
        setLGDCode(houseHoldProperties.getHouseID());
        setLatLng(houseHoldProperties.getLatitude(), houseHoldProperties.getLongitude());
        setSavedOn(houseHoldProperties.getDateModified());
        setHouseHeadName(houseHoldProperties.getHouseHeadName());
        setNumberOfResidents(houseHoldProperties.getTotalFamilyMembers());
    }

    /**
     * @param dateModified should be in the format of yyyy-MM-ddThh:mm:ss
     */
    public void setSavedOn(String dateModified) {
        SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        sourceDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat targetDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        targetDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        try {
            Date date = sourceDateFormat.parse(dateModified);
            String dateText = targetDateFormat.format(date);
            savedOnTextView.setText(dateText);
        } catch (Exception ignored) {
        }
    }

    public void setVillage(VillageProperties village) {
        this.village = village;
    }

    public void setLGDCode(String lgdcode) {
        lgdCodeTextView.setText(lgdcode);
        this.lgdCode = lgdcode;
    }

    public void setLatLng(double latitude, double longitude) {

        if (latitude != 0 && longitude != 0) {

            String latLong = Util.DDtoDMS(latitude, "latitude") + " - " + Util.DDtoDMS(longitude, "longitude");
            latlngTextview.setText(latLong);
            markLocationImageButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.marked));
        }
        enableButtonGpsButton();
//            markLocationImageButton.setOnClickListener(v -> {
//                UIController.getInstance().displayToastMessage(getContext(), "Already Marked");
//            });

    }

    private void enableButtonGpsButton() {

        markLocationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lgdCode == null || lgdCode.isEmpty()) {
                    UIController.getInstance().displayToastMessage(getContext(), "waiting to fetch village properties");
                    return;
                }
                DisplayAlert.getInstance(getContext()).displayMappingAlert(lgdCode, this::addLatLongToHouseHold);
            }

            private void addLatLongToHouseHold(double latitude, double longitude) {

                houseHoldProperties.setDateCreated("");
                houseHoldProperties.setDateModified("");

                viewModel.patchHousehold(latitude, longitude, houseHoldProperties, new OnHouseholdCreated() {
                    @Override
                    public void onHouseholdCreated(CreateHouseholdResponse createHouseholdResponse) {
                        if (createHouseholdResponse == null) {
                            UIController.getInstance().displayToastMessage(getContext(), "Cannot create Household");
                            return;
                        }
                        householdMarkingSuccessfull(createHouseholdResponse);
                    }

                    @Override
                    public void onHouseholdCreationFailed() {
                        UIController.getInstance().displayToastMessage(getContext(), "Cannot update the household detail");
                    }
                });
            }

            private void householdMarkingSuccessfull(CreateHouseholdResponse createHouseholdResponse) {
                setLatLng(createHouseholdResponse.getLatitude(), createHouseholdResponse.getLongitude());
//                    latlngTextview.setText(createHouseholdResponse.getLatitude() + " " + createHouseholdResponse.getLongitude());
//                    markLocationImageButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.marked));
//                    markLocationImageButton.setOnClickListener(v -> {
//                        UIController.getInstance().displayToastMessage(getContext(), "Already Marked");
//                    });

            }

        });
    }

    public void setHouseHeadName(String name) {
        houseHoldName.setText(name);
    }

    public void setViewModel(HouseHoldLevelMappingViewModel _viewModel) {
        viewModel = _viewModel;
    }

    public void setNumberOfResidents(Integer numberOfResidents) {
        numberOfResidentTextView.setText(numberOfResidents + "");
    }
}
