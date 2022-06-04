package ro.adipascu.batterymanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private fun formatTemperature(value: Int): String {
    return "${value / 10}.${value % 10}"
}

private const val NOTIFICATION_ID = 1
private const val CHANNEL_ID = "temperature"

class TemperatureMonitorService : Service() {
    companion object {
        fun start(context: Context) {
            context.startService(Intent(context, TemperatureMonitorService::class.java))
        }
    }

    private fun getNotification(temperature: Int, isCharging: Boolean): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Temperature: ${formatTemperature(temperature)} isCharging: $isCharging")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!intent.hasExtra(BatteryManager.EXTRA_TEMPERATURE)) {
                throw UnsupportedOperationException("Missing temperature information")
            }
            val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            Log.i("Temperature", formatTemperature(temperature))
            val isCharging = temperature < 310
            setCharging(isCharging)
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID, getNotification(temperature, isCharging))
        }

    }

    override fun onCreate() {
        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
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

        startForeground(NOTIFICATION_ID, getNotification(0, true))
        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}