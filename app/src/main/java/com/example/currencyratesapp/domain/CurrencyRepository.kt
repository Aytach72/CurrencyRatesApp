package com.example.currencyratesapp.domain

interface CurrencyRepository {
    suspend fun getCurrencies(): List<Currency>
}