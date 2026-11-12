package com.example.currencyratesapp.data

import retrofit2.http.GET

interface CurrencyApi {
    // Запитуємо курси відносно Євро (EUR)
    @GET("latest?from=EUR")
    suspend fun getLatestRates(): CurrencyResponseDto
}