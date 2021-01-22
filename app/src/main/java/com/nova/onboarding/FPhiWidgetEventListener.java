package com.nova.onboarding;

import androidx.annotation.NonNull;
import android.util.Log;

import com.facephi.fphiwidgetcore.IFPhiWidgetEventListener;


/*
TODO: This class implements the IFPhiWidgetEventListener interface which is necessary to receive event from the widget
 */
public class FPhiWidgetEventListener implements IFPhiWidgetEventListener {
    @Override
    public void onEvent(@NonNull long time, @NonNull String type, @NonNull String info) {

        /*
        TODO: This function is called by the widget every time there is an important event in the widget.
        - time is the time of the event (codified as Unix time in milliseconds)
        - type is the event type
        - info is the additional information for this particular event
         */

        //TODO: Place here the code for handling each event

        // Logging the event for demonstration purposes
        Log.i("FPhiWidget", "onEvent: ("+time+"ms) " + type + " - " + info);
    }
}
