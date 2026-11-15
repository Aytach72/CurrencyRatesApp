package com.example.currencyratesapp.data

import com.example.currencyratesapp.data.database.CurrencyEntity
import com.example.currencyratesapp.domain.Currency

// Превращаем данные из базы в модель для UI
fun CurrencyEntity.toDomain(): Currency {
    return Currency(code, name, rate, isFavorite, fullName, description)
}

// Превращаем модель UI в данные для базы (чтобы сохранить)
fun Currency.toEntity(): CurrencyEntity {
    return CurrencyEntity(code, name, rate, isFavorite, fullName, description)
}