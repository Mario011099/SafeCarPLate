package com.example.safe_car_plate.model.entities.api

data class Deuda(
    val descripcion: String,
    val rubros: List<Rubro>,
    val subtotal: Double
)