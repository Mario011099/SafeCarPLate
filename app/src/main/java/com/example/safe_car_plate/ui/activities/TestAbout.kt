package com.safecarplate.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safe_car_plate.R
import com.example.safe_car_plate.databinding.ActivityTestaboutBinding


class TestAbout : AppCompatActivity() {
    private lateinit var binding: ActivityTestaboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestaboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

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