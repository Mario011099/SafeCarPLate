package com.example.safe_car_plate.model.endpoint

import com.example.safe_car_plate.model.entities.api.PlacaApiResult
import com.example.safe_car_plate.model.entities.api.Users
import com.example.safe_car_plate.model.entities.api.UsersApiResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersApi {
    @GET("users")
    suspend fun getUsers() : Response<UsersApiResult>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int) : Response<Users>
}