package badebaba.firechat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by badebaba on 11/9/2016.
 */

public class NotificationService extends FirebaseMessagingService {

    private final static String TAG = "OurService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /*
        taking the ids and message info on log to test
         */
        Log.i(TAG, "Message_Id:" + remoteMessage.getMessageId());
        Log.i(TAG, "Message_type" + remoteMessage.getMessageType());
        Log.i(TAG, "Notification_MEssage" + remoteMessage.getNotification());
        Log.i(TAG, "Message_Content:" + remoteMessage.getData());

    }

    /*
    Message sending in form of a notification to the android device
     */

    public void sendnotification(String message) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaulturi = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(this);
        notificationbuilder.setContentTitle("FCM-Chat-Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaulturi)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationbuilder.build());
    }
}
