package com.example.currencyratesapp.domain

class GetCurrenciesUseCase(private val repository: CurrencyRepository) {
    suspend operator fun invoke(): List<Currency> {
        return repository.getCurrencies()
    }
}