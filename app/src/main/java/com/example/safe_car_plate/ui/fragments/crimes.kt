package com.example.safe_car_plate.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.safe_car_plate.ui.webview.TolerantWebViewClient
import com.example.safe_car_plate.databinding.FragmentCrimesBinding
import com.example.safe_car_plate.ui.activities.MainActivity
import com.example.safe_car_plate.usercase.placas.PlacasC
import com.example.safe_car_plate.usercase.users.UsersC
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [crimes.newInstance] factory method to
 * create an instance of this fragment.
 */
class crimes : Fragment() {
    lateinit var bindingCrimes: FragmentCrimesBinding

    var placa:String = ""
//    fun crimes(placa:String){
//        this.placa = placa
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.placa =  if (arguments?.get("placa") != null) arguments?.get("placa") as String else  ""
        bindingCrimes = FragmentCrimesBinding.inflate(inflater,container, false)
        initClass()
        placaDataExists()
        placaData()
        return bindingCrimes.root
    }

    private fun initClass(){
        bindingCrimes.infoFiscalia.settings.javaScriptEnabled = true
        bindingCrimes.infoFiscalia.webViewClient = TolerantWebViewClient();
        bindingCrimes.infoFiscalia.loadUrl("https://www.gestiondefiscalias.gob.ec/siaf/comunes/noticiasdelito/info_mod.php?businfo=a:1:%7Bi:0;s:7:\"$placa\";%7D")

    }

    private fun placaData(){
        lifecycleScope.launch(Dispatchers.Main) {
            val c = PlacasC().getPlacaDataC("PDA7058")
            println("PLACA DATA: "+ c)
        }
    }
    private fun placaDataExists(){
        lifecycleScope.launch(Dispatchers.Main) {
            val c = PlacasC().getPlacaExists("PDA7058")
            println("PLACA EXISTS: "+ c)
        }
    }



}
