package com.nova.onboarding.interfaces;

import com.nova.onboarding.model.AutenticarModel;
import com.nova.onboarding.AutenticarResponse;
import com.nova.onboarding.util.Constant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AutenticarApi {

    String AUTENTICAR_BASE_URL = Constant.BASE_URL;

    @POST("api/selphid/authenticate-facial/document/face-template")
    Call<AutenticarResponse> postAutenticar(@Body AutenticarModel autenticarModel);

}
