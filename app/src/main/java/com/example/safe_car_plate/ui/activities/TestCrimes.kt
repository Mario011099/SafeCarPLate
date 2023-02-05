package com.safecarplate.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.safe_car_plate.R
import com.example.safe_car_plate.databinding.ActivityTestcrimesBinding


class TestCrimes : AppCompatActivity() {
    private lateinit var binding: ActivityTestcrimesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestcrimesBinding.inflate(layoutInflater)
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