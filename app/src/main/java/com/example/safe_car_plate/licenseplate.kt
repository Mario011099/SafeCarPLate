package com.example.safe_car_plate

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.safe_car_plate.databinding.FragmentLicenseplateBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


lateinit var currentPhotoPath: String
lateinit var binding: FragmentLicenseplateBinding

/**
 * A simple [Fragment] subclass.
 * Use the [licenseplate.newInstance] factory method to
 * create an instance of this fragment.
 */
class licenseplate : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLicenseplateBinding.inflate(inflater, container, false)
        initClass()
        return binding.root
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

    }


}