package com.example.safe_car_plate.model.endpoint

import com.example.safe_car_plate.model.entities.api.UsersApiResult
import retrofit2.Response
import retrofit2.http.GET

interface UsersApi {
    @GET("users")
    suspend fun getUsers() : Response<UsersApiResult>
}