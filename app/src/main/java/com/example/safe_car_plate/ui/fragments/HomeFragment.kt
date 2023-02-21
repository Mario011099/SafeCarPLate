package com.example.safe_car_plate.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safe_car_plate.R
import com.example.safe_car_plate.model.repositories.CardViewConsejosAdapter
import com.example.safe_car_plate.model.repositories.Consejos
import com.example.safe_car_plate.model.repositories.ConsejosData


class HomeFragment : Fragment() {

    private lateinit var rvConsejos: RecyclerView
    private var list: ArrayList<Consejos> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvConsejos = view.findViewById(R.id.rv_consejos)
        rvConsejos.setHasFixedSize(true)
        list.addAll(ConsejosData.listData)
        showRecyclerCardView()
    }

    private fun showRecyclerCardView() {
        rvConsejos.layoutManager = LinearLayoutManager(activity)
        val cardViewDestinationAdapter = CardViewConsejosAdapter(list)
        rvConsejos.adapter = cardViewDestinationAdapter
    }


}