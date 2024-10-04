package ru.arturprgr.mywarmer.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log

class TempReceiver(private val view: android.widget.TextView) : BroadcastReceiver() {
    @SuppressLint("SetTextI18n")
    override fun onReceive(context: Context, intent: Intent) {
        val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1).toFloat() / 10
        Log.d("TempLog", "${temp}°C")
        view.text = "$temp°C"
    }
}