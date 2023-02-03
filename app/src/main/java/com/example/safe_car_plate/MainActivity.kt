package com.example.safe_car_plate

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.safe_car_plate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //@SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(licenseplate())

        println("MainA -----")

       binding.navBottom1.setOnItemSelectedListener {

           when(it.itemId){
                R.id.search_licenseplate -> replaceFragment(licenseplate())
                R.id.search_crimes -> replaceFragment(crimes())
                R.id.about -> replaceFragment(about())

                else ->{

                }
            }
           true
        }
    }

     fun replaceFragment( fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}