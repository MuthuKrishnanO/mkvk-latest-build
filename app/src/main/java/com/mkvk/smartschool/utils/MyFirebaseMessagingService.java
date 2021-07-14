package com.mkvk.smartschool.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import com.mkvk.smartschool.students.NotificationList;
import com.mkvk.smartschool.students.StudentAttendance;
import com.mkvk.smartschool.students.StudentExaminationList;
import com.mkvk.smartschool.students.StudentFees;
import com.mkvk.smartschool.students.StudentHomework;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mkvk.smartschool.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private void pushNotification(Intent intent,String title, String message) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "alert_001")

                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setOngoing(false)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(false);

        if(Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
            mBuilder.setSmallIcon(R.drawable.notificatin);
            mBuilder.setColorized(true);
            mBuilder.setColor(Color.parseColor("#f38000"));
            Log.e("MANUFACTURER", Build.MANUFACTURER);
        } else {
            mBuilder.setSmallIcon(R.drawable.notificatin);
        }

        if (mNotificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();

                NotificationChannel channel = new NotificationChannel("alert_001",
                        "Alert",
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setSound(defaultSoundUri, attributes);

                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channel.getId());
            }
            mNotificationManager.notify(1, mBuilder.build());
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "Status: Message Received");
        if (remoteMessage != null)
        {
            Log.e(TAG, "Data: "+remoteMessage.getData().toString());
            Map<String,String> data = remoteMessage.getData();
            Log.e("MY NOTIFICATION== ", data.toString());
            String mTitle = data.get("title");
            String mMessage = data.get("body");
            String action = data.get("action");
            Intent intent;

            try {
                switch (action) {
                    case "fees" :
                        intent = new Intent(this, StudentFees.class);
                        break;

                    case "absent" :
                        intent = new Intent(this, StudentAttendance.class);
                        break;

                    case "exam" :
                        intent = new Intent(this, StudentExaminationList.class);
                        break;

                    case "homework" :
                        intent = new Intent(this, StudentHomework.class);
                        break;

                    default:
                        intent = new Intent(this, NotificationList.class);
                        break;
                }
            } catch (NullPointerException e) {
                intent = new Intent(this, NotificationList.class);
            }
           // mMessage= "The white background issue with notification messages is known and is being fixed, the fix should be released soon. Another option is to use data messages and manage the creation of the notification yourself in the onMessageReceived callback.";
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pushNotification(intent,mTitle,mMessage);


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            DatabaseHelper dataBaseHelper = new DatabaseHelper(MyFirebaseMessagingService.this);
            //dataBaseHelper.insertUserDetails("cars", "dsgsgs", "0", currentDateandTime);
            dataBaseHelper.insertUserDetails(mTitle,mMessage,"0",currentDateandTime);
            dataBaseHelper.close();



        }
    }

}
