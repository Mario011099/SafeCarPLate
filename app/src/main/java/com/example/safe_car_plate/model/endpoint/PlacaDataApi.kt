package com.example.safe_car_plate.model.endpoint

import com.example.safe_car_plate.model.entities.api.PlacaApiResult
import com.example.safe_car_plate.model.entities.api.PlacaExists
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap




interface PlacaDataApi {

    @GET("valor/{placa}")
    suspend fun getPlacaData(@Path("placa") placa: String) : Response<PlacaApiResult>

    @GET("existePorNumeroPlaca")
    suspend fun getPlacaExists(
        @Query("numeroPlaca") numPlaca: String
    ): Response<PlacaExists>
}