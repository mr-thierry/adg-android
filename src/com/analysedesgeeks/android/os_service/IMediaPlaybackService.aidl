
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

