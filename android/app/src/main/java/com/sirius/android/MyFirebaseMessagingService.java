package com.sirius.android;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */

    @Override
    public void onNewToken(String token) {
        Log.d("XX", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("XXXXXXXXXXXXXXXXXXXXXXX","yy");
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();
        if (data != null) {
            Log.e("XXXXXXXXXXXXXXXXXXXXXXX",data.toString());
            // TODO: handle your message and data
            //sendMessageNotification(message, messageId);
        }
    }
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
    private void sendMessageNotification(String msg, long messageId) {
        // TODO: show notification using NotificationCompat
    }
}
