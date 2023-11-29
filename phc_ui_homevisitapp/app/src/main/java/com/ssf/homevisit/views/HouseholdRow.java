package com.ssf.homevisit.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.ssf.homevisit.R;
import com.ssf.homevisit.alerts.HHSESNearbyHousesAlert;
import com.ssf.homevisit.alerts.HHSESurveyAlert;
import com.ssf.homevisit.alerts.IndividualSESurveyAlert;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.utils.Util;
import com.ssf.homevisit.viewmodel.HouseHoldLevelMappingViewModel;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HouseholdRow extends RecyclerView.ViewHolder {

    TextView savedOnTextView;

    TextView houseHoldName;
    HouseHoldProperties houseHoldProperties;
    VillageProperties village;
    TextView lgdCodeTextView;
    TextView latlngTextview;
    TextView numberOfResidentTextView;
    ImageButton markLocationImageButton;
    HouseHoldLevelMappingViewModel viewModel;
    Context context;

    public HouseholdRow(@NonNull @NotNull View itemView, Fragment fragment, Context context, int surveyType) {
        super(itemView);
        LinearLayout editData = itemView.findViewById(R.id.editdata);
        numberOfResidentTextView = itemView.findViewById(R.id.familymem);
        houseHoldName = itemView.findViewById(R.id.householdname);
        lgdCodeTextView = itemView.findViewById(R.id.lgdcode001);
        latlngTextview = itemView.findViewById(R.id.latlng001);
        markLocationImageButton = itemView.findViewById(R.id.markgpsloc);
        savedOnTextView = itemView.findViewById(R.id.time);

        if (surveyType == HHSESNearbyHousesAlert.INDIVIDUAL_LEVEL_SURVEY) {
            editData.setOnClickListener(view -> {
                IndividualSESurveyAlert.getInstance(context).displayMappingAlert(fragment, houseHoldProperties);
            });
        } else {
            editData.setOnClickListener(view -> {
                HHSESurveyAlert.getInstance(context).displayMappingAlert(fragment, houseHoldProperties);
            });
        }

        if (surveyType == HHSESNearbyHousesAlert.INDIVIDUAL_LEVEL_SURVEY) {
            ImageView editDataImageView = itemView.findViewById(R.id.edit_data_image);
            editDataImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_user_solid));
            TextView editDataText = itemView.findViewById(R.id.edit_data_text);
            editDataText.setText(R.string.perform_individual_level_survey);
        }
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
    }

    public void setLatLng(double latitude, double longitude) {
        String latLong = Util.DDtoDMS(latitude, "latitude") + " - " + Util.DDtoDMS(longitude, "longitude");
        latlngTextview.setText(latLong);
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
