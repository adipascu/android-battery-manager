package ro.adipascu.batterymanager

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast


class BootReceiverService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("BootReceiverService", "onReceive");
        setCharging(true)
        Toast.makeText(context, "Adrian's battery manager enabled", Toast.LENGTH_LONG).show()
    }
}