package com.example.safe_car_plate.model.entities.api

data class ApiResult(
    val code: Int,
    val `data`: List<Data>,
    val meta: Meta
)