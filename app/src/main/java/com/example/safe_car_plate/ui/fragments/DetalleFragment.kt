package com.example.safe_car_plate.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.safe_car_plate.R
import de.hdodenhof.circleimageview.CircleImageView

class DetalleFragment : Fragment(), View.OnClickListener{

    private lateinit var ivImgDetalle : ImageView
    private lateinit var btnAtras : CircleImageView
    private lateinit var tvNombreDetalle : TextView
    private lateinit var tvDescripcionDetalle : TextView

    companion object {
        var EXTRA_NOMBRE = "extra_name"
        var EXTRA_DESCRIPCION = "extra_description"
        var EXTRA_FOTO = "extra_photo"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivImgDetalle = view.findViewById(R.id.iv_imgen_csl)
        btnAtras = view.findViewById(R.id.btn_atras)
        tvNombreDetalle = view.findViewById(R.id.tv_nombre_csl)
        tvDescripcionDetalle = view.findViewById(R.id.tv_descripcion_csl)

        btnAtras.setOnClickListener(this)

        if (arguments != null) {
            tvNombreDetalle.text = arguments?.getString(EXTRA_NOMBRE)
            tvDescripcionDetalle.text = arguments?.getString(EXTRA_DESCRIPCION)
            val photo = arguments?.getInt(EXTRA_FOTO,0)

            Glide.with(this)
                .load(photo)
                .apply(RequestOptions())
                .into(ivImgDetalle)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val mHomeFragment = HomeFragment()
                val mFragmentManager = parentFragmentManager
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_layout, mHomeFragment, HomeFragment::class.java.simpleName)
                    commit()
                }
            }
        })

    }


    override fun onClick(view: View) {
        when(view.id) {
            R.id.btn_atras -> {
                val mHomeFragment = HomeFragment()
                val mFragmentManager = parentFragmentManager
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_layout, mHomeFragment, HomeFragment::class.java.simpleName)
                    commit()
                }
            }
            else -> {}
        }
    }


}