package com.ssf.homevisit.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CognitoResponse {

    @SerializedName("AuthenticationResult")
    @Expose
    private AuthenticationResult authenticationResult;

    @SerializedName("ChallengeParameters")
    @Expose
    private ChallengeParameters challengeParameters;


    public AuthenticationResult getAuthenticationResult() {
        return authenticationResult;
    }

    public void setAuthenticationResult(AuthenticationResult authenticationResult) {
        this.authenticationResult = authenticationResult;
    }

    public ChallengeParameters getChallengeParameters() {
        return challengeParameters;
    }

    public void setChallengeParameters(ChallengeParameters challengeParameters) {
        this.challengeParameters = challengeParameters;
    }

    public class ChallengeParameters {


    }
}
