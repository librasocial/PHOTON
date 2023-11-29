package com.ssf.homevisit.alerts;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Window;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.squareup.picasso.Picasso;
import com.ssf.homevisit.R;
import com.ssf.homevisit.adapters.HouseholdsAdapter;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.LayoutHhseSurveyAlertBinding;
import com.ssf.homevisit.factories.HHSESurveyAlertViewModelFactory;
import com.ssf.homevisit.interfaces.OnImageUrlFetched;
import com.ssf.homevisit.interfaces.OnSureveyAnswerPosted;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.QuestionResponse;
import com.ssf.homevisit.models.SurveyAnswersBody;
import com.ssf.homevisit.models.SurveyAnswersResponse;
import com.ssf.homevisit.models.SurveyFilterResponse;
import com.ssf.homevisit.utils.ProgressDialog;
import com.ssf.homevisit.viewmodel.HHSESurveyAlertViewModel;
import com.ssf.homevisit.views.SESQuestionResponseView;
import com.ssf.homevisit.views.SurveyQuestionResponseView;

import java.util.ArrayList;
import java.util.List;

public class HHSESurveyAlert {

    private static Context context;
    private static com.ssf.homevisit.alerts.HHSESurveyAlert alert;
    private HHSESurveyAlertViewModel hhseSurveyViewModel;
    private Dialog dialog3;
    private HouseholdsAdapter householdsAdapter;
    private ArrayList<HouseHoldProperties> nearbyHouseholdList;
    private LayoutHhseSurveyAlertBinding binding;
    private boolean showingNearby = true;
    private boolean showingSearched = true;
    private Dialog currentDialog;
    private String villageId;
    private Fragment currentFragment;

    private SurveyFilterResponse surveyQuestions;
    private boolean goToIndividualSurvey = false;
    private HouseHoldProperties householdProperties;

    /**
     * Get the instance of UIController
     *
     * @return
     */


    public static HHSESurveyAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (com.ssf.homevisit.alerts.HouseholdAlert.class) {
                if (alert == null) {
                    alert = new HHSESurveyAlert();
                    context = mcontext;

                }
            }
        }
        return alert;
    }


    @SuppressLint("SetTextI18n")
    public void displayMappingAlert(Fragment fragment, HouseHoldProperties houseHoldProperties) {
        this.householdProperties = houseHoldProperties;
        currentFragment = fragment;
        dialog3 = new Dialog(AppController.getInstance().getMainActivity());
        binding = DataBindingUtil.inflate(dialog3.getLayoutInflater(), R.layout.layout_hhse_survey_alert, null, false);
        hhseSurveyViewModel = new ViewModelProvider((ViewModelStoreOwner) context, new HHSESurveyAlertViewModelFactory(((FragmentActivity) context).getApplication(), houseHoldProperties)).get(HHSESurveyAlertViewModel.class);
        Rect displayRectangle = new Rect();
        dialog3.getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        binding.getRoot().setMinimumWidth((int) (displayRectangle.width() * 0.9f)); // Height
        binding.getRoot().setMinimumHeight((int) (displayRectangle.height() * 0.9f)); // Height
        currentDialog = dialog3;
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog3.setContentView(binding.getRoot());
        dialog3.show();
        binding.hhNumberValueTv.setText(houseHoldProperties.getHouseID());
        binding.houseHeadName.setText(houseHoldProperties.getHouseHeadName());
        setImage(binding.photoIv);
        binding.ivClose.setOnClickListener(v -> {
            dismiss();
        });
        binding.saveAndDoResident.setOnClickListener(v -> {
            saveSurvey();
            this.goToIndividualSurvey = true;
        });
        binding.cancel.setOnClickListener(v -> {
            dismiss();
        });

        binding.saveSurvey.setOnClickListener(v -> {
            saveSurvey();
            this.goToIndividualSurvey = false;
        });

        hhseSurveyViewModel.getSurveyResponse(houseHoldProperties.getUuid(), "HouseHold").observe(fragment, new Observer<SurveyAnswersResponse>() {
            @Override
            public void onChanged(SurveyAnswersResponse surveyAnswersResponse) {
                if (surveyAnswersResponse != null) {
                    int index = 0;
                    for (QuestionResponse quesOption : surveyAnswersResponse.getQuesResponse()) {
                        int resourceId = questionIndexToRId(index);
                        if (resourceId == 0) continue;
                        SESQuestionResponseView sesQuestionResponseView = dialog3.findViewById(resourceId);
                        sesQuestionResponseView.setAnswer(quesOption.getResponse());
                        index++;
                    }
                    binding.saveSurvey.setOnClickListener(view -> {
                        UIController.getInstance().displayToastMessage(context, "Survey has been already responded by " + surveyAnswersResponse.getConductedBy() + "\n cannot upload again.");
                    });
                    binding.saveAndDoResident.setOnClickListener(view -> {
                        UIController.getInstance().displayToastMessage(context, "Survey has been already responded by " + surveyAnswersResponse.getConductedBy() + "\n cannot upload again.");
                        UIController.getInstance().displayToastMessage(context, "Continuing to resident survey without saving.");
                        IndividualSESurveyAlert.getInstance(context).displayMappingAlert(currentFragment, householdProperties);
                    });
                }
            }
        });

        hhseSurveyViewModel.getSurveyMaster("HouseHold").observe(fragment, new Observer<SurveyFilterResponse>() {
            @Override
            public void onChanged(SurveyFilterResponse surveyFilterResponse) {
                if (surveyFilterResponse == null) {
                    UIController.getInstance().displayToastMessage(context, "Cannot get Questions");
                }
                surveyQuestions = surveyFilterResponse;
                int index = 0;
                if (surveyFilterResponse.getData() != null && surveyFilterResponse.getData().size() > 0) {
                    for (SurveyFilterResponse.Datum.QuesOption quesOption : surveyFilterResponse.getData().get(0).getQuesOptions()) {
                        int resourceId = questionIndexToRId(index);
                        if (resourceId == 0) continue;
                        SESQuestionResponseView sesQuestionResponseView = dialog3.findViewById(resourceId);
                        sesQuestionResponseView.setQuestion(quesOption.getQuestion());
                        sesQuestionResponseView.setOptions(quesOption.getChoices());
                        index++;
                    }
                }
            }
        });


    }

    private void setImage(ImageView photoIv) {
        downloadImages(photoIv);
    }

    private void downloadImages(ImageView photoIv) {

        if (householdProperties.getHouseHeadImageUrls() == null || householdProperties.getHouseHeadImageUrls().length == 0)
            return;

        String bucketKey = null;

        bucketKey = householdProperties.getHouseHeadImageUrls()[0];

        if (bucketKey != null) {
            hhseSurveyViewModel.getImageUrl(new OnImageUrlFetched() {
                @Override
                public void onImageUrlFetched(String imageUrl) {
                    Picasso.get().load(imageUrl).resize(100, 100).into(photoIv);
                }

                @Override
                public void onUrlFetchingFail() {

                }
            }, bucketKey);
        }
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
                return R.id.type_of_family;
            case 1:
                return R.id.head_religion;
            case 2:
                return R.id.head_category;
            case 3:
                return R.id.mother_tongue;
            case 4:
                return R.id.location_of_hh;
            case 5:
                return R.id.appropriate_option_of_hh;
            case 6:
                return R.id.house_ownership;
            case 7:
                return R.id.type_of_house;
            case 8:
                return R.id.living_rooms;
            case 9:
                return R.id.cross_ventilation;
            case 10:
                return R.id.electricity;
            case 11:
                return R.id.kitchen_type;
            case 12:
                return R.id.smoke_vent;
            case 13:
                return R.id.fuel_used;
            case 14:
                return R.id.drinking_water;
            case 15:
                return R.id.everyday_use;
            case 16:
                return R.id.bathing_area;
            case 17:
                return R.id.toilet_facility;
            case 18:
                return R.id.drainage_system;
            case 19:
                return R.id.health_worker_visit;
            case 20:
                return R.id.health_care_facility;
            case 21:
                return R.id.type_of_road;
        }
        return 0;
    }

    private void saveSurvey() {
        if (surveyQuestions == null) {
            UIController.getInstance().displayToastMessage(context, "Cannot save until Questions are loaded");
            return;
        }
        ProgressDialog.getInstance(context).display();
        ProgressDialog.getInstance(context).setStatus("Posting Review");
        hhseSurveyViewModel.postSurveyAnswers(surveyQuestions.getData().get(0).getId(), householdProperties.getUuid(),getSurveyAnswersBody(), new OnSureveyAnswerPosted() {
            @Override
            public void onPosted(SurveyAnswersResponse surveyAnswersResponse) {
                UIController.getInstance().displayToastMessage(context, "Survey Posted");
                ProgressDialog.dismiss();
                dismiss();
                if (goToIndividualSurvey) {
                    IndividualSESurveyAlert.getInstance(context).displayMappingAlert(currentFragment, householdProperties);
                }
            }

            @Override
            public void onFail(Throwable th) {
                UIController.getInstance().displayToastMessage(context, "Cannot Post Survey");
                ProgressDialog.dismiss();
            }
        });
    }

    private SurveyAnswersBody getSurveyAnswersBody() {

        SurveyAnswersBody surveyAnswersBody = new SurveyAnswersBody();
        surveyAnswersBody.setSurveyId(surveyQuestions.getData().get(0).getId());

        List<QuestionResponse> questionResponses = new ArrayList<>();

        int index = 0;
        for (SurveyFilterResponse.Datum.QuesOption quesOption : surveyQuestions.getData().get(0).getQuesOptions()) {
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setQuestion(quesOption.getQuestion());
            questionResponse.setPropertyName(quesOption.getPropertyName());

            int resourceId = questionIndexToRId(index);
            if (resourceId == 0) continue;
            SurveyQuestionResponseView sesQuestionResponseView = dialog3.findViewById(resourceId);
            questionResponse.setResponse(sesQuestionResponseView.getResponse());
            questionResponses.add(questionResponse);
            index++;
        }

        surveyAnswersBody.setQuesResponse(questionResponses);
        return surveyAnswersBody;
    }
}
