package com.analysedesgeeks.android;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=7b6bed9b",
        formKey = "",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class ADGApplication extends Application {

}
