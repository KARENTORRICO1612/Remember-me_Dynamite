package com.cdp.agenda;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.cdp.agenda.MainActivity;
import com.cdp.agenda.NuevoActivity;
import com.cdp.agenda.R;
import com.google.android.material.textfield.TextInputEditText;

//Muestra la notificación en la barra de estado

/*
* Bundle b = getIntent().getExtras();
String yourvalue = getIntent.getExtras.getString("key");
* */

public class NotificationService extends IntentService {

    /*private static String EXTRA_NOTIFICATION_ID = "extra notificacion"; //+++++++++
    private static String ACTION_SNOOZE = "boton de accion";//++++++++++++    */
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private static int NOTIFICATION_ID = 1;

    Notification notification;


    public NotificationService(String name) {
        super(name);
    }

    public NotificationService() {
        super("SERVICE");
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent2) {
        String NOTIFICATION_CHANNEL_ID = getApplicationContext().getString(R.string.app_name);
        Context context = this.getApplicationContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, MainActivity.class); //con este "intent" podemos dirigirnos a otra actividad
        Resources res = this.getResources();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); //para el sonido de la alarma

        String tituloda = "sin nada";
        String message = "sin nada";
        //String tituloda2 = "sin nada";
        //String message2 = "sin nada";
        //String message = intent2.getStringExtra();

        //si el sistema esta funcionando con versiones que esta por encima de Android Oreo "O"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final int NOTIFY_ID = 0; // ID of notification
           //final int NOTIFY_ID2 = 1;
            String id = NOTIFICATION_CHANNEL_ID; // default_channel_id
            String id2 = NOTIFICATION_CHANNEL_ID;
            String title = NOTIFICATION_CHANNEL_ID; // Default Channel
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;
            NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notifManager == null) {
                notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }


            builder = new NotificationCompat.Builder(context, id); //context = this.getAplicationContext(), id = NOTIFICATION_CHANNEL_ID
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT); //la pantalla de donde se realizo la configuracion de la notificacion
            //"pendingIntent" pendiente de que el usuario haga click en la notificación
            tituloda = intent2.getStringExtra("titulo");
            message = intent2.getStringExtra("mensaje");
            String GROUP_NOTIFICATIONS = "Grupo de notificaciones";//-------intentando grupo de notificaciones

            /*//----------Boton de accion notificacion
            Intent snoozeIntent = new Intent(this, VerActivity.class);
            snoozeIntent.setAction(ACTION_SNOOZE);
            snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
            PendingIntent snoozePendingIntent =
                    PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);
            //----------para el boton de accion*/

            builder.setContentTitle(tituloda).setCategory(Notification.CATEGORY_SERVICE) //aqui para el titulo en android 8 o superior
                    .setSmallIcon(R.drawable.ic_stat_name)   // required
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_stat_name))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent)
                    //.addAction(R.drawable.ic_stat_name,getString(R.string.Ver_Informacion), snoozePendingIntent)//+++++++)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            Notification notification = builder.build(); //viene de "notificationManager.notify(idAlarm, mNotifyBuilder.build());"
            notifManager.notify(NOTIFY_ID, notification); //viene de "notificationManager.notify(idAlarm, mNotifyBuilder.build());"

            startForeground(1, notification);

            //----------------intentando crear 2 canales???
            /*builder = new NotificationCompat.Builder(context, id2); //id = NOTIFICATION_CHANNEL_ID
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT); //la pantalla de donde se realizo la configuracion de la notificacion
            //"pendingIntent" pendiente de que el usuario haga click en la notificación
            tituloda2 = intent2.getStringExtra("titulo");
            message2 = intent2.getStringExtra("mensaje");

            builder.setContentTitle(tituloda2).setCategory(Notification.CATEGORY_SERVICE) //aqui para el titulo en android 8 o superior
                    .setSmallIcon(R.drawable.ic_stat_name)   // required
                    .setContentText(message2)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_stat_name))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent)
                    .setGroup(GROUP_NOTIFICATIONS)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            Notification notification2 = builder.build();
            notifManager.notify(NOTIFY_ID, notification2);

            startForeground(2, notification);

            //-----------------intentando crear 2 canales */

            //y esta para versiones que esten por debajo de Android O
        } else {
            tituloda = intent2.getStringExtra("titulo");
            message = intent2.getStringExtra("mensaje");

            pendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new NotificationCompat.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_stat_name))
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setContentTitle(tituloda).setCategory(Notification.CATEGORY_SERVICE)
                    //.setContentTitle(getApplicationContext().getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE) //titulo notificacion
                    .setContentText(message).build();
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }
}
