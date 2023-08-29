package com.example.samplebluedotexperience

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.samplebluedotexperience.databinding.ActivityMainBinding
import com.example.samplebluedotexperience.fragment.MapFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val constants = Constants()
    private val orgSecret :String = constants.orgSecret
    private val MapFragment = MapFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        // Load BlueDot Map screen in fragment, permissions are checked inside this fragment.
        setUPMapFragment(orgSecret)
    }

    private fun setUPMapFragment(ORG_SECRET:String) {
        //val mapFragment = MapFragment()
        val mapFragment = supportFragmentManager.findFragmentByTag(MapFragment.TAG)
        if (mapFragment == null) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_fragment, MapFragment.newInstance(ORG_SECRET), MapFragment.TAG).addToBackStack(MapFragment.TAG).commit()
        }
    }
}