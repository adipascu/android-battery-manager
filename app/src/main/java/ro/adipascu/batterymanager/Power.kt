package ro.adipascu.batterymanager

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class Power : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        TemperatureMonitorService.start(this)
        Log.i("JobService", "onStartJob")
        return false
    }

    override fun onStopJob(params: JobParameters?) = false
}