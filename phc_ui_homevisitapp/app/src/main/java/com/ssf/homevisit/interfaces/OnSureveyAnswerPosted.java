package com.ssf.homevisit.interfaces;

import com.ssf.homevisit.models.SurveyAnswersResponse;

public interface OnSureveyAnswerPosted {
    public void onPosted(SurveyAnswersResponse surveyAnswersResponse);
    public void onFail(Throwable th);

}
