package com.safecarplate.model.entities.api

data class Pagination(
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)