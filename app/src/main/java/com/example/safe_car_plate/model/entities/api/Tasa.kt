package com.example.safe_car_plate.model.entities.api

data class Tasa(
    val descripcion: String,
    val deudas: List<Deuda>,
    val subtotal: Double
)