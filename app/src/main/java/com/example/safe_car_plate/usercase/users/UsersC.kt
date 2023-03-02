package com.example.safe_car_plate.usercase.users

import android.util.Log
import com.example.safe_car_plate.model.entities.api.Data
import com.example.safe_car_plate.model.endpoint.UsersApi
import com.example.safe_car_plate.model.entities.api.Users
import com.example.safe_car_plate.model.repositories.UsersRepository


class UsersC {

    suspend fun getUsersC(): List<Data> {
        var c: List<Data> = emptyList()
        try {
            val service = UsersRepository().getInstance()
            val response = service.create(UsersApi::class.java).getUsers()
            if (response.isSuccessful) {
                val d = response.body()!!
                println(d.data)
                val ed = d.data
                c=d.data
            } else {
                throw Exception("Fracaso conexion")
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
        return c
    }

    suspend fun getUsersCById(id: Int): Users? {
        var c: Users? = null
        try {
            val service = UsersRepository().getInstance()
            val response = service.create(UsersApi::class.java).getUserById(id)
            if (response.isSuccessful) {
                c = response.body()!!
            } else {
                throw Exception("Fracaso conexion UserById")
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
        return c
    }
}