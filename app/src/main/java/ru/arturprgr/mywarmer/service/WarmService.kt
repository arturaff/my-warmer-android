package ru.arturprgr.mywarmer.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import ru.arturprgr.mywarmer.R
import java.util.Random

class WarmService : Service() {
    private var millis: Long = -1
    private var random: String? = null
    private lateinit var thread: Thread

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("WarmerLog", "Сервис запущен!")
        millis = when {
            intent!!.getIntExtra("intensity", 0) <= 0 ->500
            intent.getIntExtra("intensity", 0) == 1 -> 200
            intent.getIntExtra("intensity", 0) == 2 -> 1
            else -> 500
        }
        Log.d("WarmerLog", millis.toString())
        thread = Thread {
            while (true) {
                random = Random().nextLong().toString()
                Log.d("WarmerLog", random.toString())
                try {
                    Thread.sleep(millis)
                } catch (_: InterruptedException) {
                    break
                }
            }
        }
        thread.start()
        createToast(resources.getString(R.string.service_started))
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("WarmerLog", "Сервис остановлен!")
        random = null
        thread.interrupt()
        createToast(resources.getString(R.string.service_stopped))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createToast(text: String) = Toast.makeText(
        this, text, Toast.LENGTH_LONG
    ).show()
}