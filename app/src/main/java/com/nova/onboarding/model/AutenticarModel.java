package com.nova.onboarding.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutenticarModel {

    @SerializedName("documentTemplate")
    @Expose
    public String documentTemplate;

    @SerializedName("FaceTemplate1")
    @Expose
    public String faceTemplate;

    public AutenticarModel(String documentTemplate, String faceTemplate) {
        this.documentTemplate = documentTemplate;
        this.faceTemplate = faceTemplate;
    }

    public String getDocumentTemplate() {
        return documentTemplate;
    }

    public void setDocumentTemplate(String documentTemplate) {
        this.documentTemplate = documentTemplate;
    }

    public String getFaceTemplate() {
        return faceTemplate;
    }

    public void setFaceTemplate(String faceTemplate) {
        this.faceTemplate = faceTemplate;
    }
}
