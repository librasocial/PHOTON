package com.ssf.homevisit.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ssf.homevisit.R;

public class SESQuestionResponseTextView extends FrameLayout {
    private TextView questionTV;
    private TextView answerTV;

    public SESQuestionResponseTextView(Context context) {
        super(context);
        initView();
    }

    public SESQuestionResponseTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SESQuestionResponseTextView,
                0, 0);

        try {
            questionTV.setText(a.getString(R.styleable.SESQuestionResponseTextView_question_text));

            if (a.getString(R.styleable.SESQuestionResponseTextView_answer_background).equals("enclosed")) {
                answerTV.setBackgroundResource(R.drawable.edit_bg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }
    }

    public SESQuestionResponseTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.layout_sestextview_quetion, this);
        questionTV = findViewById(R.id.question_text1);
        answerTV = findViewById(R.id.setAnswer1);
    }

    public void setQuestion(String question) {
        questionTV.setText(question);
    }

    public void setAnswer(String answer) {
        answerTV.setText(answer);
    }
}
