package com.example.currencyratesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrencyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}