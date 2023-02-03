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
    lateinit var currentPhotoPath: String
    lateinit var binding: FragmentLicenseplateBinding

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
        // Inflate the layout for this fragment
        binding = FragmentLicenseplateBinding.inflate(inflater, container, false)
        //initClass()
        //initClass2()
        placaImageView = binding.imgVPlaca
        imageSelec()
        pruebaApiML()
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
        runButton.setOnClickListener(View.OnClickListener { runTextRecognition() })

    }


    private fun runTextRecognition() {
        val image = InputImage.fromBitmap(mSelectedImage!!, 0)
        val recognizer = TextRecognition.getClient()
        runButton.setEnabled(false)
        recognizer.process(image)
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
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            placaImageName = absoluteFile.name
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            context?.let {
                takePictureIntent.resolveActivity(it.packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(), "com.example.android.fileprovider", it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    }
                }
            }
        }
    }

    private fun initClass() {

        placaImageView = binding.imgVPlaca
        binding.subirFoto.setOnClickListener {
            var photoFile: File? = null
            photoFile = createImageFile()
            var imageUri: Uri
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            val uriSavedImage = Uri.fromFile(File("/storage/emulated/0/Pictures/image.jpg"))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = Uri.parse("/storage/emulated/0/Pictures/image.jpg");
            } else {
                imageUri = Uri.fromFile(File("/storage/emulated/0/Pictures/image.jpg"));
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivity(intent)


        }

        binding.buttonBuscar.setOnClickListener {
            var placa = binding.textPlaca.text.toString()
            val bundle = Bundle()
            bundle.putString("placa", placa)
            val crimesFragment = crimes()
            crimesFragment.arguments = bundle
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, crimesFragment)
            transaction?.commit()
        }


    }

    private fun initClass2(){
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
            println("Data $dato")
            //startCreate()
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

//    private fun startCreate(){
//
//        val assetManager = activity?.assets
//        placaImageName = "placa4.jpg"
//        try {
//            //my
//            val placaInputStream: InputStream? = assetManager?.open(placaImageName)
//            val placaBitmap = BitmapFactory.decodeStream(placaInputStream)
//            placaImageView.setImageBitmap(placaBitmap)
//        } catch (e: IOException) {
//            Log.e("FragmentPlaca", "Failed to open a test image")
//        }
//
////        resultImageView = binding.resultImageViewPlaca
////        chipsGroup = binding.chipsGroup
//        textPromptTextView = binding.textView
//
//        viewModel = ViewModelProvider.AndroidViewModelFactory(activity?.application!!).create(MLExecutionViewModel::class.java)
//        viewModel.resultingBitmap.observe(
//            this,
//            Observer { resultImage ->
//                if (resultImage != null) {
//                    updateUIWithResults(resultImage)
//                }
//                enableControls(true)
//            }
//        )
//
////        mainScope.async(inferenceThread) { createModelExecutor(useGPU) }
////        mainScope.launch {
////            println("Dentro corrutina======")
////            createModelExecutor(false)
////        }
//        lifecycleScope.launch(Dispatchers.Default){
//            //createModelExecutor(false)
//        }
// ACTUALIZA AHI MISMO
//        activity?.runOnUiThread {
//            binding.photoDetect.setImageBitmap(imgWithResult)
//        }
//        println("----Antes Botón----")

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

//    }

    private suspend fun createModelExecutor(useGPU: Boolean) {
        mutex.withLock {
            if (ocrModel != null) {
                ocrModel!!.close()
                ocrModel = null
            }
            try {
                ocrModel = OCRModelExecutor(activity?.baseContext!!, useGPU)
            } catch (e: Exception) {
                Log.e(this.tag, "Fail to create OCRModelExecutor: ${e.message}")
//                val logText: TextView = binding.logView
//                logText.text = e.message
            }
        }
    }

    private fun setChipsToLogView(itemsFound: Map<String, Int>) {
//        chipsGroup.removeAllViews()
        //my
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
            var intent = Intent(activity?.baseContext!!, com.example.safe_car_plate.ocr.Result::class.java)
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
        Glide.with(activity?.baseContext!!).load(image).override(250, 250).fitCenter().into(imageView)
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