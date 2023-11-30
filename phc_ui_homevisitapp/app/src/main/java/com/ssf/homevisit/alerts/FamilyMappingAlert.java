package com.ssf.homevisit.alerts;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Window;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.microsoft.appcenter.analytics.Analytics;
import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.databinding.LayoutFamilyMappingBinding;
import com.ssf.homevisit.interfaces.OnCitizenCreated;
import com.ssf.homevisit.models.*;
import com.ssf.homevisit.utils.AnalyticsEvents;
import com.ssf.homevisit.utils.ProgressDialog;
import com.ssf.homevisit.viewmodel.FamilyMappingViewModel;
import com.ssf.homevisit.viewmodel.LoginViewModel;
import com.ssf.homevisit.views.FamilyMappingRow;


public class FamilyMappingAlert {
    private final Context context;
    private static FamilyMappingAlert alert;
    private Dialog currentDialog;
    private LayoutFamilyMappingBinding binding;
    private FamilyMappingViewModel familyMappingViewModel;
    private HouseHoldProperties houseHoldProperties;

    /**
     * Get the instance of UIController
     *
     * @return
     */
    public static FamilyMappingAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (FamilyMappingAlert.class) {
                if (alert == null) {
                    alert = new FamilyMappingAlert(mcontext);
                }
            }
        }
        return alert;
    }

    public FamilyMappingAlert(Context context) {
        this.context = context;
//        commonAlertViewModel = new CommonAlertViewModel(((Activity) context).getApplication());
    }


    @SuppressLint("SetTextI18n")
    public void displayMappingAlert(HouseHoldProperties houseHoldProperties) {
        this.houseHoldProperties = houseHoldProperties;
        final Dialog dialog = new Dialog(AppController.getInstance().getMainActivity());
        binding = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.layout_family_mapping, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(binding.getRoot());
        Rect displayRectangle = new Rect();
        dialog.getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        binding.getRoot().setMinimumWidth((int) (displayRectangle.width() * 0.9f)); // Height
        binding.getRoot().setMinimumHeight((int) (displayRectangle.height() * 0.9f)); // Height
        dialog.show();
        currentDialog = dialog;
        familyMappingViewModel = new ViewModelProvider((FragmentActivity) context).get(FamilyMappingViewModel.class);

        binding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            dialog.dismiss();
        });
        binding.nameOfHouseHead.setText(houseHoldProperties.getHouseHeadName());
        binding.saveSurvey.setOnClickListener(view -> {
            saveMapping();
        });
        getResidentData();
    }

    public static void dismiss() {
        if (alert != null) {
            alert.currentDialog.dismiss();
            alert = null;
        }
    }

    private void getResidentData() {
        familyMappingViewModel.getResidentsInHouseholdData(houseHoldProperties.getUuid()).observe((FragmentActivity) context, residentInHouseHoldResponse -> {
            binding.mappingContainer.removeAllViews();
            for (ResidentInHouseHoldResponse.Content content : residentInHouseHoldResponse.getContent()) {
                ResidentProperties residentProperties = content.getTarget().getProperties();
                if (residentProperties.getIsHead())
                    continue;
                FamilyMappingRow familyMappingRow = new FamilyMappingRow(context);
                familyMappingRow.setResidentProperties(residentProperties);
                binding.mappingContainer.addView(familyMappingRow);
            }
        });
    }

    private void saveMapping() {
        ProgressDialog.getInstance(context).display();
        ProgressDialog.getInstance(context).setStatus("Saving Family Relations");
        saveResidentAtIndex(0);
    }

    private void saveResidentAtIndex(int index) {
        FamilyMappingRow familyMappingRow = (FamilyMappingRow) binding.mappingContainer.getChildAt(index);
        if (familyMappingRow == null) {
            Analytics.trackEvent(AnalyticsEvents.REQUESTED_SAVE_BEFORE_LOADING_FAMILY_MAPPING);
            if (index == binding.mappingContainer.getChildCount()) {
                ProgressDialog.dismiss();
                dismiss();
            } else {
                ProgressDialog.getInstance(context).setStatus("Saving Relations " + index + "/" + binding.mappingContainer.getChildCount());
                saveResidentAtIndex(index + 1);
            }
            // todo handle some resident data not saving.
            return;
        }
        final int ind = index + 1;
        familyMappingViewModel.createCitizen(familyMappingRow.getResidentProperties(), houseHoldProperties.getUuid(), new OnCitizenCreated() {
            @Override
            public void onCitizenCreated(CreateResidentResponse createHouseholdResponse) {
                if (ind == binding.mappingContainer.getChildCount()) {
                    ProgressDialog.dismiss();
                    dismiss();
                } else {
                    ProgressDialog.getInstance(context).setStatus("Saving Relations " + ind + "/" + binding.mappingContainer.getChildCount());
                    saveResidentAtIndex(index + 1);
                }
            }

            @Override
            public void onCitizenCreationFailed() {
                saveResidentAtIndex(index + 1);
            }
        });
    }
}