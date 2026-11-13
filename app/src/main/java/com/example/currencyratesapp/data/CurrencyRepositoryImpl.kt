package com.example.currencyratesapp.data

import android.util.Log
import com.example.currencyratesapp.domain.Currency
import com.example.currencyratesapp.domain.CurrencyRepository

class CurrencyRepositoryImpl : CurrencyRepository {

    // Приблизний поточний курс євро до гривні (для конвертації)
    private val eurToUah = 44.5

    override suspend fun getCurrencies(): List<Currency> {
        return try {
            val response = RetrofitClient.api.getLatestRates()
            val resultList = mutableListOf<Currency>()

            // Додаємо сам Євро (базова валюта)
            resultList.add(
                Currency(
                    code = "EUR", name = "Євро", rate = eurToUah,
                    isFavorite = false, fullName = "Євро",
                    description = "Офіційний курс оновлено: ${response.date}"
                )
            )

            // РОЗШИРЕНИЙ СПИСОК ВАЛЮТ (14 штук)
            val allowedCodes = listOf(
                "USD", "GBP", "CHF", "JPY", "CAD", "AUD", // Основні світові
                "PLN", "CZK", "HUF", "RON", "MDL", "TRY", "CNY"  // Сусіди та партнери
            )

            response.rates.forEach { (code, rateToEur) ->
                // ФІЛЬТР: якщо валюти немає в дозволеному списку - пропускаємо її
                if (code !in allowedCodes) return@forEach

                // Рахуємо скільки гривень коштує 1 одиниця валюти
                val rateInUah = rateToEur * eurToUah

                resultList.add(
                    Currency(
                        code = code,
                        name = getCurrencyName(code),
                        rate = String.format("%.2f", rateInUah).replace(",", ".").toDouble(),
                        isFavorite = false,
                        fullName = getCurrencyName(code),
                        description = "Офіційний курс оновлено: ${response.date}"
                    )
                )
            }
            resultList
        } catch (e: Exception) {
            Log.e("API_ERROR", "Помилка: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    private fun getCurrencyName(code: String): String {
        return when (code) {
            "USD" -> "Долар США"
            "EUR" -> "Євро"
            "GBP" -> "Фунт стерлінгів"
            "CHF" -> "Швейцарський франк"
            "JPY" -> "Японська єна"
            "CAD" -> "Канадський долар"
            "AUD" -> "Австралійський долар"
            "PLN" -> "Польський злотий"
            "CZK" -> "Чеська крона"
            "HUF" -> "Угорський форинт"
            "RON" -> "Румунський лей"
            "MDL" -> "Молдовський лей"
            "TRY" -> "Турецька ліра"
            "CNY" -> "Китайський юань"
            else -> code
        }
    }
}