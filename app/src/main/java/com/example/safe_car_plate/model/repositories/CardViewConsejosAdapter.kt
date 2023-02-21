package com.example.safe_car_plate.model.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.safe_car_plate.R
import com.example.safe_car_plate.ui.fragments.DetalleFragment

class CardViewConsejosAdapter(private val listaConsejos: ArrayList<Consejos>) :
    RecyclerView.Adapter<CardViewConsejosAdapter.CardViewViewHolder>() {

    inner class CardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgFoto: ImageView = itemView.findViewById(R.id.img_consejo)
        var tvNombre: TextView = itemView.findViewById(R.id.tv_nombre)
        var tvDescripcion: TextView = itemView.findViewById(R.id.tv_descripcion)
    }

    override fun getItemCount(): Int {
        return listaConsejos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_consejos, parent, false)
        return CardViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewViewHolder, position: Int) {
        val tips = listaConsejos[position]
        Glide.with(holder.itemView.context)
            .load(tips.foto)
            .apply(RequestOptions().override(350, 550))
            .into(holder.imgFoto)
        holder.tvNombre.text = tips.nombre
        holder.tvDescripcion.text = tips.descripcion
        holder.itemView.setOnClickListener {

            val mDetalleFragment = DetalleFragment()
            val nombre = listaConsejos[holder.adapterPosition].nombre
            val descripcion = listaConsejos[holder.adapterPosition].descripcion
            val foto = listaConsejos[holder.adapterPosition].foto

            val mBundle = Bundle()
            mBundle.putString(DetalleFragment.EXTRA_NOMBRE, nombre)
            mBundle.putString(DetalleFragment.EXTRA_DESCRIPCION, descripcion)
            mBundle.putInt(DetalleFragment.EXTRA_FOTO, foto)
            mDetalleFragment.arguments = mBundle

            val activity = it.context as AppCompatActivity
            activity.supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.frame_layout,
                    mDetalleFragment,
                    DetalleFragment::class.java.simpleName
                )
                .commit()

        }
    }
}
