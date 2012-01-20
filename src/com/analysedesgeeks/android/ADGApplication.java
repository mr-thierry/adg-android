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

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(formUri = "http://www.bugsense.com/api/acra?api_key=7b6bed9b",
        formKey = "",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class ADGApplication extends Application {

}
