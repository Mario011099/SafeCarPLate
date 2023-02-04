package com.safecarplate

import com.example.safe_car_plate.model.entities.api.ApiResult
import retrofit2.Response
import retrofit2.http.GET

interface UsersApi {
    @GET("users")
    suspend fun getUsers() : Response<ApiResult>
}