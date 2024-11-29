package ro.adipascu.batterymanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BootReceiverService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }
        Log.i("BootReceiverService", "onReceive")
        setCharging(true)
        Toast.makeText(context, "Adrian's battery manager enabled", Toast.LENGTH_LONG).show()
    }
}