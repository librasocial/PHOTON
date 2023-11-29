package com.ssf.homevisit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ssf.homevisit.R;
import com.ssf.homevisit.interfaces.SESQuestionResponseOnOptionChange;

import java.util.ArrayList;
import java.util.List;

public class SESQuestionResponseCheckBox extends FrameLayout implements SurveyQuestionResponseView {
    private TextView questionTV;

    public SESQuestionResponseCheckBox(Context context) {
        super(context);
        initView();
    }

    public SESQuestionResponseCheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SESQuestionResponseCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_sescheckbox_quetion, this);
        questionTV = findViewById(R.id.question_text2);
    }

    public void setQuestion(String question) {
        questionTV.setText(question);
    }

    @Override
    public void setQuestionTag(String question) {

    }

    @Override
    public void setOptions(List<String> options) {

    }

    @Override
    public void setListener(SESQuestionResponseView.QuestionSelectionListener listener) {

    }

    @Override
    public List<String> getResponse() {
        List<String> response = new ArrayList<>();
        RadioButton yesRadioButton = (RadioButton) findViewById(R.id.yes_radio_button);
        RadioButton noRadioButton = (RadioButton) findViewById(R.id.no_radio_button);

        if (yesRadioButton.isChecked())
            response.add("Yes");
        if (noRadioButton.isChecked())
            response.add("No");

        return response;
    }

    @Override
    public void setAnswer(List<String> response) {
        if (response.contains("Yes")) {
            RadioButton yesRadioButton = (RadioButton) findViewById(R.id.yes_radio_button);
            yesRadioButton.setChecked(true);
        } else {
            RadioButton noRadioButton = (RadioButton) findViewById(R.id.no_radio_button);
            noRadioButton.setChecked(true);
        }
    }

    public void setOnOptionChangeListener(SESQuestionResponseOnOptionChange onOptionChange) {
        RadioButton yesRadioButton = (RadioButton) findViewById(R.id.yes_radio_button);
        yesRadioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                onOptionChange.onOptionChange("yes");
            else
                onOptionChange.onOptionChange("no");
        });
    }

}
