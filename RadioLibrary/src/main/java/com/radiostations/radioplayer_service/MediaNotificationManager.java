package com.radiostations.radioplayer_service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.radiostations.R;
import com.radiostations.Radio_Activity;

import java.util.Objects;


class MediaNotificationManager {

    private static final int NOTIFICATION_ID = 555;
    private final RadioService service;

    private final NotificationManagerCompat notificationManager;

    MediaNotificationManager(RadioService service) {
        this.service = service;
        notificationManager = NotificationManagerCompat.from(service);
    }

    void startNotify(String playbackStatus, Bitmap bitmap, String radioName, String radioDetail) {

        int icon = R.drawable.ic_pause_small;
        Intent playbackAction = new Intent(service, RadioService.class);
        playbackAction.setAction(RadioService.ACTION_PAUSE);
        PendingIntent action = PendingIntent.getService(service, 1, playbackAction, 0);

        if (playbackStatus.equals(PlaybackStatus.PAUSED)) {
            icon = R.drawable.ic_play_small;
            playbackAction.setAction(RadioService.ACTION_PLAY);
            action = PendingIntent.getService(service, 2, playbackAction, 0);
        }

        Intent stopIntent = new Intent(service, RadioService.class);
        stopIntent.setAction(RadioService.ACTION_STOP);
        PendingIntent stopAction = PendingIntent.getService(service, 3, stopIntent, 0);
        Intent intent = new Intent(service, Radio_Activity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(service, 0, intent, 0);
        notificationManager.cancel(NOTIFICATION_ID);
        String PRIMARY_CHANNEL = "PRIMARY_CHANNEL_ID";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            String PRIMARY_CHANNEL_NAME = "PRIMARY";
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL, PRIMARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            Objects.requireNonNull(manager).createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, PRIMARY_CHANNEL)
                .setAutoCancel(false)
                .setContentTitle(radioName)
                .setContentText(radioDetail)
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(icon, "Pause", action)
                .addAction(R.drawable.ic_stop_small, "Stop", stopAction)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis());

        //Exception in huawei lollipop phones
        try {
            builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(service.getMediaSession().getSessionToken())
                    .setShowActionsInCompactView(0, 1)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(stopAction));
        } catch (Exception e) {
            e.printStackTrace();
        }
        service.startForeground(NOTIFICATION_ID, builder.build());
    }

    void cancelNotify() {
        service.stopForeground(true);
    }
}
