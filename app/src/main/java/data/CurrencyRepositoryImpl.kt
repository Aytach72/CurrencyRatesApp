package com.example.currencyratesapp.data

import com.example.currencyratesapp.domain.Currency
import com.example.currencyratesapp.domain.CurrencyRepository

class CurrencyRepositoryImpl : CurrencyRepository {
    override suspend fun getCurrencies(): List<Currency> {
        return listOf(
            Currency("USD", "Долар США", 38.45, true, "Долар США", "Офіційний курс НБУ. Основна світова резервна валюта."),
            Currency("EUR", "Євро", 41.20, false, "Євро", "Офіційна валюта Європейського Союзу."),
            Currency("PLN", "Польський злотий", 9.55, false, "Польський злотий", "Національна валюта Польщі."),
            Currency("GBP", "Фунт стерлінгів", 48.10, true, "Фунт стерлінгів", "Національна валюта Великої Британії."),
            Currency("CHF", "Швейцарський франк", 42.30, false, "Швейцарський франк", "Валюта Швейцарії, відома своєю стабільністю."),
            Currency("JPY", "Японська єна", 0.25, false, "Японська єна", "Національна валюта Японії."),
            Currency("CAD", "Канадський долар", 28.15, false, "Канадський долар", "Валюта Канади."),
            Currency("CNY", "Китайський юань", 5.30, false, "Китайський юань", "Валюта Китайської Народної Республіки.")
        )
    }
}