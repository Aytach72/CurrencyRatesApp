package com.example.currencyratesapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyratesapp.data.CurrencyRepositoryImpl
import com.example.currencyratesapp.domain.Currency
import com.example.currencyratesapp.domain.GetCurrenciesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CurrencyUiState(
    val items: List<Currency> = emptyList(),
    val searchQuery: String = "",
    val isError: Boolean = false
)

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {

    // Передаем контекст приложения для базы Room
    private val repository = CurrencyRepositoryImpl(application.applicationContext)
    private val getCurrenciesUseCase = GetCurrenciesUseCase(repository)

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        // 1. Подписываемся на базу (данные из кеша придут сразу)
        viewModelScope.launch {
            getCurrenciesUseCase().collect { currencies ->
                _uiState.update { currentState ->
                    currentState.copy(items = currencies)
                }
            }
        }

        // 2. Запускаем обновление из интернета в фоне
        viewModelScope.launch {
            repository.refresh()
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
    }

    fun toggleFavorite(currencyCode: String) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.map { currency ->
                if (currency.code == currencyCode) {
                    currency.copy(isFavorite = !currency.isFavorite)
                } else {
                    currency
                }
            }
            currentState.copy(items = updatedItems)
        }
    }
}