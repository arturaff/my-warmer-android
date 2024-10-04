package ru.arturprgr.mywarmer.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.arturprgr.mywarmer.R
import ru.arturprgr.mywarmer.databinding.ActivityMainBinding
import ru.arturprgr.mywarmer.receiver.TempReceiver
import ru.arturprgr.mywarmer.service.WarmService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var intent: Intent
    private var isStarted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        intent = Intent(this@MainActivity, WarmService::class.java)
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
            intent.putExtra("intensity", seekBarIntensity.progress)

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
                isStarted = !isStarted
                if (isStarted) {
                    seekBarIntensity.isEnabled = false
                    startService(intent.putExtra("intensity", seekBarIntensity.progress))
                    buttonStart.text = resources.getString(R.string.stop)
                } else {
                    seekBarIntensity.isEnabled = true
                    stopService(intent)
                    buttonStart.text = resources.getString(R.string.start)
                }
            }
        }
    }

    private fun createDialog(title: String, message: String) =
        AlertDialog.Builder(this@MainActivity).setTitle(title).setMessage(message)
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }.show()
}