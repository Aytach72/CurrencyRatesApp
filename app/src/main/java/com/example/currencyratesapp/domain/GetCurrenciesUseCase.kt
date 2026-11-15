package com.example.currencyratesapp.domain

import kotlinx.coroutines.flow.Flow

class GetCurrenciesUseCase(private val repository: CurrencyRepository) {
    operator fun invoke(): Flow<List<Currency>> {
        return repository.observeCurrencies()
    }
}