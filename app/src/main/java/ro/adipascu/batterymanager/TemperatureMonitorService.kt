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
            .setOngoing(true)
    }

    override fun onCreate() {
        super.onCreate()

        val notificationManager = NotificationManagerCompat.from(this)
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent) {
                if (!intent.hasExtra(
                        BatteryManager.EXTRA_TEMPERATURE
                    )
                ) {
                    throw Error("Missing EXTRA_TEMPERATURE")
                }
                val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10.0
                val chargeEnabled = temperature < 30.0
                setCharging(chargeEnabled)

                if (notificationManager.areNotificationsEnabled()) {
                    notificationManager.notify(
                        1,
                        notificationBuilder.setContentText("Battery $temperature°C charge $chargeEnabled")
                            .setSmallIcon(
                                if (chargeEnabled) {
                                    android.R.drawable.ic_menu_add
                                } else {
                                    android.R.drawable.ic_menu_info_details
                                }
                            )
                            .build()
                    )
                }
                Log.d("BatteryManager", "Battery temperature changed: $temperature°C")
            }
        }, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        notificationManager.createNotificationChannel(
            NotificationChannelCompat.Builder(channelId, NotificationManagerCompat.IMPORTANCE_MIN)
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