package com.nova.onboarding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutenticarResponse {

    @SerializedName("Similarity")
    @Expose
    private double similarity;

    @SerializedName("AuthStatus")
    @Expose
    private String authStatus;

    public AutenticarResponse(){

    }

    public AutenticarResponse(double similarity, String authStatus) {
        this.similarity = similarity;
        this.authStatus = authStatus;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

}
