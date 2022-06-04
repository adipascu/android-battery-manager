package ro.adipascu.batterymanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.topjohnwu.superuser.Shell
import ro.adipascu.batterymanager.ui.theme.BatteryManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shell.getShell()
        TemperatureMonitorService.start(this)
        setContent {
            BatteryManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (!intent.hasExtra(BatteryManager.EXTRA_TEMPERATURE)) {
                    throw UnsupportedOperationException("Missing temperature information")
                }
                val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
                Log.i("Temperature", temperature.toString())

                val bundle = intent.extras
                if (bundle != null) {
                    for (key in bundle.keySet()) {
                        Log.e("Temperature extra", key + " : " + if (bundle[key] != null) bundle[key] else "NULL")
                    }
                }
            }

        }
        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BatteryManagerTheme {
        Greeting("Android")
    }
}