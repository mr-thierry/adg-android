/*
 * Copyright (C) 2011 Thierry-Dimitri Roy <thierryd@gmail.com>
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
package com.analysedesgeeks.android.os_service;

import android.graphics.Bitmap;

interface IMediaPlaybackService
{
	long duration();
    void openFile(String path);
    String getPath();
    boolean isPlaying();
    void stop();
    void pause();
    void play();
    long seek(long pos);
    String getDescription();
    void setDescription(String description);
    long position();
    boolean hasPlayed();
   
}

