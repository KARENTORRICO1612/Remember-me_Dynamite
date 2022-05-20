package com.cdp.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class ayudaUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda_usuario);

        /*VideoView mivdeo= findViewById(R.id.videoUsuario);

        String videop="android.resource://"+getPackageName()+"/" + R.raw.gato ;
        Uri uri=  Uri.parse(videop);
        //Log.println(Log.ERROR, uri.toString(), "...");

        mivdeo.setVideoURI(uri);

        MediaController mediaController=new MediaController(this);
        mivdeo.setMediaController(mediaController);
        mediaController.setAnchorView(mivdeo);

        mivdeo.start();*/
    }
}