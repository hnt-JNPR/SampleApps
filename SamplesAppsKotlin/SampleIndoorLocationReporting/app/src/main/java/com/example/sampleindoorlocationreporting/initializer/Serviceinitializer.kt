package com.example.sampleindoorlocationreporting.initializer

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService
import com.example.sampleindoorlocationreporting.Constants
import com.example.sampleindoorlocationreporting.service.LocationForegroundService
import com.example.sampleindoorlocationreporting.service.LocationJobservice

/**
 * ServiceInitializer This class provides utility functions to start the android services.
 */
class Serviceinitializer {
    /* Define your SDK job ID here.*/
    public final val MIST_SDK_JOB_ID : Int =789

    val Constants = Constants()
    val LocationJobService=LocationJobservice()

    enum class BackgroundServiceType {
        SCHEDULE_SERVICE, FOREGROUND_SERVICE
    }

    var BACKGROUND = BackgroundServiceType.SCHEDULE_SERVICE

    fun startLocationService(context : Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            BACKGROUND = BackgroundServiceType.FOREGROUND_SERVICE
            startMistForegroundService(context)
        }
        else{
            scheduleJob(context)
        }
    }

    fun stopLocationService(context: Context) {
        if (BACKGROUND == BackgroundServiceType.SCHEDULE_SERVICE) {
            stopScheduleJob(context)
        }
        else if(BACKGROUND==BackgroundServiceType.FOREGROUND_SERVICE){
            stopMistForegroundService(context)
        }
    }

    /** stop scheduled job */
    @Throws(java.lang.NullPointerException::class)
    open fun stopScheduleJob(context: Context) {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        if (jobScheduler != null) {
            LocationJobService.needJobReschedule(false)
            jobScheduler.cancel(MIST_SDK_JOB_ID)
        } else {
            throw java.lang.NullPointerException("JobScheduler Service is null")
        }
    }

    /**
     * This is an API for scheduling various types of jobs against the framework that will be
     * executed in your application's own process.
     * https://developer.android.com/reference/android/app/job/JobService
     * https://developer.android.com/reference/android/app/job/JobScheduler
     */
    @Throws(NullPointerException::class)
    open fun scheduleJob(context: Context) {
        val serviceComponent = ComponentName(context, LocationJobService::class.java)
        val builder = JobInfo.Builder(MIST_SDK_JOB_ID, serviceComponent).setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setPersisted(true)
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        if (jobScheduler != null) {
            LocationJobService.needJobReschedule(true)
            jobScheduler.schedule(builder.build())
        } else {
            throw NullPointerException("JobScheduler Service is null")
        }
    }

    private fun startMistForegroundService(context: Context) {
        val intent = Intent(context, LocationForegroundService::class.java).putExtra("ORG_SECRET",Constants.ORG_SECRET)
        startForegroundService(context,intent)
    }


    private fun stopMistForegroundService(context: Context) {
        val intent = Intent(context,LocationForegroundService::class.java)
        context.stopService(intent)
    }
}