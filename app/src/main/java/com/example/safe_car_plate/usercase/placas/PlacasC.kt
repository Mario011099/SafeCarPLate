package com.example.safe_car_plate.usercase.placas

import android.util.Log
import com.example.safe_car_plate.model.endpoint.PlacaDataApi
import com.example.safe_car_plate.model.entities.api.PlacaApiResult
import com.example.safe_car_plate.model.entities.api.PlacaExists
import com.example.safe_car_plate.model.repositories.PlacaDataRepository

class PlacasC {

    suspend fun getPlacaDataC(placa: String): PlacaApiResult? {
        var d: PlacaApiResult? = null
        try {
            val service = PlacaDataRepository().getInstance()
            val response = service.create(PlacaDataApi::class.java).getPlacaData(placa)
            if (response.isSuccessful) {
                d = response.body()!!
                println(d)
                println(d.placa)
            } else {
                throw Exception("Fracaso conexion")
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
        return d
    }

    suspend fun getPlacaExists(placa: String): PlacaExists? {
        var d: PlacaExists? = null
        try {
            val service = PlacaDataRepository().getInstanceExist()
            val response = service.create(PlacaDataApi::class.java).getPlacaExists(placa)
            if (response.isSuccessful) {
                d = response.body()!!
                println(d)
            } else {
                throw Exception("Fracaso conexion")
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
        return d
    }
}