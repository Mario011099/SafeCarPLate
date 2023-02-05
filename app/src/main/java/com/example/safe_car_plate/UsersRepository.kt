package com.example.safe_car_plate

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class UsersRepository {

    fun getInstance(): Retrofit{
        return Retrofit.Builder().baseUrl("https://gorest.co.in/public-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}