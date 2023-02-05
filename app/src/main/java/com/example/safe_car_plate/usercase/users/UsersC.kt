package com.safecarplate.usercase.users

import android.util.Log
import com.safecarplate.model.endpoint.UsersApi
import com.safecarplate.model.repositories.UsersRepository
import com.safecarplate.model.entities.api.Data


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
}