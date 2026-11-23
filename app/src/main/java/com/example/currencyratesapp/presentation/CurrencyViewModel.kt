package com.example.currencyratesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyratesapp.domain.Currency
import com.example.currencyratesapp.domain.CurrencyRepository
import com.example.currencyratesapp.domain.GetCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ВОТ ЭТОТ КЛАСС ДОЛЖЕН БЫТЬ ОБЯЗАТЕЛЬНО
data class CurrencyUiState(
    val items: List<Currency> = emptyList(),
    val searchQuery: String = ""
)

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val getCurrenciesUseCase = GetCurrenciesUseCase(repository)

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            getCurrenciesUseCase().collect { currencies ->
                _uiState.update { it.copy(items = currencies) }
            }
        }
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