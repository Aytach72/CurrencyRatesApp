package com.example.currencyratesapp.domain

import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    // Читаем данные из базы (Room)
    fun observeCurrencies(): Flow<List<Currency>>
    // Обновляем данные из интернета
    suspend fun refresh()
}