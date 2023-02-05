package com.safecarplate.model.endpoint

import com.safecarplate.model.entities.api.ApiResult
import com.safecarplate.model.entities.api.Users
import retrofit2.Response
import retrofit2.http.GET

interface UsersApi {
    @GET("users")
    suspend fun getUsers() : Response<ApiResult>
}