
package com.analysedesgeeks.android.os_service;

import android.graphics.Bitmap;

interface IMediaPlaybackService
{
	long duration();
    void openFile(String path);
    boolean isPlaying();
    void stop();
    void pause();
    void play();
    long seek(long pos);
    String getPath();
    long position();
   
}

