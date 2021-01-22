package com.nova.onboarding;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import com.facephi.fphiselphidwidgetcore.WidgetSelphIDConfiguration;
import com.facephi.fphiselphidwidgetcore.WidgetSelphIDResult;
import com.facephi.fphiselphidwidgetcore.WidgetSelphIDScanMode;
import com.facephi.selphid.Widget;

public class SelfieID extends AppCompatActivity {

    private final int RESULT_CODE = 1;

    public void capture() {
        // Create Widget configuration
        WidgetSelphIDConfiguration widgetConfiguration = new WidgetSelphIDConfiguration();
        // Add the license from ConfigWrapper

        widgetConfiguration.setLicense(ConfigWrapper.LICENSE);
        // Set the resource file name in assets folder
        widgetConfiguration.setResourcesPath("fphi-selphid-widget-resources-selphid-1.0.zip");
        // Enable image return and wizard mode

        widgetConfiguration.enableImages(true);
        widgetConfiguration.setWizardMode(true);

        widgetConfiguration.setShowAfterCapture(true);
        // To obtain data in a more accurate way enter the locale in the specificData property
        widgetConfiguration.setScanMode(WidgetSelphIDScanMode.SMSearch);
        widgetConfiguration.setSpecificData("CR|<ALL>");

        widgetConfiguration.setFullscreen(true);

        //widgetConfiguration.setDocumentSide(WidgetSelphIDDocumentSide.DSFront);

        // Launch the Widget adding widget configuration
        Intent intent = new Intent(this, Widget.class);
        intent.putExtra("configuration", widgetConfiguration);
        startActivityForResult(intent, RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != RESULT_CODE) return;

        // Get the widget result from intent data
        WidgetSelphIDResult widgetResult = data.getParcelableExtra("result");
        if (widgetResult == null && widgetResult.getException() != null) return;

        // Read the front image from widget result
        Bitmap frontBitmap = widgetResult.getDocumentFrontImage();

        Intent intent = new Intent(this, CapturaSelfieCedulaActivity.class);
        intent.putExtra("configuration", frontBitmap);
        startActivity(intent);


        if (frontBitmap != null) {
            //buttonFrontID.setBackground(new BitmapDrawable(getResources(), frontBitmap));
            //frontImageView.setImageBitmap(frontBitmap);
        }

        // Read the back image from widget result
        Bitmap backBitmap = widgetResult.getDocumentBackImage();
        if (backBitmap != null) {
            //backImageView.setImageBitmap(backBitmap);
        }

        // Read the face image from widget result
        Bitmap faceBitmap = widgetResult.getFaceImage();
        if (faceBitmap != null) {
            //faceImageView.setImageBitmap(faceBitmap);
        }

    }
}
