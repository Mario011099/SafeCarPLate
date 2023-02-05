package com.safecarplate.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.safe_car_plate.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.safecarplate.model.endpoint.UsersApi
import com.safecarplate.usercase.users.UsersC
import com.safecarplate.model.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //imprimirUsers()
    }
    private fun imprimirUsers(){
        val UserApi = UsersRepository().getInstance().create(UsersApi::class.java)
        GlobalScope.launch {
            val result = UserApi.getUsers()
            if (result != null)
                Log.d("Users: ", result.body().toString())
        }
    }

    override fun onStart() {
        super.onStart()

        init()
    }

    private fun init() {
        val txt = binding.txtUser
        binding.buttonLogin.setOnClickListener {
            val txtUser = binding.txtUser.text.toString()
            val txtPass = binding.txtPass.text.toString()
            lifecycleScope.launch(Dispatchers.Main) {
                val c = UsersC().getUsersC()
                for(item in c){
                    if(item.email==txtUser && txtPass == "1234" && item.status=="active"){
                        var intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Snackbar.make(
                            txt, "Email o contraseña incorrectos y/o Cuenta inactiva",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                println("USUARIOS: "+ c)

            }}
    }

}