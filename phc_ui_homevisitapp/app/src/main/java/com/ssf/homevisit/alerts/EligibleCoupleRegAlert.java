package com.ssf.homevisit.alerts;

import android.content.Context;
import com.squareup.picasso.Picasso;
import com.ssf.homevisit.databinding.FemaleEcProgramLayoutBinding;
import com.ssf.homevisit.databinding.LayouteligiblecoupleregalertBinding;
import com.ssf.homevisit.interfaces.OnImageUrlFetched;
import com.ssf.homevisit.models.ResidentProperties;
import com.ssf.homevisit.viewmodel.HHSESurveyAlertViewModel;
import com.ssf.homevisit.views.IndividualSESFormRow;

import java.util.ArrayList;

public class EligibleCoupleRegAlert {
    private static Context context;
    private static EligibleCoupleRegAlert alert;
    private HHSESurveyAlertViewModel hhseSurveyAlertViewModel;
    private ArrayList<ResidentProperties> residentsList;
    private HHSESurveyAlertViewModel hhseSurveyViewModel;

    private LayouteligiblecoupleregalertBinding binding;

    public static EligibleCoupleRegAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (com.ssf.homevisit.alerts.HouseholdAlert.class) {
                if (alert == null) {
                    alert = new EligibleCoupleRegAlert();
                    context = mcontext;

                }
            }
        }
        return alert;
    }
    private void downloadImages() {
        for (ResidentProperties residentProperties :
                residentsList) {
            String bucketKey = null;
            try {
                bucketKey = residentProperties.getImageUrls().get(residentProperties.getImageUrls().size() - 1);
            } catch (Exception ignore) {
            }
            if (bucketKey != null) {
                hhseSurveyViewModel.getImageUrl(new OnImageUrlFetched() {
                    @Override
                    public void onImageUrlFetched(String imageUrl) {
                        IndividualSESFormRow individualSESFormRow = (IndividualSESFormRow) binding.surveyFormContainer.getChildAt(residentsList.indexOf(residentProperties));
                        Picasso.get().load(imageUrl).into(individualSESFormRow.getProfileIV());
                    }

                    @Override
                    public void onUrlFetchingFail() {

                    }
                }, bucketKey);
            }
        }
    }

}
