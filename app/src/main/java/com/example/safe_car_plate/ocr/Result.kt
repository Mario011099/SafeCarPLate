package org.tensorflow.lite.examples.ocr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*

class Result : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        initActivity()
    }

    private fun initActivity() {
        var words: List<String?> = ArrayList<String?>()
        words = intent.getSerializableExtra("keys") as ArrayList<String?>
        println("/////////// words///////////////")
        println(words)
//        words.filter {  }
        txtPlaca.text = words[0]
    }
}