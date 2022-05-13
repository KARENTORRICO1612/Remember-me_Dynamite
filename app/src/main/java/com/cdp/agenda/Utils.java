package com.cdp.agenda;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static android.content.Context.ALARM_SERVICE;

public class Utils {

    public static void setAlarm(int i, Long timestamp, Context ctx, String título, String descripción, String fecha, String hora, String dirección) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent;
        alarmIntent.putExtra("título", título);
        alarmIntent.putExtra("fecha", fecha);
        alarmIntent.putExtra("hora", hora);
        alarmIntent.putExtra("dirección", dirección);
        alarmIntent.putExtra("descripción", descripción);

        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }
}
