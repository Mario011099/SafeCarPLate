package com.example.safe_car_plate.model.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlacaDataRepository {

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl("https://srienlinea.sri.gob.ec/movil-servicios/api/v1.0/matriculacion/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getInstanceExist(): Retrofit {
        return Retrofit.Builder().baseUrl("https://srienlinea.sri.gob.ec/sri-matriculacion-vehicular-recaudacion-servicio-internet/rest/BaseVehiculo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}