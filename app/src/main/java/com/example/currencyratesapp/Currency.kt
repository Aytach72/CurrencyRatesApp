package com.example.currencyratesapp

data class Currency(
    val code: String,
    val name: String,
    val rate: Double,
    val isFavorite: Boolean = false
)