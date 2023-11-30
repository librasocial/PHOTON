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

import com.squareup.picasso.Picasso;
import com.ssf.homevisit.R;
import com.ssf.homevisit.adapters.IndividualSESFormAdapter;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.LayoutIndiSeSurveyAlertBinding;
import com.ssf.homevisit.factories.HHSESurveyAlertViewModelFactory;
import com.ssf.homevisit.interfaces.OnImageUrlFetched;
import com.ssf.homevisit.interfaces.OnSureveyAnswerPosted;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.ResidentInHouseHoldResponse;
import com.ssf.homevisit.models.ResidentProperties;
import com.ssf.homevisit.models.SurveyAnswersResponse;
import com.ssf.homevisit.models.SurveyFilterResponse;
import com.ssf.homevisit.utils.ProgressDialog;
import com.ssf.homevisit.viewmodel.HHSESurveyAlertViewModel;
import com.ssf.homevisit.views.IndividualSESFormRow;
import com.ssf.homevisit.views.SESQuestionResponseView;

import java.util.ArrayList;

public class IndividualSESurveyAlert {

    private static Context context;
    private static IndividualSESurveyAlert alert;
    private HHSESurveyAlertViewModel hhseSurveyViewModel;
    private Dialog dialog3;
    private LayoutIndiSeSurveyAlertBinding binding;
    private Dialog currentDialog;
    private String villageId;
    private Fragment currentFragment;

    private SurveyFilterResponse surveyQuestions;
    private ArrayList<ResidentProperties> residentsList;
    private IndividualSESFormAdapter householdsAdapter;
    private HouseHoldProperties householdProperties;

    public static IndividualSESurveyAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (com.ssf.homevisit.alerts.HouseholdAlert.class) {
                if (alert == null) {
                    alert = new IndividualSESurveyAlert();
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
        binding = DataBindingUtil.inflate(dialog3.getLayoutInflater(), R.layout.layout_indi_se_survey_alert, null, false);
        ProgressDialog.getInstance(context).display();
        ProgressDialog.getInstance(context).setStatus("Getting Data");
        hhseSurveyViewModel = new ViewModelProvider(
                (ViewModelStoreOwner) context,
                new HHSESurveyAlertViewModelFactory(((FragmentActivity) context).getApplication(), houseHoldProperties)).get(HHSESurveyAlertViewModel.class);
        hhseSurveyViewModel.setHousehold(houseHoldProperties);
        Rect displayRectangle = new Rect();
        dialog3.getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        binding.getRoot().setMinimumWidth((int) (displayRectangle.width() * 0.9f)); // Height
        binding.getRoot().setMinimumHeight((int) (displayRectangle.height() * 0.9f)); // Height
        currentDialog = dialog3;
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog3.setContentView(binding.getRoot());
        dialog3.show();

        binding.ivClose.setOnClickListener(v -> {
            dismiss();
        });
        binding.cancel.setOnClickListener(v -> {
            dismiss();
        });

        binding.saveSurvey.setOnClickListener(v -> {
            ProgressDialog.getInstance(context).display();
            ProgressDialog.getInstance(context).setStatus("Saving Survey Data");
            saveSurvey();
        });

        ProgressDialog.getInstance(context).display1();
        ProgressDialog.getInstance(context).setStatus("Fetching Survey Data");
        hhseSurveyViewModel.getSurveyMaster("Citizen").observe(fragment, new Observer<SurveyFilterResponse>() {
            @Override
            public void onChanged(SurveyFilterResponse surveyFilterResponse) {
                ProgressDialog.dismiss();
                if (surveyFilterResponse == null) {
                    UIController.getInstance().displayToastMessage(context, "Cannot get Questions");
                    return;
                }
                surveyQuestions = surveyFilterResponse;
                int index = 0;
                for (SurveyFilterResponse.Datum.QuesOption quesOption : surveyFilterResponse.getData().get(0).getQuesOptions()) {
                    int resourceId = questionIndexToRId(index);
                    if (resourceId == 0) continue;
                    SESQuestionResponseView sesQuestionResponseView = dialog3.findViewById(resourceId);
                    sesQuestionResponseView.setQuestionTag(quesOption.getPropertyName());
                    sesQuestionResponseView.setQuestion(quesOption.getQuestion());

                    sesQuestionResponseView.setOptions(quesOption.getChoices());
                    index++;
                }
                getCitizens();
            }
        });


    }

    private void getCitizens() {
        ProgressDialog.getInstance(context).display1();
        ProgressDialog.getInstance(context).setStatus("Fetching Citizens");
        hhseSurveyViewModel.getResidentsInHouseholdData().observe(currentFragment, new Observer<ResidentInHouseHoldResponse>() {
            @Override
            public void onChanged(ResidentInHouseHoldResponse residentInHouseHoldResponse) {
                residentsList = new ArrayList<>();
                binding.surveyFormContainer.removeAllViews();
                int index = 0;
                for (ResidentInHouseHoldResponse.Content content : residentInHouseHoldResponse.getContent()) {
                    residentsList.add(content.getTarget().getProperties());
                    IndividualSESFormRow individualSESFormRow = new IndividualSESFormRow(context);
                    individualSESFormRow.setQuestions(surveyQuestions);
                    individualSESFormRow.setHouseUuid(content.getSource().getProperties().getUuid());
                    individualSESFormRow.setResidentProperties(residentsList.get(residentsList.size() - 1));
                    individualSESFormRow.setParentScroller(binding.scroller);
                    hhseSurveyViewModel.getSurveyResponse(content.getTarget().getProperties().getUuid(), "Citizen").observe(currentFragment, new Observer<SurveyAnswersResponse>() {
                        @Override
                        public void onChanged(SurveyAnswersResponse surveyAnswersResponse) {
                            if (surveyAnswersResponse != null) {
                                int index = -1;
                                int i = 0;
                                for (ResidentProperties residentProperties : residentsList) {
                                    if (residentProperties.getUuid().equals(surveyAnswersResponse.getContext().getMemberId())) {
                                        index = i;
                                        break;
                                    }
                                    i++;
                                }
                                if (index == -1) return;
                                IndividualSESFormRow individualSESFormRow1 = (IndividualSESFormRow) binding.surveyFormContainer.getChildAt(index);
                                individualSESFormRow1.setSurveyResponse(surveyAnswersResponse);
                            }
                        }
                    });
                    binding.surveyFormContainer.addView(individualSESFormRow);
                    index++;
                }
                if (binding.surveyFormContainer.getChildCount() == 0) {
                    UIController.getInstance().displayToastMessage(context, "No resident is found for this HouseHold");
                }
                ProgressDialog.dismiss();
                downloadImages();
            }
        });
    }

    private void downloadImages() {
        for (ResidentProperties residentProperties :
                residentsList) {
            String bucketKey = null;
            try {
                if(residentProperties.getImageUrls().size()>0){
                    bucketKey = residentProperties.getImageUrls().get(0);
                }
                else{bucketKey="";
                }

            } catch (Exception ignore) {
            }
            if (bucketKey != null) {
                if (!bucketKey.equals("")) {
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
                else {
                    IndividualSESFormRow individualSESFormRow = (IndividualSESFormRow) binding.surveyFormContainer.getChildAt(residentsList.indexOf(residentProperties));
                    individualSESFormRow.getProfileIV().setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_place_holder));

                }
            }
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
        }
        return 0;
    }

    private void saveSurvey() {
        if (surveyQuestions == null) {
            UIController.getInstance().displayToastMessage(context, "Cannot save until Questions are loaded");
            return;
        }
        final int[] postedSurveys = {0};
        int totalForms = binding.surveyFormContainer.getChildCount();
        ProgressDialog.getInstance(context).display1();
        ProgressDialog.getInstance(context).setStatus("posted " + postedSurveys[0] + "/" + totalForms);
        for (int i = 0; i < totalForms; i++) {
            IndividualSESFormRow individualSESFormRow = (IndividualSESFormRow) binding.surveyFormContainer.getChildAt(i);

            hhseSurveyViewModel.postIndividualSurveyAnswers(surveyQuestions.getData().get(0).getId(), individualSESFormRow.getSurveyResponse(), new OnSureveyAnswerPosted() {
                @Override
                public void onPosted(SurveyAnswersResponse surveyAnswersResponse) {
                    postedSurveys[0]++;
                    if (postedSurveys[0] == totalForms) {
                        ProgressDialog.dismiss();
                        ProgressDialog.getInstance(context).setStatus("posted " + postedSurveys[0] + "/" + totalForms);
                        UIController.getInstance().displayToastMessage(context, "Survey Response posted");
                        dismiss();
                    } else {
                        ProgressDialog.getInstance(context).setStatus("posted " + postedSurveys[0] + "/" + totalForms);
                    }
                }

                @Override
                public void onFail(Throwable th) {
                    UIController.getInstance().displayToastMessage(context, "Cannot Post Survey");
                    ProgressDialog.dismiss();
                }
            });
        }
    }
}
