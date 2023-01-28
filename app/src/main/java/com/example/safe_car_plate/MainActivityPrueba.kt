package com.example.safe_car_plate

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.safe_car_plate.databinding.ActivityMainPruebaBinding
import com.example.safe_car_plate.databinding.FragmentLicenseplateBinding
import com.example.safe_car_plate.ocr.Result
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.tensorflow.lite.examples.ocr.MLExecutionViewModel
import org.tensorflow.lite.examples.ocr.ModelExecutionResult
import org.tensorflow.lite.examples.ocr.OCRModelExecutor
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import java.util.concurrent.Executors

class MainActivityPrueba : AppCompatActivity() {
    lateinit var currentPhotoPath: String
    lateinit var binding: ActivityMainPruebaBinding

    //my
    private lateinit var placaImageView: ImageView
    private lateinit var placaImageName: String
    private lateinit var resultImageView: ImageView
    private lateinit var runButton: ImageButton
    private lateinit var chipsGroup: ChipGroup


    //Modelo
    private var useGPU = false
    private lateinit var viewModel: MLExecutionViewModel
    private lateinit var textPromptTextView: TextView
    private var ocrModel: OCRModelExecutor? = null
    private val inferenceThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val mainScope = MainScope()
    private val mutex = Mutex()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPruebaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClass()
    }

    private fun initClass() {
        binding.buttonPrueba.setOnClickListener {
            requestPermission()
        }
    }

    private fun cargarImagen(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        empezarForActivityGallery.launch(intent)
    }

    private val empezarForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == Activity.RESULT_OK){
            val dato = result.data?.data
            binding.imgPlacaPrueba.setImageURI(dato)
            placaImageView = binding.imgPlacaPrueba
            println("Data $dato")
            startCreate()
        }

    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(
                    baseContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    cargarImagen()
                }
                else -> requestPermissionL.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            cargarImagen()
        }
    }

    private val requestPermissionL = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isOtorgado ->
        if (isOtorgado){
            cargarImagen()
        }else{
            Toast.makeText(baseContext, "Necesita habilitar los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCreate(){

        val assetManager = assets
        placaImageName = "placa4.jpg"
        try {
            //my
            val placaInputStream: InputStream? = assetManager?.open(placaImageName)
            val placaBitmap = BitmapFactory.decodeStream(placaInputStream)
            placaImageView.setImageBitmap(placaBitmap)
        } catch (e: IOException) {
            Log.e("FragmentPlaca", "Failed to open a test image")
        }

        resultImageView = binding.imgViewResult
        //chipsGroup = binding.chipsGroup
        //textPromptTextView = binding.textView

        viewModel = ViewModelProvider.AndroidViewModelFactory(application).create(MLExecutionViewModel::class.java)
        viewModel.resultingBitmap.observe(
            this,
            Observer { resultImage ->
                if (resultImage != null) {
                    updateUIWithResults(resultImage)
                }
                enableControls(true)
            }
        )

//        mainScope.async(inferenceThread) { createModelExecutor(useGPU) }
        mainScope.launch {
            println("Dentro corrutina======")
            createModelExecutor(false)
        }
        println("----Antes Botón----")

//        runButton = binding.buttonBuscar
//        runButton.setOnClickListener {
//            enableControls(false)
//
//            mainScope.async(inferenceThread) {
//                mutex.withLock {
//                    if (ocrModel != null) {
//                        viewModel.onApplyModel(activity?.baseContext!!, placaImageName, ocrModel, inferenceThread)
//                    } else {
//                        Log.d(
//                            TAG,
//                            "Skipping running OCR since the ocrModel has not been properly initialized ..."
//                        )
//                    }
//                }
//            }
//        }
//
//        println("----Después Botón----")
//
//        setChipsToLogView(HashMap<String, Int>())
//        enableControls(true)

    }

    private suspend fun createModelExecutor(useGPU: Boolean) {
        mutex.withLock {
            if (ocrModel != null) {
                ocrModel!!.close()
                ocrModel = null
            }
            try {
                ocrModel = OCRModelExecutor(baseContext, useGPU)
            } catch (e: Exception) {
                Log.e("MainPrueba", "Fail to create OCRModelExecutor: ${e.message}")
//                val logText: TextView = binding.logView
//                logText.text = e.message
            }
        }
    }

    private fun setChipsToLogView(itemsFound: Map<String, Int>) {
//        chipsGroup.removeAllViews()
//        //my
//        val keys = itemsFound.keys
//        for ((word, color) in itemsFound) {
//            val chip = Chip(requireContext())
//            chip.text = word // /*******************PALABRAS IDENTIFICADAS******************************************
//            chip.chipBackgroundColor = getColorStateListForChip(color)
//            chip.isClickable = false
//            chipsGroup.addView(chip)
//        }
//        val labelsFoundTextView: TextView = binding.textResultModel //CHECK
//        if (chipsGroup.childCount == 0) {
//            labelsFoundTextView.text = getString(R.string.tfe_ocr_no_text_found)
//        } else {
//            labelsFoundTextView.text = getString(R.string.tfe_ocr_texts_found)
//        }
//        chipsGroup.parent.requestLayout()

        //my
        var wordsList: List<String?> = ArrayList<String?>()
        wordsList = arrayOf(itemsFound.keys)[0].toList()
        println("Lista de palabras"+ wordsList)
        if(wordsList.isNotEmpty()) {
            var intent = Intent(baseContext, Result::class.java)
            val bundle = Bundle()
            bundle.putSerializable("keys", wordsList as java.io.Serializable)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun getColorStateListForChip(color: Int): ColorStateList {
        val states =
            arrayOf(
                intArrayOf(android.R.attr.state_enabled), // enabled
                intArrayOf(android.R.attr.state_pressed) // pressed
            )

        val colors = intArrayOf(color, color)
        return ColorStateList(states, colors)
    }

    private fun setImageView(imageView: ImageView, image: Bitmap) {
        Glide.with(baseContext).load(image).override(250, 250).fitCenter().into(imageView)
    }

    private fun updateUIWithResults(modelExecutionResult: ModelExecutionResult) {
        setImageView(resultImageView, modelExecutionResult.bitmapResult)
//        val logText: TextView = binding.logView
//        logText.text = modelExecutionResult.executionLog

        setChipsToLogView(modelExecutionResult.itemsFound)
        enableControls(true)
    }

    private fun enableControls(enable: Boolean) {
        runButton.isEnabled = enable
    }
}