package ro.adipascu.batterymanager

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log


class BootReceiverService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("BootReceiverService", "onReceive");
        TemperatureMonitorService.start(context)
    }
}