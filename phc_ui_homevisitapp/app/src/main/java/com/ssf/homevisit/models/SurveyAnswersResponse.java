package com.ssf.homevisit.models;

import java.util.List;

public class SurveyAnswersResponse {
    private String surveyId;
    private SurveyAnswerContext context;
    private List<QuestionResponse> quesResponse;
    private String conductedBy;
    private Audit audit;


    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public SurveyAnswerContext getContext() {
        return context;
    }

    public void setContext(SurveyAnswerContext context) {
        this.context = context;
    }

    public List<QuestionResponse> getQuesResponse() {
        return quesResponse;
    }

    public void setQuesResponse(List<QuestionResponse> quesResponse) {
        this.quesResponse = quesResponse;
    }

    public String getConductedBy() {
        return conductedBy;
    }

    public void setConductedBy(String conductedBy) {
        this.conductedBy = conductedBy;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }
}
