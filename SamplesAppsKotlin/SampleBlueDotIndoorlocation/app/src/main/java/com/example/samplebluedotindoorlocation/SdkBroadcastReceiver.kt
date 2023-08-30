package com.example.samplebluedotindoorlocation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.samplebluedotindoorlocation.initializer.*

class SdkBroadcastReceiver : BroadcastReceiver(){
    private val serviceInitializer = ServiceInitializer()
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            /*https://developer.android.com/reference/android/content/Intent#ACTION_BOOT_COMPLETED*/
            Intent.ACTION_BOOT_COMPLETED -> {
                Toast.makeText(context, "ACTION_BOOT_COMPLETED", Toast.LENGTH_SHORT).show()
                serviceInitializer.startLocationService(context)
            }
            /*https://developer.android.com/reference/android/content/Intent#ACTION_USER_PRESENT*/
            Intent.ACTION_USER_PRESENT -> serviceInitializer.startLocationService(context)
            else -> throw IllegalStateException("Unexpected value: " + intent.action)
        }
    }
}