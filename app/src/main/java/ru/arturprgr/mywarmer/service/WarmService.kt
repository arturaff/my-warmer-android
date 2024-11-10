package ru.arturprgr.mywarmer.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.Random

class WarmService : Service() {
    private lateinit var thread: Thread
    private var intensity: Int = -1
    private var isStarted: Boolean = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intensity = intent!!.getIntExtra("intensity", 0)
        isStarted = intent.getBooleanExtra("isStarted", false)
        Log.d("WarmerApp", "WarmService: intensity: $intensity, isStarted: $isStarted")
        thread = Thread {
            while (isStarted) for (i in 1..intensity) Random().nextLong().toString()
        }
        thread.start()
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.w("WarmerApp", "WarmService: onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.w("WarmerApp", "WarmService: onDestroy()")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}