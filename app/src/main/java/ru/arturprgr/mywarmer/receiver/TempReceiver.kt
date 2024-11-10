package ru.arturprgr.mywarmer.receiver

class TempReceiver(private val view: android.widget.TextView) :
    android.content.BroadcastReceiver() {
    @android.annotation.SuppressLint("SetTextI18n")
    override fun onReceive(context: android.content.Context, intent: android.content.Intent) {
        val temp =
            intent.getIntExtra(android.os.BatteryManager.EXTRA_TEMPERATURE, -1).toFloat() / 10
        android.util.Log.i("WarmerApp", "TempReceiver: ${temp}°C")
        view.text = "$temp°C"
    }
}