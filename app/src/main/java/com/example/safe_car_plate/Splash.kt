package com.example.safe_car_plate

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.safe_car_plate.databinding.ActivitySplashBinding


class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showSplash()
    }

    fun showSplash(){

        object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                val startActivity = Intent(this@Splash, Login::class.java)
                startActivity(startActivity)
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {}
        }.start()
    }
}