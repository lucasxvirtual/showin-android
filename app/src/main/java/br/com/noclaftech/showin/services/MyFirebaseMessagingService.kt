package br.com.noclaftech.showin.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "Firebase"

    override fun onNewToken(p0: String) {
        Log.d(TAG, "Refreshed token: $p0")
    }
}