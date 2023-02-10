package com.example.safe_car_plate.model.entities.api

data class PlacaApiResult(
    val anioModelo: Int,
    val anioUltimoPago: Int,
    val camvCpn: String,
    val cantonMatricula: String,
    val cilindraje: Double,
    val clase: String,
    val deudas: List<Deuda>,
    val estadoAuto: String,
    val fechaCaducidadMatricula: Long,
    val fechaCompra: Long,
    val fechaRevision: Long,
    val fechaUltimaMatricula: Long,
    val informacion: Any,
    val marca: String,
    val mensajeMotivoAuto: Any,
    val modelo: String,
    val paisFabricacion: String,
    val placa: String,
    val remision: Any,
    val servicio: String,
    val tasas: List<Tasa>,
    val tipoUso: String,
    val total: Double
)