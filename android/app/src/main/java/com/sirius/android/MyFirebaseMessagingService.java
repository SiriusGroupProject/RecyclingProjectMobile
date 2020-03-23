package com.sirius.android;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map data = remoteMessage.getData();
        if (data != null) {
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
