package com.example.sampleappwakeup

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

open class PermissionHandlerActivity : AppCompatActivity(){
    private val PERMISSION_REQUEST_BLUETOOTH_LOCATION : Int = 1

    private val PERMISSION_REQUEST_BACKGROUND_LOCATION : Int = 2

    private val REQUEST_ENABLE_BLUETOOTH : Int = 3

    open fun startBeaconMonitor(){

    }

    fun checkPermissions(){
        val permissionRequired = ArrayList<String>()
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionRequired.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        // For API 31 we need BLUETOOTH_SCAN permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            permissionRequired.add(android.Manifest.permission.BLUETOOTH_SCAN)
        }
        // For API 31 we need BLUETOOTH_CONNECT permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissionRequired.add(android.Manifest.permission.BLUETOOTH_CONNECT)
        }
        if(permissionRequired.size > 0){
            showLocationBluetoothPermissionDialog(permissionRequired)
        }
        else{
            checkIfBluetoothEnabled()
            if (checkBackgroundLocation()) {
                startBeaconMonitor()
            }
        }
    }

    private fun checkBackgroundLocation(): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf<String>(Manifest.permission.ACCESS_BACKGROUND_LOCATION), PERMISSION_REQUEST_BACKGROUND_LOCATION)
            return false
        }
        else{
            return true
        }
    }

    private fun checkIfBluetoothEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!(bluetoothAdapter != null && bluetoothAdapter.isEnabled && bluetoothAdapter.state!= BluetoothAdapter.STATE_ON)){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH)
        }
    }

    private fun showLocationBluetoothPermissionDialog(permissionRequired: ArrayList<String>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("This app needs bluetooth and location permission")
        builder.setMessage("Please grant bluetooth/location access so this app can detect beacons in the background")
        builder.setPositiveButton(android.R.string.ok, null)
        builder.setOnDismissListener {
            val permissionToRequest = permissionRequired.filter {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }.toTypedArray()
            if (permissionToRequest.isNotEmpty()) {
                requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_BLUETOOTH_LOCATION)
            }
        }
        builder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_REQUEST_BLUETOOTH_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    checkIfBluetoothEnabled()
                    if(checkBackgroundLocation()){
                        startBeaconMonitor()
                    }
                }
                else{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited!")
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.")
                    builder.setPositiveButton(android.R.string.ok,null)
                    fun onDismiss(dialog: DialogInterface?) { }
                    builder.show()
                }
            }

            PERMISSION_REQUEST_BACKGROUND_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    checkIfBluetoothEnabled()
                    startBeaconMonitor()
                }
            }
        }
    }
}