package ro.adipascu.batterymanager

import android.app.Application
import android.content.Intent
import android.os.BatteryManager
import androidx.core.content.ContextCompat

lateinit var batteryManager: BatteryManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        batteryManager =
            getSystemService(BATTERY_SERVICE) as BatteryManager
        ContextCompat.startForegroundService(
            this,
            Intent(this, TemperatureMonitorService::class.java)
        )
    }
}