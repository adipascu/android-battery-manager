package ro.adipascu.batterymanager

import com.topjohnwu.superuser.Shell

val DIRECTORY = "/sys/devices/platform/google,charger/"
val STOP_FILE = DIRECTORY + "charge_stop_level"
val START_FILE = DIRECTORY + "charge_start_level"

fun read(path: String) =
    Shell.cmd("cat $path").exec().out.first()
        .toInt()

fun write(path: String, value: Int) {
    val result = Shell.cmd("echo $value >> $path").exec()
    if (!result.isSuccess) {
        throw Error("Failed to write file\n" + result.err.firstOrNull())
    }
}

fun setCharging(isEnabled: Boolean) {
    if (isEnabled) {
        write(START_FILE, 70)
        write(STOP_FILE, 75)

//        write(START_FILE, 0)
//        write(STOP_FILE, 100)
    } else {
        write(START_FILE, 0)
        write(STOP_FILE, 0)
    }
}

class ChargeManager {

    companion object {

    }
}