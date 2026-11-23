package com.example.currencyratesapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.currencyratesapp.data.CurrencyApi
import com.example.currencyratesapp.data.CurrencyRepositoryImpl
import com.example.currencyratesapp.data.RetrofitClient
import com.example.currencyratesapp.data.database.AppDatabase
import com.example.currencyratesapp.data.database.CurrencyDao
import com.example.currencyratesapp.domain.CurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi {
        return RetrofitClient.api
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "currency_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCurrencyDao(db: AppDatabase): CurrencyDao {
        return db.currencyDao()
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        api: CurrencyApi,
        dao: CurrencyDao
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(api, dao)
    }
}