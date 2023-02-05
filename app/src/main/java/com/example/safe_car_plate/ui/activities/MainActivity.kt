package com.safecarplate.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.safe_car_plate.R
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
        //replaceFragment(licenseplate())

       binding.navBottom1.setOnItemSelectedListener {

           when(it.itemId){
               R.id.search_licenseplate ->{
                   startActivity(Intent(this, MainActivity::class.java))
                   overridePendingTransition(0,0)
                   return@setOnItemSelectedListener true}
               R.id.search_crimes -> {
                   startActivity(Intent(this, TestCrimes::class.java))
                   overridePendingTransition(0,0)
                   return@setOnItemSelectedListener true
               }
               R.id.about -> {
                   startActivity(Intent(this, TestAbout::class.java))
                   overridePendingTransition(0,0)
                   return@setOnItemSelectedListener true}

                else ->{

                }
            }
           true
        }
    }



}