package com.cdp.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class pantalla extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent intent= new Intent(pantalla.this, LoginActivity.class);
                startActivity(intent);//para que se pase a la otra actividad
                finish();// se termina la actividad y se pasa a la otra


            }
        };
        Timer tiempo = new Timer();
        tiempo.schedule(tarea, 2000);
    }
}