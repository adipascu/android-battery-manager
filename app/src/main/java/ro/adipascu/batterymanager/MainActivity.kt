package ro.adipascu.batterymanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.topjohnwu.superuser.Shell

class MainActivity : ComponentActivity() {

    private val startButton by lazy {
        Button(this).apply {
            text = chargeStatus()
            setOnClickListener {
                setCharging(true)
                text = chargeStatus()
            }
        }
    }

    private val stopButton by lazy {
        Button(this).apply {
            text = chargeStatus()
            setOnClickListener {
                setCharging(false)
                text = chargeStatus()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shell.getShell()
        val linearLayout = LinearLayout(this)
        linearLayout.addView(startButton)
        linearLayout.addView(stopButton)
        setContentView(linearLayout)
        ContextCompat.startForegroundService(
            this,
            Intent(this, TemperatureMonitorService::class.java)
        )
    }
}