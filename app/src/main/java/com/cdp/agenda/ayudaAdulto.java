package com.cdp.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class ayudaAdulto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda_adulto);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        VideoView mivdeo= findViewById(R.id.videoAdulto);

        String videop="android.resource://"+getPackageName()+"/" + R.raw.adulto ;
        Uri uri=  Uri.parse(videop);
        //Log.println(Log.ERROR, uri.toString(), "...");

        mivdeo.setVideoURI(uri);

        MediaController mediaController=new MediaController(this);
        mivdeo.setMediaController(mediaController);
        mediaController.setAnchorView(mivdeo);

        mivdeo.start();
    }
}