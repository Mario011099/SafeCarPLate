package com.example.safe_car_plate.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.safe_car_plate.R
import com.example.safe_car_plate.ui.fragments.about
import com.example.safe_car_plate.ui.fragments.crimes
import com.example.safe_car_plate.databinding.ActivityMainBinding
import com.example.safe_car_plate.ui.fragments.HomeFragment
import com.example.safe_car_plate.ui.fragments.licenseplate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //@SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(licenseplate())

       binding.navBottom1.setOnItemSelectedListener {

           when(it.itemId){
                R.id.search_licenseplate -> replaceFragment(licenseplate())
                R.id.search_crimes -> replaceFragment(crimes())
                R.id.about -> replaceFragment(about())
                R.id.tip -> replaceFragment(HomeFragment())

                else ->{

                }
            }
           true
        }
    }

     fun replaceFragment( fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayout.id, fragment)
         fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}