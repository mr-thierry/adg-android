/*
 * Copyright (C) 2012 Thierry-Dimitri Roy <thierryd@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.analysedesgeeks.android;

import com.analysedesgeeks.android.service.ConnectionService;
import com.analysedesgeeks.android.service.DatabaseService;
import com.analysedesgeeks.android.service.DownloadService;
import com.analysedesgeeks.android.service.RssService;
import com.analysedesgeeks.android.service.impl.ConnectionServiceImpl;
import com.analysedesgeeks.android.service.impl.DatabaseServiceImpl;
import com.analysedesgeeks.android.service.impl.DownloadServiceImpl;
import com.analysedesgeeks.android.service.impl.RssServiceImpl;
import com.google.inject.AbstractModule;

public class ADGModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConnectionService.class).to(ConnectionServiceImpl.class);
		bind(DatabaseService.class).to(DatabaseServiceImpl.class);
		bind(DownloadService.class).to(DownloadServiceImpl.class);
		bind(RssService.class).to(RssServiceImpl.class);
	}

}
