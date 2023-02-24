package com. example.safe_car_plate.ui.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.safe_car_plate.R
import com.example.safe_car_plate.databinding.FragmentCrimesBinding
import com.example.safe_car_plate.databinding.FragmentLicenseplateBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [licenseplate.newInstance] factory method to
 * create an instance of this fragment.
 */
class licenseplate : Fragment() {
    lateinit var binding: FragmentLicenseplateBinding

    //my
    private lateinit var placaImageView: ImageView
    private lateinit var runButton: ImageButton
    private lateinit var consultaButton: Button
    private lateinit var imageInput: InputImage
    private lateinit var placa: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLicenseplateBinding.inflate(inflater, container, false)
        placa = binding.textPlaca.text.toString()
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        initClass()
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
        for (i in blocks.indices) {
            val lines = blocks[i].lines
            for (j in lines.indices) {
                val elements = lines[j].elements
                for (k in elements.indices) {
                    println("Palabra " + elements[k].text)
                    wordsList.add(elements[k].text)
                }
            }
        }
        getListWords(wordsList)

    }

    private fun getListWords(wordsList: MutableList<String?>) {
        var v = wordsList.filter { s-> !(s.equals("ECUADOR") || s.equals("ANT") ) }
        println(v)
        if(v.isNotEmpty()){
            placa = v[0]?.uppercase()!!.replace("-","")
            binding.textResultModel.text = v[0]?.uppercase()
            binding.textPlaca.text = Editable.Factory.getInstance().newEditable(placa)
            binding.btnConsultar.visibility = View.VISIBLE
            binding.idTextTitlePlaca.visibility = View.VISIBLE

        }else{
            showToast("No se encontró ninguna placa")
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    private fun initClass() {
        runButton = binding.buttonBuscar
        consultaButton = binding.btnConsultar
        binding.subirFoto.setOnClickListener {
            requestPermission()
        }
        consultaButton.setOnClickListener {
            consultarDatos()
        }
        binding.buttonBuscar.setOnClickListener {
            consultarDatos()
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

    private fun consultarDatos() {

//        var intent = Intent(activity?.baseContext!!, FragmentCrimesBinding::class.java)
//        intent.putExtra("placa",placa)
////        startActivity(intent)
////        val mFragment: Fragment = FragmentCrimesBinding()
////        fragmentManager().beginTransaction().replace(R.id.content_frame, mFragment)
////            .commit()
        if(placa!=null){
            val placaC = binding.textPlaca.text.toString()
            println("Placa enviada"+ placaC)
            val bundle = Bundle()
            bundle.putString("placa",placaC )
            val crimesFragment = crimes()
            crimesFragment.arguments = bundle
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, crimesFragment)
            transaction?.commit()
        }else{
            Toast.makeText(activity?.baseContext, "Por favor ingrese la placa con una foto o en el campo de texto",Toast.LENGTH_SHORT).show()
        }


    }

}