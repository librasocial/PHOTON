package com.ssf.homevisit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ssf.homevisit.R;

public class SESQuestionEditTextView extends FrameLayout {
    private TextView questionTV;
    private EditText answerEditText;

    public SESQuestionEditTextView(@NonNull Context context) {
        super(context);
        initView();
    }

    public SESQuestionEditTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SESQuestionEditTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_composit_ses_que_edittext, this);
        questionTV = findViewById(R.id.question_text);
        answerEditText = findViewById(R.id.answer_edit_text);
    }
}
