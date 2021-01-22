package com.nova.onboarding;

import android.graphics.Bitmap;

public class Informacion {

    private static Informacion INSTANCE = null;
    private Bitmap frontImageView, backImageView, faceImageView, frontSelfie;
    private String nombre, fecha_expiracion, fecha_nacimiento, apellidos, nacionalidad, num_identificacion;
    private String templateFaceImage, templateSelfieImage;
    private String similitud;
    private String estado;

    private Informacion() {};

    public static synchronized Informacion getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Informacion();
        }
        return(INSTANCE);
    }

    public static Informacion getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(Informacion INSTANCE) {
        Informacion.INSTANCE = INSTANCE;
    }

    public Bitmap getFrontImageView() {
        return frontImageView;
    }

    public void setFrontImageView(Bitmap frontImageView) {
        this.frontImageView = frontImageView;
    }

    public Bitmap getBackImageView() {
        return backImageView;
    }

    public void setBackImageView(Bitmap backImageView) {
        this.backImageView = backImageView;
    }

    public Bitmap getFaceImageView() {
        return faceImageView;
    }

    public void setFaceImageView(Bitmap faceImageView) {
        this.faceImageView = faceImageView;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_expiracion() {
        return fecha_expiracion;
    }

    public void setFecha_expiracion(String fecha_expiracion) {
        this.fecha_expiracion = fecha_expiracion;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getNum_identificacion() {
        return num_identificacion;
    }

    public void setNum_identificacion(String num_identificacion) {
        this.num_identificacion = num_identificacion;
    }

    public Bitmap getFrontSelfie() {
        return frontSelfie;
    }

    public void setFrontSelfie(Bitmap frontSelfie) {
        this.frontSelfie = frontSelfie;
    }

    public String getTemplateFaceImage() {
        return templateFaceImage;
    }

    public void setTemplateFaceImage(String templateFaceImage) {
        this.templateFaceImage = templateFaceImage;
    }

    public String getTemplateSelfieImage() {
        return templateSelfieImage;
    }

    public void setTemplateSelfieImage(String templateSelfieImage) {
        this.templateSelfieImage = templateSelfieImage;
    }

    public String getSimilitud() {
        return similitud;
    }

    public void setSimilitud(String similitud) {
        this.similitud = similitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
