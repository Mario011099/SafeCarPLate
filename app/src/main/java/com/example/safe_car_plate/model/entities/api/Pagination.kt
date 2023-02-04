package com.example.safe_car_plate.model.entities.api

data class Pagination(
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)