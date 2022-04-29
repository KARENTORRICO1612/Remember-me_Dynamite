package com.cdp.agenda;

//ESCUCHA EL EVENTO DE ALARMA DESDE EL SISTEMA Y LLAMA A LA CLASE
//NotificationService

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.cdp.agenda.NotificationService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, NotificationService.class);
        Toast.makeText(context, "fff:"+intent.getStringExtra("titulo"), Toast.LENGTH_LONG).show();
        service1.putExtra("titulo", intent.getStringExtra("titulo"));
        service1.putExtra("mensaje", intent.getStringExtra("mensaje"));
        service1.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        ContextCompat.startForegroundService(context, service1 );
        Log.d("WALKIRIA", " ALARM RECEIVED!!!");

    }
}
