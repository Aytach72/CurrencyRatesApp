package com.example.currencyratesapp.data

import android.util.Log
import com.example.currencyratesapp.data.database.CurrencyDao
import com.example.currencyratesapp.domain.Currency
import com.example.currencyratesapp.domain.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyApi,
    private val dao: CurrencyDao
) : CurrencyRepository {

    private val eurToUah = 44.5

    override fun observeCurrencies(): Flow<List<Currency>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refresh() {
        try {
            val response = api.getLatestRates()
            val resultList = mutableListOf<Currency>()

            resultList.add(
                Currency("EUR", "Євро", eurToUah, false, "Євро", "Курс: ${response.date}")
            )

            val allowedCodes = listOf(
                "USD", "GBP", "CHF", "JPY", "CAD", "AUD",
                "PLN", "CZK", "HUF", "RON", "MDL", "TRY", "CNY"
            )

            response.rates.forEach { (code, rateToEur) ->
                if (code !in allowedCodes) return@forEach
                val rateInUah = rateToEur * eurToUah
                resultList.add(
                    Currency(
                        code = code, name = getCurrencyName(code),
                        rate = String.format("%.2f", rateInUah).replace(",", ".").toDouble(),
                        isFavorite = false, fullName = getCurrencyName(code),
                        description = "Курс: ${response.date}"
                    )
                )
            }

            dao.insertAll(resultList.map { it.toEntity() })
            Log.d("API_SUCCESS", "Данные сохранены в Room")

        } catch (e: Exception) {
            Log.e("API_ERROR", "Нет сети, показываем кеш: ${e.message}")
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