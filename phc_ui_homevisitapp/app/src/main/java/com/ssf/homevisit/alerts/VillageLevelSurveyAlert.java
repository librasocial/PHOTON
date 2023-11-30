package com.ssf.homevisit.alerts;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Window;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import com.ssf.homevisit.R;
import com.ssf.homevisit.adapters.HouseholdsAdapter;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.LayoutHhseSurveyAlertBinding;
import com.ssf.homevisit.databinding.LayoutVillageLevelSurveyAlertBinding;
import com.ssf.homevisit.factories.HHSESurveyAlertViewModelFactory;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.SurveyFilterResponse;
import com.ssf.homevisit.viewmodel.HHSESurveyAlertViewModel;
import com.ssf.homevisit.views.SESQuestionResponseView;

import java.util.ArrayList;

public class VillageLevelSurveyAlert {

    private static Context context;
    private static VillageLevelSurveyAlert alert;
    private HHSESurveyAlertViewModel hhseSurveyViewModel;
    private Dialog dialog3;
    private HouseholdsAdapter householdsAdapter;
    private ArrayList<HouseHoldProperties> nearbyHouseholdList;
    private LayoutVillageLevelSurveyAlertBinding binding;
    private boolean showingNearby = true;
    private boolean showingSearched = true;
    private Dialog currentDialog;
    private String villageId;
    private Fragment currentFragment;

    /**
     * Get the instance of UIController
     *
     * @return
     */


    public static VillageLevelSurveyAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (HouseholdAlert.class) {
                if (alert == null) {
                    alert = new VillageLevelSurveyAlert();
                    context = mcontext;

                }
            }
        }
        return alert;
    }


    @SuppressLint("SetTextI18n")
    public void displayMappingAlert(Fragment fragment, HouseHoldProperties houseHoldProperties) {
        currentFragment = fragment;
        dialog3 = new Dialog(AppController.getInstance().getMainActivity());
        binding = DataBindingUtil.inflate(dialog3.getLayoutInflater(), R.layout.layout_village_level_survey_alert, null, false);
        hhseSurveyViewModel = new ViewModelProvider(
                (ViewModelStoreOwner) context,
                new HHSESurveyAlertViewModelFactory(((FragmentActivity) context).getApplication(), houseHoldProperties)).get(HHSESurveyAlertViewModel.class);
        Rect displayRectangle = new Rect();
        dialog3.getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        binding.getRoot().setMinimumWidth((int) (displayRectangle.width() * 0.9f)); // Height
        binding.getRoot().setMinimumHeight((int) (displayRectangle.height() * 0.9f)); // Height
        currentDialog = dialog3;
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog3.setContentView(binding.getRoot());
        dialog3.show();



        hhseSurveyViewModel.getSurveyMasterResponse().observe(fragment, new Observer<SurveyFilterResponse>() {
            @Override
            public void onChanged(SurveyFilterResponse surveyFilterResponse) {
                if (surveyFilterResponse == null) {
                    UIController.getInstance().displayToastMessage(context, "Cannot get Questions");
                }
                int index = 0;
                for (SurveyFilterResponse.Datum.QuesOption quesOption : surveyFilterResponse.getData().get(0).getQuesOptions()) {
                    int rId = questionIndexToRId(index);
                    if (rId == 0) continue;
                    SESQuestionResponseView sesQuestionResponseView = dialog3.findViewById(rId);
                    sesQuestionResponseView.setQuestion(quesOption.getQuestion());
                    sesQuestionResponseView.setOptions(quesOption.getChoices());
                    index++;
                }
            }
        });


    }

    public static void dismiss() {
        if (alert != null) {
            alert.currentDialog.dismiss();
            alert = null;
        }
    }

    private int questionIndexToRId(int qestionIndex) {
        switch (qestionIndex) {
            case 0:
                return R.id.water_quality;
            case 1:
                return R.id.air_quality;
            case 2:
                return R.id.drainage_system;
            case 3:
                return R.id.mother_tongue;
            case 4:
                return R.id.location_of_hh;
            case 5:
                return R.id.appropriate_option_of_hh;

        }
        return 0;
    }
}
