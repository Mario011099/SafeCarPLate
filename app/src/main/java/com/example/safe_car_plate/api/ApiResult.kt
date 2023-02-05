package com.example.safe_car_plate.api

data class ApiResult(
    val code: Int,
    val `data`: List<Data>,
    val meta: Meta
)