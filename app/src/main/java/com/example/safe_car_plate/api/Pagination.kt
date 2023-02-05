package com.example.safe_car_plate.api

data class Pagination(
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)