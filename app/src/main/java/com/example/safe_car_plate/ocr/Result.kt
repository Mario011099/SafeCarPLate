package com.example.safe_car_plate.ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safe_car_plate.R

class Result : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
    }

    private fun initActivity() {
        var words: List<String?> = ArrayList<String?>()
        words = intent.getSerializableExtra("keys") as ArrayList<String?>
        println("/////////// words///////////////")
        println(words)
//        words.filter {  }
        // txtPlaca.text = words[0]
    }
}