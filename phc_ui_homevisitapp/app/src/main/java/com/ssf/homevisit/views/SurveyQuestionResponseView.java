package com.ssf.homevisit.views;

import java.util.List;

public interface SurveyQuestionResponseView {
    public void setQuestion(String question);

    public void setQuestionTag(String question);

    public void setOptions(List<String> options);

    public void setListener(SESQuestionResponseView.QuestionSelectionListener listener);

    public List<String> getResponse();

    public void setAnswer(List<String> response);
}
