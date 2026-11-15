package com.example.currencyratesapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey val code: String,
    val name: String,
    val rate: Double,
    val isFavorite: Boolean,
    val fullName: String,
    val description: String
)