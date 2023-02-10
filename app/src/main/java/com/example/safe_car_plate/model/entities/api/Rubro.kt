package com.example.safe_car_plate.model.entities.api

data class Rubro(
    val beneficiario: String,
    val descripcion: String,
    val detallesRubro: List<DetallesRubro>,
    val periodoFiscal: String,
    val valor: Double
)