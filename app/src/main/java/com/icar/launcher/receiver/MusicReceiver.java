package com.icar.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        String action = intent.getAction();

        String album = intent.getStringExtra("album");
        String artist = intent.getStringExtra("artist");
        String track = intent.getStringExtra("track");
        String notify_artistname = intent.getStringExtra("notify_artistname");
        String notify_audioname = intent.getStringExtra("notify_audioname");
        int trackLengthInSec = intent.getIntExtra("length", 0);

        Bundle extras = intent.getExtras();
        Intent i = new Intent("musicInfo");
        // Data you need to pass to activity
        i.putExtra("album", album);
        i.putExtra("artist", artist);
        i.putExtra("track", track);
        i.putExtra("notify_artistname", notify_artistname);
        i.putExtra("notify_audioname", notify_audioname);
        i.putExtra("trackLengthInSec", trackLengthInSec);

        context.sendBroadcast(i);
    }
}
