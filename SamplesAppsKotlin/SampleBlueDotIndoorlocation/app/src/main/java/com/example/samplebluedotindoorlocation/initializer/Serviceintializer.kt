package com.example.samplebluedotindoorlocation.initializer

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.samplebluedotindoorlocation.service.LocationForegroundService


class Serviceintializer {

    /* Define your SDK job ID here.*/
    public final val MIST_SDK_JOB_ID : Int =789

    fun startLocationService(context : Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startMistForegroundService(context)
        }
    }

    fun stopLocationService(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            stopMistForegroundService(context)
        }
    }

    private fun stopMistForegroundService(context: Context?) {
        val intent = Intent(context, LocationForegroundService::class.java)
        ContextCompat.startForegroundService(context!!, intent)
    }

    private fun startMistForegroundService(context: Context) {
        val intent = Intent(context,LocationForegroundService::class.java)
        context.stopService(intent)
    }


}