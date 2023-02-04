package com.example.safe_car_plate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.safe_car_plate.databinding.FragmentLicenseplateBinding
import com.example.safe_car_plate.utils.GraphicOverlay
import com.example.safe_car_plate.utils.TextGraphic
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.tensorflow.lite.examples.ocr.MLExecutionViewModel
import org.tensorflow.lite.examples.ocr.ModelExecutionResult
import org.tensorflow.lite.examples.ocr.OCRModelExecutor
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


/**
 * A simple [Fragment] subclass.
 * Use the [licenseplate.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "licenseplate"

class licenseplate : Fragment() {

    lateinit var binding: FragmentLicenseplateBinding

    //my
    private lateinit var placaImageView: ImageView
    private lateinit var runButton: ImageButton
    private lateinit var consultaButton: ImageButton
    private lateinit var imageInput: InputImage

    //********Dependencia*******
    private var mSelectedImage: Bitmap? = null
    private var mGraphicOverlay: GraphicOverlay? = null

    // Max width (portrait mode)
    private var mImageMaxWidth: Int? = null

    // Max height (portrait mode)
    private var mImageMaxHeight: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLicenseplateBinding.inflate(inflater, container, false)

        initClass()
        pruebaApiML()
//        imageSelec()
        return binding.root

    }

    fun getBitmapFromAsset(context: Context, filePath: String?): Bitmap? {
        val assetManager = context.assets
        val `is`: InputStream
        var bitmap: Bitmap? = null
        try {
            `is` = assetManager.open(filePath!!)
            bitmap = BitmapFactory.decodeStream(`is`)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    // Functions for loading images from app assets.

    // Functions for loading images from app assets.
    // Returns max image width, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private fun getImageMaxWidth(): Int {
        if (mImageMaxWidth == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxWidth = placaImageView.getWidth()
        }
        return mImageMaxWidth as Int
    }

    // Returns max image height, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private fun getImageMaxHeight(): Int {
        if (mImageMaxHeight == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxHeight = placaImageView.getHeight()
        }
        return mImageMaxHeight as Int
    }

    // Gets the targeted width / height.
    private fun getTargetedWidthHeight(): Pair<Int, Int> {
        val targetWidth: Int?
        val targetHeight: Int?
        val maxWidthForPortraitMode: Int? = getImageMaxWidth()
        val maxHeightForPortraitMode: Int? = getImageMaxHeight()
        targetWidth = maxWidthForPortraitMode
        targetHeight = maxHeightForPortraitMode
        return Pair(targetWidth, targetHeight)
    }


    private fun imageSelec() {
        mGraphicOverlay = binding.graphicOverlay
        mGraphicOverlay!!.clear()
        mSelectedImage = getBitmapFromAsset(activity?.baseContext!!, "placa4.jpg")
//        mSelectedImage = imageInput.bitmapInternal
        if (mSelectedImage != null) {
            // Get the dimensions of the View
            val targetedSize: Pair<Int, Int> = getTargetedWidthHeight()
            val targetWidth = targetedSize.first
            val maxHeight = targetedSize.second

            // Determine how much to scale down the image

            val scaleFactor = Math.max(
                mSelectedImage!!.width.toFloat() / targetWidth.toFloat(),
                mSelectedImage!!.height.toFloat() / maxHeight.toFloat()
            )
            val widthInt = (mSelectedImage!!.width / scaleFactor).toInt()
            val heightInt = (mSelectedImage!!.height / scaleFactor).toInt()
            if(widthInt > 0 && heightInt >0){
                val resizedBitmap = Bitmap.createScaledBitmap(
                    mSelectedImage!!,
                    (mSelectedImage!!.width / scaleFactor).toInt(),
                    (mSelectedImage!!.height / scaleFactor).toInt(),
                    true
                )
                placaImageView.setImageBitmap(resizedBitmap)
                mSelectedImage = resizedBitmap
            }else{
                Toast.makeText(activity?.baseContext!!, "Error en la imagen", Toast.LENGTH_SHORT)
            }

        }
    }

    private fun pruebaApiML() {
        runButton = binding.buttonBuscar

    }

    private fun runTextRecognition() {
        val recognizer = TextRecognition.getClient()
        runButton.setEnabled(false)
        recognizer.process(imageInput)
            .addOnSuccessListener { texts ->
                runButton.setEnabled(true)
                processTextRecognitionResult(texts)
            }
            .addOnFailureListener { e -> // Task failed with an exception
                runButton.setEnabled(true)
                e.printStackTrace()
            }
    }

    private fun processTextRecognitionResult(texts: Text) {
        var wordsList: MutableList<String?> = ArrayList<String?>()
        val blocks = texts.textBlocks
        if (blocks.size == 0) {
            showToast("No text found")
            return
        }
        mGraphicOverlay?.clear()
        for (i in blocks.indices) {
            val lines = blocks[i].lines
            for (j in lines.indices) {
                val elements = lines[j].elements
                for (k in elements.indices) {
                    val textGraphic: GraphicOverlay.Graphic = TextGraphic(mGraphicOverlay, elements[k])
                    mGraphicOverlay?.add(textGraphic)
                    println("Palabra " + elements[k].text)
                    wordsList.add(elements[k].text)
                }
            }
        }
        getListWords(wordsList)

    }

    private fun getListWords(wordsList: MutableList<String?>) {
        var v = wordsList.filter { s-> !(s.equals("ECUADOR") || s.equals("ANT")) }
        println(v)
        if(v.isNotEmpty()){
            binding.textResultModel.text = v[0]?.uppercase()
            binding.btnConsultar.visibility = View.VISIBLE
            binding.idTextTitlePlaca.visibility = View.VISIBLE
        }else{
            showToast("No se encontró ninguna placa")
        }

    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    private fun initClass(){
        binding.subirFoto.setOnClickListener {
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
            binding.imgVPlaca.setImageURI(dato)
            placaImageView = binding.imgVPlaca
            imageInput = InputImage.fromFilePath(activity?.baseContext!!, result.data?.data!!)
            runTextRecognition()
            println("Data $dato")
        }

    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(
                    activity?.baseContext!!,
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
            Toast.makeText(activity?.baseContext, "Necesita habilitar los permisos",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setChipsToLogView(itemsFound: Map<String, Int>) {
        var wordsList: List<String?> = ArrayList<String?>()
        wordsList = arrayOf(itemsFound.keys)[0].toList()
        println("Lista de palabras"+ wordsList)
        if(wordsList.isNotEmpty()) {
            var intent = Intent(activity?.baseContext!!, com.example.safe_car_plate.ocr.Result::class.java)
            val bundle = Bundle()
            bundle.putSerializable("keys", wordsList as java.io.Serializable)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }


}