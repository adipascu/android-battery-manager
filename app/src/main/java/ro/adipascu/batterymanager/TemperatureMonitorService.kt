package ro.adipascu.batterymanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_POWER_DISCONNECTED
import android.content.IntentFilter
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.topjohnwu.superuser.Shell
import java.io.DataOutputStream


class TemperatureMonitorService : Service() {

    companion object {
        private const val CHANNEL_ID = "temperature"

        fun start(context: Context) {
            StrictMode.enableDefaults();
            Log.i("Service", "start")
            val batteryManager = context.getSystemService(BATTERY_SERVICE) as BatteryManager
//            if (!batteryManager.isCharging) {
//                return
//            }

            val serviceIntent = Intent(context, TemperatureMonitorService::class.java)
            context.startService(serviceIntent)
            context.registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    Log.i("Service", "DISCHARGE")
                    context.stopService(serviceIntent)
                }

            }, IntentFilter(ACTION_POWER_DISCONNECTED))
        }
    }

    override fun onCreate() {
        Log.i("Service", "onCreate")
        Handler().postDelayed({
            Log.i("Service", "bunkit")
            stopSelf()
        }, 10_000)

        setCharging(true)

    }

    override fun onDestroy() {
        Log.i("Service", "onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("Service", "onStartCommand")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    getString(R.string.temperature_service_channel),
                    NotificationManager.IMPORTANCE_NONE
                )
            )
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Temperature Service")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        startForeground(1, notification)
        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}