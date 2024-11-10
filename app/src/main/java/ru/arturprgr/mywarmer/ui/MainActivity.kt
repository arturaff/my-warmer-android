package ru.arturprgr.mywarmer.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.arturprgr.mywarmer.R
import ru.arturprgr.mywarmer.databinding.ActivityMainBinding
import ru.arturprgr.mywarmer.receiver.TempReceiver
import ru.arturprgr.mywarmer.service.WarmService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManagerCompat
    private val channelId = "Warmer App"

    override fun onDestroy() {
        super.onDestroy()
        Log.w("WarmerApp", "MainActivity: onDestroy()")
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        notificationBuilder = NotificationCompat.Builder(this@MainActivity, channelId)
            .setSmallIcon(R.drawable.ic_info)
            .setContentTitle(resources.getString(R.string.service_has_been_launched))
            .setContentText(resources.getString(R.string.click_to_open_app))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(
                PendingIntent.getActivity(
                    this@MainActivity,
                    0,
                    Intent(this@MainActivity, MainActivity::class.java),
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        notificationManager = NotificationManagerCompat.from(this@MainActivity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(
            NotificationChannel(
                channelId, "Информация о состоянии сервиса", NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            registerReceiver(
                TempReceiver(textBatteryTemp), IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            )
            @Suppress("DEPRECATION") when (this@MainActivity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> buttonStart.setTextColor(resources.getColor(R.color.color_light))
                Configuration.UI_MODE_NIGHT_NO -> buttonStart.setTextColor(resources.getColor(R.color.color_dark))
            }

            buttonInfo.setOnClickListener {
                createDialog(
                    resources.getString(R.string.about_of_application),
                    resources.getString(R.string.app_info)
                )
            }

            buttonInfoIntensity.setOnClickListener {
                createDialog(
                    resources.getString(R.string.intensity_modes),
                    resources.getString(R.string.intensity_info)
                )
            }

            buttonStart.setOnClickListener {
                val intentService = Intent(this@MainActivity, WarmService::class.java)
                intentService.putExtra("isStarted", seekBarIntensity.isEnabled)
                seekBarIntensity.isEnabled = !seekBarIntensity.isEnabled
                if (!seekBarIntensity.isEnabled) {
                    intentService.putExtra("intensity", seekBarIntensity.progress + 1)
                    buttonStart.text = resources.getString(R.string.stop)
                    notificationManager.notify(1, notificationBuilder.build())
                } else {
                    buttonStart.text = resources.getString(R.string.start)
                    notificationManager.cancel(1)
                }
                startService(intentService)
            }
        }
    }

    private fun createDialog(title: String, message: String) =
        AlertDialog.Builder(this@MainActivity).setTitle(title).setMessage(message)
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }.show()
}