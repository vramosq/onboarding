package com.nova.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.facephi.fphiselphidwidgetcore.WidgetSelphIDConfiguration;
import com.facephi.fphiselphidwidgetcore.WidgetSelphIDResult;
import com.facephi.fphiselphidwidgetcore.WidgetSelphIDScanMode;
import com.facephi.fphiwidgetcore.FPhiImage;
import com.facephi.fphiwidgetcore.WidgetConfiguration;
import com.facephi.fphiwidgetcore.WidgetLivenessMode;
import com.facephi.fphiwidgetcore.WidgetMode;
import com.facephi.fphiwidgetcore.WidgetResult;
import com.facephi.selphid.Widget;
import com.nova.onboarding.interfaces.AutenticarApi;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final int RESULT_CODE = 1;
    private Bitmap frontBitmap, backBitmap, faceBitmap;
    private int cedula, selfie;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        cedula = intent.getIntExtra("cedula", 0);
        selfie = intent.getIntExtra("selfie", 0);

        if(cedula > 0){
            captureID();
        }else if(selfie > 0){
            captureSelfie();
        }

        //cedula = getIntent().getStringExtra("cedula");  //borrar
        //selfie = getIntent().getStringExtra("selfie");

        /*bundle = getIntent().getExtras();   //borrar
        if(bundle != null) {

            cedula = bundle.getString("cedula");
            selfie = bundle.getString("selfie");

            if(cedula != null){
                captureID();
            }else if(selfie != null)){
                captureSelfie();
            }

        }*/
    }

    private void captureID() {

        // Create Widget configuration
        WidgetSelphIDConfiguration widgetConfiguration = new WidgetSelphIDConfiguration();
        // Add the license from ConfigWrapper

        widgetConfiguration.setLicense(ConfigWrapper.LICENSE);
        // Set the resource file name in assets folder
        widgetConfiguration.setResourcesPath("fphi-selphid-widget-resources-selphid-1.0.zip");
        // Enable image return and wizard mode

        widgetConfiguration.enableImages(true);
        widgetConfiguration.setWizardMode(true);

        //widgetConfiguration.setShowAfterCapture(true);
        // To obtain data in a more accurate way enter the locale in the specificData property
        widgetConfiguration.setScanMode(WidgetSelphIDScanMode.SMSearch);
        widgetConfiguration.setSpecificData("CR|<ALL>");

        widgetConfiguration.setFullscreen(true);
        widgetConfiguration.setShowAfterCapture(true);

        // Launch the Widget adding widget configuration
        Intent intent = new Intent(this, Widget.class);
        intent.putExtra("configuration", widgetConfiguration);
        startActivityForResult(intent, RESULT_CODE);

    }

    private void captureSelfie() {

        Intent intent = new Intent(getBaseContext(), com.facephi.selphi.Widget.class);

        // TODO this is the construction of the configuration object needed to launch the widget. IMPORTANT!! You need to change the resourcesPath file to the proper one before running this example
        WidgetConfiguration conf = new WidgetConfiguration(WidgetMode.Authenticate, "fphi-selphi-widget-resources-selphi-live-1.2.zip");

        // TODO these methods set different functionality for the Widget
        conf.setLivenessMode(WidgetLivenessMode.LIVENESS_BLINK);
        conf.setFullscreen(true);
        conf.setTutorialFlag(false);
        // TODO enables the return of the list images processed during the execution.
        conf.logImages(false);

        // TODO sets the name of the class that implements the event listener for the widget
        // This is optional
        conf.setIFPhiWidgetEventListener_classname("com.nova.onboarding.FPhiWidgetEventListener");

        intent.putExtra("configuration", conf);
        startActivityForResult(intent, RESULT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == -1) {
            finish();
        }

        if (data == null) return;

        if (requestCode != RESULT_CODE) return;

        Object widgetResult = data.getParcelableExtra("result");
        if(widgetResult.getClass().equals(WidgetSelphIDResult.class)){

            obtenerResultadoID((WidgetSelphIDResult) widgetResult);

        }else{

            obtenerResultadoSelfie((WidgetResult) widgetResult);

        }

    }

    private void obtenerResultadoID(WidgetSelphIDResult data){

        // Get the widget result from intent data
        WidgetSelphIDResult widgetResult = data;
        if (widgetResult == null && widgetResult.getException() != null) return;

        String tokenFaceImage = widgetResult.getTokenFaceImage();
        Informacion.getInstance().setTemplateFaceImage(tokenFaceImage);

        // Read the front image from widget result
        frontBitmap = widgetResult.getDocumentFrontImage();
        if (frontBitmap != null) {
            //frontImageView.setImageBitmap(frontBitmap);

            frontBitmap = decodeSampledBitmapFromResource(frontBitmap, 800, 600);
            Informacion.getInstance().setFrontImageView(frontBitmap);
        }

        // Read the back image from widget result
        backBitmap = widgetResult.getDocumentBackImage();
        if (backBitmap != null) {
            //backImageView.setImageBitmap(backBitmap);

            Informacion.getInstance().setBackImageView(backBitmap);
        }

        // Read the face image from widget result
        faceBitmap = widgetResult.getFaceImage();
        if (faceBitmap != null) {
            //faceImageView.setImageBitmap(faceBitmap);

            Informacion.getInstance().setFaceImageView(faceBitmap);
        }

        // Render OCR data in Log
        readOcrData(widgetResult.ocrResults);

        Intent intent = new Intent(this, CapturaSelfieCedulaActivity.class);
        startActivity(intent);

    }

    private void obtenerResultadoSelfie(WidgetResult data){

        WidgetResult wResult = data;
        if (wResult == null) {
            return;
        }

        // Processes the widget result message.
        if (RESULT_CODE == RESULT_CANCELED) {
            if (wResult.getException() != null) {
                if(wResult.getException().getExceptionType() != null){
                    String message = "";
                    switch(wResult.getException().getExceptionType()) {
                        case StoppedManually:
                            message = getResources().getString(R.string.message_user_stopped_manually);
                            break;
                        case Timeout:
                            message = getResources().getString(R.string.message_timeout_error);
                            break;
                        case CameraPermissionDenied:
                            message = getResources().getString(R.string.message_camera_permission_error);
                            break;
                        case SettingsPermissionDenied:
                            message = getResources().getString(R.string.message_settings_permission_error);
                            break;
                        default:
                            message = getResources().getString(R.string.message_unexpected_error);
                            break;
                    }

                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getBaseContext(), wResult.getException().getMessage(), Toast.LENGTH_LONG ).show();
                }
            }

            return;
        }

        byte[] template = wResult.getTemplateRaw();
        String templateSelfie = Base64.encodeToString(template, Base64.NO_WRAP);
        Informacion.getINSTANCE().setTemplateSelfieImage(templateSelfie);
        FPhiImage fPhiImage = wResult.getBestImage();
        Bitmap bitmapSelfie = fPhiImage.getImage();
        Informacion.getINSTANCE().setFrontSelfie(bitmapSelfie);

        Intent intent = new Intent(this, CapturaSelfieCedulaActivity.class);
        startActivity(intent);

    }

    private Bitmap decodeSampledBitmapFromResource(
            Bitmap bitmap, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = bitmap.getHeight()*bitmap.getDensity();
        final int width = bitmap.getWidth()*bitmap.getDensity();
        //int inSampleSize = 1;

        final int maxSize = 960;
        int outWidth = reqWidth;
        int outHeight = reqHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);

        //bitmap = Bitmap.createScaledBitmap(bitmap, 800, 600, false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 15, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        //options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        return bmp;

    }

    private void readOcrData(HashMap<String, String> ocrResults) {
        if (ocrResults == null) return;

        Informacion.getInstance().setNombre(ocrResults.get("FirstName"));
        Informacion.getInstance().setFecha_expiracion(ocrResults.get("DateOfExpiry"));
        Informacion.getInstance().setFecha_nacimiento(ocrResults.get("DateOfBirth"));
        Informacion.getInstance().setApellidos(ocrResults.get("LastName"));
        Informacion.getInstance().setNacionalidad(ocrResults.get("Issuer"));
        Informacion.getInstance().setNum_identificacion(ocrResults.get("DocumentNumber"));

    }

}