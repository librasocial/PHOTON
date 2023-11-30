package com.ssf.homevisit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.ssf.homevisit.R;
import com.ssf.homevisit.models.QuestionResponse;
import com.ssf.homevisit.models.ResidentProperties;
import com.ssf.homevisit.models.SurveyAnswerContext;
import com.ssf.homevisit.models.SurveyAnswersBody;
import com.ssf.homevisit.models.SurveyAnswersResponse;
import com.ssf.homevisit.models.SurveyFilterResponse;

import java.util.ArrayList;
import java.util.List;

public class IndividualSESFormRow extends LinearLayout implements SESQuestionResponseView.QuestionSelectionListener {
    private Context context;
    private View individualForm;
    private SurveyFilterResponse questions;
    private ResidentProperties citizenProperties;

    private String houseUuid;

    public IndividualSESFormRow(Context context) {
        super(context);
        init(context);
    }

    public IndividualSESFormRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IndividualSESFormRow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public IndividualSESFormRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        individualForm = inflater.inflate(R.layout.layout_individual_ses_form, this, true);
        SESQuestionResponseCheckBox disabledCheckBox = findViewById(R.id.disabled);
        SESQuestionResponseView typeOfDisabilityView = findViewById(R.id.type_of_disability);
        typeOfDisabilityView.setEnabled(false);
        disabledCheckBox.setOnOptionChangeListener(selectedOption -> {
            if(selectedOption.equals("yes")){
                typeOfDisabilityView.setEnabled(true);
            }
            else{
                typeOfDisabilityView.setEnabled(false);
            }
        });
    }
    public void setResidentProperties(ResidentProperties citizenProperties) {
        this.citizenProperties = citizenProperties;
        SESQuestionResponseTextView citizenNameTV = findViewById(R.id.resident_name);
        citizenNameTV.setQuestion("Resident Name");
        String firstName = citizenProperties.getFirstName() != null ? citizenProperties.getFirstName() : "";
        String middleName = citizenProperties.getMiddleName() != null ? citizenProperties.getMiddleName() : "";
        String lastName = citizenProperties.getLastName() != null ? citizenProperties.getLastName() : "";
        citizenNameTV.setAnswer(firstName + " " + middleName + " " + lastName);

        SESQuestionResponseTextView ageTV = findViewById(R.id.age);
        ageTV.setQuestion("Age");
        int age = citizenProperties.getAge() != null ? citizenProperties.getAge() : 0;
        ageTV.setAnswer(age + "");

        SESQuestionResponseTextView dobTV = findViewById(R.id.dob);
        dobTV.setQuestion("Date of Birth");
        String dob = citizenProperties.getDateOfBirth() != null ? citizenProperties.getDateOfBirth() : "Not Available";
        dobTV.setAnswer(dob);

        SESQuestionResponseTextView phoneNumberTV = findViewById(R.id.phone_number);
        phoneNumberTV.setQuestion("Phone Number");
        String phoneNumber = citizenProperties.getContact() != null ? citizenProperties.getContact() : "Not Available";
        phoneNumberTV.setAnswer(phoneNumber);

        SESQuestionResponseView relationshipTV = findViewById(R.id.relationship);
        relationshipTV.setAnswer(citizenProperties.getRelationshipWithHead());

        if (citizenProperties.getIsHead()) {
            CheckBox isHeadCB = findViewById(R.id.is_head_cb);
            isHeadCB.setChecked(true);
            relationshipTV.setEnabled(false);
        }


        if (citizenProperties.profileImage != null) {
            ImageView profilIV = findViewById(R.id.photo_iv);
            profilIV.setImageBitmap(citizenProperties.profileImage);
        }

        String gender = citizenProperties.getGender();
        SESQuestionResponseTextView genderResponseView= findViewById(R.id.gender);
        genderResponseView.setAnswer(gender);
    }

    public void setHouseUuid(String uuid){
        houseUuid=uuid;
    }
    public void setQuestions(SurveyFilterResponse surveyFilterResponse) {
        int index = 0;
        questions = surveyFilterResponse;
        for (SurveyFilterResponse.Datum.QuesOption question :
                surveyFilterResponse.getData().get(0).getQuesOptions()) {
            int resourceId = indexToResourceId(index);
            index++;
            if (resourceId == 0) continue;
            SurveyQuestionResponseView surveyQuestionResponseView = findViewById(resourceId);
            surveyQuestionResponseView.setQuestion(question.getQuestion());
            surveyQuestionResponseView.setListener(this);
            surveyQuestionResponseView.setQuestionTag(question.getPropertyName());
            surveyQuestionResponseView.setOptions(question.getChoices());
        }
    }

    public void setParentScroller(NestedScrollView scroller){
        ScrollView scrollView = findViewById(R.id.languages_scroll_view);
        scrollView.setOnTouchListener((view1, motionEvent) -> {
            // Disallow the touch request for parent scroll on touch of child view
            scroller.requestDisallowInterceptTouchEvent(true);
            view1.onTouchEvent(motionEvent);
            return true;
        });
    }

    private int indexToResourceId(int index) {
        switch (index) {
            case 0:
                return 0;
            case 1:
                return R.id.relationship;
            case 2:
                return R.id.marital_status;
            case 3:
                return R.id.blood_group;
            case 4:
                return R.id.religion;
            case 5:
                return R.id.caste;
            case 6:
                return R.id.mother_tongue;
            case 7:
                return R.id.ip_proof_type;
            case 8:
                return R.id.hindi;
            case 9:
                return R.id.kannada;
            case 10:
                return R.id.english;
            case 11:
                return R.id.telugu;
            case 12:
                return R.id.tamil;
            case 13:
                return R.id.malayalam;
            case 14:
                return R.id.marathi;
            case 15:
                return R.id.gujarati;
            case 16:
                return R.id.urdu;
            case 17:
                return R.id.bengali;
            case 18:
                return R.id.odia;
            case 19:
                return R.id.punjabi;
            case 20:
                return R.id.asameese;
            case 21:
                return R.id.education;
            case 22:
                return R.id.education_specification;
            case 23:
                return R.id.present_occupation;
            case 24:
                return R.id.previous_occupation;
            case 25:
                return R.id.working_in_village;
            case 26:
                return R.id.living_in_village;
            case 27:
                return R.id.poverty_status;
            case 28:
                return R.id.annual_income;
            case 29:
                return R.id.source_of_income;
            case 30:
                return R.id.social_security_benefits;
            case 31:
                return R.id.disabled;
            case 32:
                return R.id.type_of_disability;
            default:
                return 0;
        }
    }

    public ImageView getProfileIV() {
        return findViewById(R.id.photo_iv);
    }

    public SurveyAnswersBody getSurveyResponse(){
        SurveyAnswersBody surveyAnswersBody = new SurveyAnswersBody();
        surveyAnswersBody.setSurveyId(questions.getData().get(0).getId());
        SurveyAnswerContext surveyAnswerContext = new SurveyAnswerContext();
        surveyAnswerContext.setMemberId(citizenProperties.getUuid());
        surveyAnswerContext.sethId(houseUuid);
        surveyAnswersBody.setContext(surveyAnswerContext);
        surveyAnswersBody.setSurveyName("Individual");
        surveyAnswersBody.setSurveyType("Individual");
        List<QuestionResponse> questionResponses = new ArrayList<>();

        int index = 0;
        for(SurveyFilterResponse.Datum.QuesOption quesOption : questions.getData().get(0).getQuesOptions()){
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setQuestion(quesOption.getQuestion());
            questionResponse.setPropertyName(quesOption.getPropertyName());
            int resourceId = indexToResourceId(index);
            index++;
            if (resourceId == 0) {
                questionResponses.add(questionResponse);
                continue;
            }
            SurveyQuestionResponseView surveyQuestionResponseView = findViewById(resourceId);
            questionResponse.setResponse(surveyQuestionResponseView.getResponse());
            questionResponses.add(questionResponse);
        }

        surveyAnswersBody.setQuesResponse(questionResponses);
        return surveyAnswersBody;
    }

    public void setSurveyResponse(SurveyAnswersResponse surveyAnswersResponse) {
        int index = 0;
        for (QuestionResponse quesOption : surveyAnswersResponse.getQuesResponse()) {
            int resourceId = indexToResourceId(index);
            index++;
            if (resourceId == 0) continue;
            SurveyQuestionResponseView surveyQuestionResponseView = findViewById(resourceId);
            surveyQuestionResponseView.setAnswer(quesOption.getResponse());
        }
    }

    @Override
    public void onEconomicSelectedSelected(String str) {
        if (str.equalsIgnoreCase("BPL")) {
            onBPLSelected(true);
        } else {
            onBPLSelected(false);
        }


    }

    private void onBPLSelected(boolean selected) {

        SESQuestionResponseView surveyQuestionResponseView = findViewById(indexToResourceId(28));
        surveyQuestionResponseView.setEnabled(!selected);
        surveyQuestionResponseView.clearOptionSelection();
        surveyQuestionResponseView = findViewById(indexToResourceId(29));
        surveyQuestionResponseView.setEnabled(!selected);
        surveyQuestionResponseView.clearOptionSelection();
    }
}
