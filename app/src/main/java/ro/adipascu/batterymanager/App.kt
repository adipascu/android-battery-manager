package ro.adipascu.batterymanager

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log

lateinit var batteryManager: BatteryManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        batteryManager =
            getSystemService(BATTERY_SERVICE) as BatteryManager
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent) {
                val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10.0
                Log.d("BatteryManager", "Battery temperature changed: $temperature°C")
            }

        }, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }
}