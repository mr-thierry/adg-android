package com.analysedesgeeks.android;

import java.util.List;

import roboguice.application.RoboApplication;
import android.app.Instrumentation;
import android.content.Context;

import com.google.inject.Module;

public class ADGApplication extends RoboApplication {

	public ADGApplication() {
		super();
	}

	public ADGApplication(final Context targetContext) {
		super();
		attachBaseContext(targetContext);
	}

	public ADGApplication(final Instrumentation instrumentation) {
		super();
		attachBaseContext(instrumentation.getTargetContext());
	}

	@Override
	protected void addApplicationModules(final List<Module> modules) {
		modules.add(new ADGModule());
	}

}
