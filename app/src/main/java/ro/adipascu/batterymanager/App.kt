package ro.adipascu.batterymanager

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.util.Log

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val builder = JobInfo.Builder(0, ComponentName(this, Power::class.java))
        builder.setRequiresCharging(true);
        val jobScheduler = this.getSystemService(JobScheduler::class.java)
        jobScheduler.schedule(builder.build())
        Log.i("JobService", "schedule")
    }
}