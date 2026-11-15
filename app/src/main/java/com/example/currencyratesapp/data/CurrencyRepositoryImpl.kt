package com.example.currencyratesapp.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.currencyratesapp.data.database.AppDatabase
import com.example.currencyratesapp.domain.Currency
import com.example.currencyratesapp.domain.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrencyRepositoryImpl(context: Context) : CurrencyRepository {

    // Подключаем базу данных Room
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "currency_database"
    ).fallbackToDestructiveMigration().build()

    private val dao = db.currencyDao()
    private val eurToUah = 44.5

    // 1. Читаем ВСЕГДА из базы данных
    override fun observeCurrencies(): Flow<List<Currency>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // 2. Обновляем данные из сети и сохраняем в базу
    override suspend fun refresh() {
        try {
            val response = RetrofitClient.api.getLatestRates()
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

            // СОХРАНЯЕМ В БАЗУ
            dao.insertAll(resultList.map { it.toEntity() })
            Log.d("API_SUCCESS", "Данные сохранены в Room")

        } catch (e: Exception) {
            // Если нет интернета, ничего не стираем, просто пишем в лог
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