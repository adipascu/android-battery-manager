package ro.adipascu.batterymanager

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat

class TemperatureMonitorService : Service() {
    private val channelId = "BatteryMonitorChannel"

    val notificationBuilder by lazy {
        NotificationCompat.Builder(this, channelId)
            .setContentTitle("Battery Monitor Running")
            .setContentText("Monitoring battery temperature")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setPriority(NotificationCompat.PRIORITY_LOW)
    }

    override fun onCreate() {
        super.onCreate()

        val notificationManager = NotificationManagerCompat.from(this)
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent) {
                val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10.0

                if (notificationManager.areNotificationsEnabled()) {
                    notificationManager.notify(
                        1,
                        notificationBuilder.setContentText("Battery temperature $temperature°C")
                            .build()
                    )
                }
                Log.d("BatteryManager", "Battery temperature changed: $temperature°C")
            }
        }, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        notificationManager.createNotificationChannel(
            NotificationChannelCompat.Builder(channelId, NotificationManagerCompat.IMPORTANCE_LOW)
                .setName("Battery Monitor Service")
                .build()
        )

        ServiceCompat.startForeground(
            this, 1, notificationBuilder.build(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                0
            }
        )
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}