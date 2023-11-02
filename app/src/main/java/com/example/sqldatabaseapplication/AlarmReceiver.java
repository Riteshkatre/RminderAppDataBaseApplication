package com.example.sqldatabaseapplication;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sqldatabaseapplication.Fragment.UpcomingFragment;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer music = MediaPlayer.create(context, R.raw.ringtone);
        music.start();

        String description = intent.getStringExtra("description");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");


        Intent nextActivity = new Intent(context, MainActivity.class);
        nextActivity.putExtra("from","notification");
        nextActivity.putExtra("description", description);
        nextActivity.putExtra("date", date);
        nextActivity.putExtra("time", time);
        nextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        context.startActivity(nextActivity);





        int notificationId = (int) System.currentTimeMillis(); // Unique ID for each notification

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, nextActivity, PendingIntent.FLAG_IMMUTABLE);
        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.ringtone);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "androidKnowledge")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("Reminder")
                .setContentText("Its Time to wake up"+" : "+description)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(notificationId, builder.build());




    }




}