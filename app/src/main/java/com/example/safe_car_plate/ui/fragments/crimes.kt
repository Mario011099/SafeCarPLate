package com.example.safe_car_plate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.safe_car_plate.R
import com.example.safe_car_plate.databinding.FragmentCrimesBinding
import com.example.safe_car_plate.ui.webview.TolerantWebViewClient
import com.example.safe_car_plate.usercase.placas.PlacasC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [crimes.newInstance] factory method to
 * create an instance of this fragment.
 */
class crimes : Fragment() {
    lateinit var bindingCrimes: FragmentCrimesBinding

    var placa: String = ""
    var placaNoExiste = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.placa = if (arguments?.get("placa") != null) arguments?.get("placa") as String else ""
        bindingCrimes = FragmentCrimesBinding.inflate(inflater, container, false)
        initClass()
        placaDataExists()
        placaData()
        return bindingCrimes.root
    }

    private fun initClass() {
        placaNoExiste = resources.getString(R.string.placa_no_existe)
        bindingCrimes.infoFiscalia.settings.javaScriptEnabled = true
        bindingCrimes.infoFiscalia.webViewClient = TolerantWebViewClient();
        bindingCrimes.infoFiscalia.loadUrl("https://www.gestiondefiscalias.gob.ec/siaf/comunes/noticiasdelito/info_mod.php?businfo=a:1:%7Bi:0;s:7:\"$placa\";%7D")

    }

    private fun placaData() {
        lifecycleScope.launch(Dispatchers.Main) {
            val c = PlacasC().getPlacaDataC(placa)
            println("PLACA DATA: " + c)
            if (c != null) {
                bindingCrimes.anioModelo.text = c.anioModelo.toString()
                bindingCrimes.anioUltimoPago.text = c.anioUltimoPago.toString()
                bindingCrimes.claseVehiculo.text = c.clase
                bindingCrimes.marcaVehiculo.text = c.marca
                bindingCrimes.modeloVehiculo.text = c.modelo
            }else{
                bindingCrimes.anioModelo.text = placaNoExiste
                bindingCrimes.anioUltimoPago.text = placaNoExiste
                bindingCrimes.claseVehiculo.text = placaNoExiste
                bindingCrimes.marcaVehiculo.text = placaNoExiste
                bindingCrimes.modeloVehiculo.text = placaNoExiste
            }
        }
    }

    private fun placaDataExists() {
        lifecycleScope.launch(Dispatchers.Main) {
            val c = PlacasC().getPlacaExists(placa)
            if (c != null) bindingCrimes.existePlaca.text = c.mensaje else bindingCrimes.existePlaca.text = placaNoExiste
            println("PLACA EXISTS: " + c)
        }
    }



}
