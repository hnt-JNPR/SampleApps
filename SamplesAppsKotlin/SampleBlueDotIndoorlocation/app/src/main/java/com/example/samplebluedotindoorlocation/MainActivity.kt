package com.example.samplebluedotindoorlocation

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.samplebluedotindoorlocation.databinding.ActivityMainBinding
import com.example.samplebluedotindoorlocation.fragment.MapFragment
import com.example.samplebluedotindoorlocation.initializer.Serviceintializer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val contstants =Constants()
    val serviceinitializer = Serviceintializer()

    private val PERMISSION_REQUEST_BLUETOOTH_LOCATION : Int = 1
    private val REQUEST_ENABLE_BLUETOOTH : Int = 1
    private val PERMISSION_REQUEST_BACKGROUND_LOCATION : Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** Stopping the location sdk background service,if its running. */
        serviceinitializer.stopLocationService(applicationContext)
        if(checkPermissions()){
            /** If permissions are provide load the Bluedot map fragment. */
            setUpMapFragment(contstants.ORG_SECRET)
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("SampleBlueDot","onStop called")
        /**
         * Starting the location SDK in background when app goes in background.
         */
        serviceinitializer.startLocationService(applicationContext)
    }

    private fun setUpMapFragment(orgSecret: String) {
        val MapFragment = MapFragment()
        val mapFragment : Fragment? = supportFragmentManager.findFragmentByTag(MapFragment.TAG)
        if(mapFragment == null){
            supportFragmentManager.beginTransaction().replace(R.id.frame_fragment,MapFragment.newInstance(orgSecret),MapFragment.TAG).addToBackStack(MapFragment.TAG).commit()
        }
    }

    /**
     * Checking permissions required for running location sdk and blue dot experience.
     */
    private fun checkPermissions(): Boolean {
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
            /**
             * We check for background location only when permissions for fine location is granted
             */
            if (checkBackgroundLocation()) {
                return true
            }
        }
        return false
    }

    /**
     * Creates dialog for asking the permissions @param permissionRequired
     */
    private fun showLocationBluetoothPermissionDialog(permissionRequired: ArrayList<String>) {
        if(this!=null) {
            val builder = AlertDialog.Builder(this)
            //val array: Array<String> = permissionRequired.toArray() as Array<String>
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
    }


    /*private fun showLocationBluetoothPermissionDialog(permissionRequired: java.util.ArrayList<String>) {
        TODO("Not yet implemented")
    }*/

    /**
     * Checking if Bluetooth is enabled or not, it is required for ble scanning.
     */
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

    /**
     * Check and Request for background location permissions.
     */
    private fun checkBackgroundLocation(): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf<String>(Manifest.permission.ACCESS_BACKGROUND_LOCATION), PERMISSION_REQUEST_BACKGROUND_LOCATION)
            return false
        }
        else{
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_REQUEST_BLUETOOTH_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    checkIfBluetoothEnabled()
                    if(checkBackgroundLocation()){
                        setUpMapFragment(contstants.ORG_SECRET)
                    }
                }
                else{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Functionality limited!")
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.")
                    fun onDismiss(dialog: DialogInterface?) { }
                    builder.show()
                }
            }

            PERMISSION_REQUEST_BACKGROUND_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    setUpMapFragment(contstants.ORG_SECRET)
                }
            }
        }
    }

}
