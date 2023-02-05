package com.example.safe_car_plate

import com.example.safe_car_plate.api.ApiResult
import retrofit2.Response
import retrofit2.http.GET

interface UsersApi {
    @GET("users")
    suspend fun getUsers() : Response<ApiResult>
}