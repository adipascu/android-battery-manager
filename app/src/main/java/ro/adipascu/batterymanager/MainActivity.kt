package ro.adipascu.batterymanager

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import com.topjohnwu.superuser.Shell


class MainActivity : ComponentActivity() {

    private val button by lazy {
        Button(this).apply {
            text = chargeStatus()
            setOnClickListener {
                setCharging(true)
                text = chargeStatus()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shell.getShell()
        setContentView(button)
    }
}