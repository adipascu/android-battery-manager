package ro.adipascu.batterymanager

import android.app.Application
import android.os.BatteryManager

lateinit var batteryManager: BatteryManager
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        batteryManager =
            getSystemService(BATTERY_SERVICE) as BatteryManager
    }
}