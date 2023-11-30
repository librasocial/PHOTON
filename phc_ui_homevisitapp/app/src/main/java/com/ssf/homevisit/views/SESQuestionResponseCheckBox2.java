package com.ssf.homevisit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ssf.homevisit.R;

import java.util.ArrayList;
import java.util.List;

public class SESQuestionResponseCheckBox2 extends FrameLayout implements SurveyQuestionResponseView {
    private TextView questionTV;
    LinearLayout checkboxContainer;
    private List<String> options;
    private List<String> previousResponse;

    public SESQuestionResponseCheckBox2(Context context) {
        super(context);
        initView();
    }

    public SESQuestionResponseCheckBox2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SESQuestionResponseCheckBox2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_sescheckbox_quetion2, this);
        questionTV = findViewById(R.id.question_text2);
        checkboxContainer = findViewById(R.id.checkbox_container);
    }

    public void setQuestion(String question) {
        questionTV.setText(question);
    }

    @Override
    public void setQuestionTag(String question) {
        questionTV.setTag(question);
    }

    @Override
    public void setOptions(List<String> options) {
        this.options = options;
        checkboxContainer.removeAllViews();
        for (String option : options) {
            addCheckbox();
        }
        if (previousResponse != null) {
            for (String answer : previousResponse) {
                int index = options.indexOf(answer);
                if (index != -1)
                    ((CheckBox) checkboxContainer.getChildAt(index)).setChecked(true);
            }
        }

    }

    @Override
    public void setListener(SESQuestionResponseView.QuestionSelectionListener listener) {

    }

    private void addCheckbox() {
        CheckBox checkBox = new CheckBox(getContext());
        checkboxContainer.addView(checkBox);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        checkBox.setLayoutParams(layoutParams);
        checkBox.setGravity(Gravity.START);
    }

    @Override
    public List<String> getResponse() {
        List<String> response = new ArrayList<>();
        int index = 0;
        for (String option : options) {
            boolean isChecked = ((CheckBox) checkboxContainer.getChildAt(index)).isChecked();
            if (isChecked) response.add(option);
            index++;
        }
        return response;
    }

    @Override
    public void setAnswer(List<String> response) {
        if (options != null) {
            for (String answer : response) {
                int index = options.indexOf(answer);
                if (index != -1)
                    ((CheckBox) checkboxContainer.getChildAt(index)).setChecked(true);
            }
        }
        this.previousResponse = response;
    }

}
