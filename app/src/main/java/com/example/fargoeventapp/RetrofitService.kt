package com.example.fargoeventapp

import android.app.Service
import android.content.Intent
import android.os.IBinder

/** Retrieves Event data **/
class RetrofitService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
