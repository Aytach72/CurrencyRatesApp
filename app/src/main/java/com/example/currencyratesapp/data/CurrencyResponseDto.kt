package com.example.currencyratesapp.data

import com.google.gson.annotations.SerializedName

data class CurrencyResponseDto(
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String,
    @SerializedName("rates") val rates: Map<String, Double>
)