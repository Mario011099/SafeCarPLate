package com.example.safe_car_plate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.safe_car_plate.databinding.FragmentCrimesBinding

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
        return bindingCrimes.root
    }

    private fun initClass(){
        bindingCrimes.infoFiscalia.settings.javaScriptEnabled = true
        bindingCrimes.infoFiscalia.webViewClient = TolerantWebViewClient();
        bindingCrimes.infoFiscalia.loadUrl("https://www.gestiondefiscalias.gob.ec/siaf/comunes/noticiasdelito/info_mod.php?businfo=a:1:%7Bi:0;s:7:\"$placa\";%7D")

    }


}
