package com.example.currencyratesapp.domain

data class Currency(
    val code: String,
    val name: String,
    val rate: Double,
    val isFavorite: Boolean = false,
    val fullName: String = "Повна назва валюти",
    val description: String = "Детальна інформація про валюту від НБУ."
)