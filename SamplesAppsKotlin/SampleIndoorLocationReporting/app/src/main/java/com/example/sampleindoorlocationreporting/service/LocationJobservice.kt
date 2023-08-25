package com.example.sampleindoorlocationreporting.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.example.sampleindoorlocationreporting.Constants
import com.example.sampleindoorlocationreporting.handler.SDKCallbackHandler
import com.example.sampleindoorlocationreporting.initializer.MistSdkManager

class LocationJobservice : JobService() {

    val constants = Constants()
    val ORG_SECRET = constants.ORG_SECRET
    private var needReshedule : Boolean = true
    override fun onStartJob(p0: JobParameters?): Boolean {
        startWorkOnNewThread()
        return true
    }

    private fun startWorkOnNewThread() {
        Thread({ doWork() }, "LocationJobService").start()
    }

    private fun doWork() {
        val mistSdkManager : MistSdkManager?= MistSdkManager().getInstance(application)
        val sdkCallbackHandler : SDKCallbackHandler = SDKCallbackHandler(applicationContext)
        if (mistSdkManager != null) {
            mistSdkManager.init(ORG_SECRET,sdkCallbackHandler,sdkCallbackHandler,applicationContext)
            mistSdkManager.startMistSDK()
        }
        Log.d("TAG","SampleLocationApp: doWork() ThreadName: " + Thread.currentThread().name)

    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        jobFinished(p0,needReshedule)
        return needReshedule
    }

    fun needJobReschedule(needReschedule: Boolean) {
        LocationJobservice().needReshedule=needReshedule
    }
}