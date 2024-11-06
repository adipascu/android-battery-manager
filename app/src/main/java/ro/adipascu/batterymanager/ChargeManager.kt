package ro.adipascu.batterymanager

import android.util.Log
import com.topjohnwu.superuser.Shell

private const val DIRECTORY = "/sys/devices/platform/google,charger/"
private const val STOP_FILE = DIRECTORY + "charge_stop_level"
private const val START_FILE = DIRECTORY + "charge_start_level"

private fun read(path: String) =
    Shell.cmd("cat $path").exec().out.first()
        .toInt()

private fun write(path: String, value: Int) {
    if (read(path) == value) {
        return
    }
    val result = Shell.cmd("echo $value >> $path").exec()
    if (!result.isSuccess) {
        throw Error("Failed to write file $path\n" + result.err.firstOrNull())
    }
}

private var lastIsEnabled: Boolean? = null

fun setCharging(isEnabled: Boolean) {
    if (isEnabled == lastIsEnabled) {
        return
    }
    Log.i("ChargeManager", "${if (isEnabled) "Enabled" else "Disabled"} charging")
    val startValue: Int
    val stopValue: Int
    if (isEnabled) {
        startValue = 70
        stopValue = 75
    } else {
        startValue = 0
        stopValue = 1
    }
    try {
        write(STOP_FILE, stopValue)
        write(START_FILE, startValue)
    } catch (e: Error){
        write(START_FILE, startValue)
        write(STOP_FILE, stopValue)
    }
    lastIsEnabled = isEnabled
}
