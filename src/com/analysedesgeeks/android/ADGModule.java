package com.analysedesgeeks.android;

import roboguice.config.AbstractAndroidModule;

import com.analysedesgeeks.android.service.ConnectionService;
import com.analysedesgeeks.android.service.DatabaseService;
import com.analysedesgeeks.android.service.DownloadService;
import com.analysedesgeeks.android.service.RssService;
import com.analysedesgeeks.android.service.impl.ConnectionServiceImpl;
import com.analysedesgeeks.android.service.impl.DatabaseServiceImpl;
import com.analysedesgeeks.android.service.impl.DownloadServiceImpl;
import com.analysedesgeeks.android.service.impl.RssServiceImpl;

public class ADGModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(ConnectionService.class).to(ConnectionServiceImpl.class);
		bind(DatabaseService.class).to(DatabaseServiceImpl.class);
		bind(DownloadService.class).to(DownloadServiceImpl.class);
		bind(RssService.class).to(RssServiceImpl.class);
	}

}
