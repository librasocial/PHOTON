package com.ssf.homevisit.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ssf.homevisit.R;

import java.util.ArrayList;
import java.util.List;

public class SESQuestionResponseView extends FrameLayout implements SurveyQuestionResponseView {
    private TextView questionTV;
    private Spinner optionsSpinner;
    List<String> options;
    private boolean enabled = true;
    private String answer;
    private final String TAG = getClass().getCanonicalName();
    private QuestionSelectionListener questionSelectionListener;


    public SESQuestionResponseView(Context context) {
        super(context);
        initView();
    }

    public SESQuestionResponseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SESQuestionResponseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.layout_ses_quetion, this);
        questionTV = findViewById(R.id.question_text);
        optionsSpinner = findViewById(R.id.options_spinner);
    }

    public void setQuestion(String question) {
        questionTV.setText(question);
    }

    public void setQuestionTag(String tag) {
        questionTV.setTag(tag);
    }

    public void setOptions(List<String> options) {
        this.options = new ArrayList<>();
        this.options.add(0, "Select");
        this.options.addAll(options);
        String[] wee = this.options.toArray(new String[0]);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                getContext(), R.layout.survey_spinner_item, wee);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.survey_spinner_dropdown_item);

        optionsSpinner.setAdapter(spinnerArrayAdapter);
        if (answer != null) {
            optionsSpinner.setSelection(this.options.indexOf(answer));
        }
        if (questionTV.getTag() != null && questionTV.getTag().toString().equalsIgnoreCase("povertyStatus")) {
            optionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (questionSelectionListener != null)
                        questionSelectionListener.onEconomicSelectedSelected(wee[position]);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    if (questionSelectionListener != null)
                        questionSelectionListener.onEconomicSelectedSelected(wee[0]);
                }
            });
        }

    }

    public void clearOptionSelection() {
        optionsSpinner.setSelection(0);
    }

    @Override
    public void setListener(QuestionSelectionListener listener) {
        this.questionSelectionListener = listener;
    }

    public List<String> getResponse() {
        if (!enabled) {
            return new ArrayList<>();
        }
        int selectedIndex = optionsSpinner.getSelectedItemPosition();
        String selectedOption = options.get(optionsSpinner.getSelectedItemPosition());
        List<String> response = new ArrayList<>();
        if (selectedIndex != 0)
            response.add(selectedOption);
        return response;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        optionsSpinner.setEnabled(enabled);
        if (optionsSpinner.getSelectedView() != null)
            optionsSpinner.getSelectedView().setEnabled(enabled);
        int color = enabled ? Color.parseColor("#000000") : Color.parseColor("#33333333");
        questionTV.setTextColor(color);
        optionsSpinner.setAlpha(enabled ? Float.parseFloat("1") : Float.parseFloat("0.1"));
    }

    public void setAnswer(String answer) {
        if (answer == null) {
            return;
        }
        this.answer = answer;
        if (options != null) {
            optionsSpinner.setSelection(options.indexOf(answer));
        }
    }

    public void setAnswer(List<String> response) {
        if (response.size() != 0)
            setAnswer(response.get(0));
    }

    public interface QuestionSelectionListener {
        public void onEconomicSelectedSelected(String str);
    }
}
