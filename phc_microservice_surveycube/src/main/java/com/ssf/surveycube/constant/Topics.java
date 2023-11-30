package com.ssf.surveycube.constant;

public enum Topics {
    SES("ses"),
    RMNCHA("rmncha"),
    NCD("ncd"),
    EMRPATEINT("emrPateint");

    private String topic;

    private Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic(){
       return this.topic;
    }
}
