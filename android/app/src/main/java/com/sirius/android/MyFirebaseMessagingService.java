package com.sirius.android;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        Log.d("XX", "Refreshed token: " + token);
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
        }
    }
    private void sendRegistrationToServer(String token) {

    }
    private void sendMessageNotification(String msg, long messageId) {

    }
}
