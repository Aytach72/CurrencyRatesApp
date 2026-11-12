package com.example.currencyratesapp.presentation

import androidx.lifecycle.ViewModel
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
    val searchQuery: String = ""
)

class CurrencyViewModel : ViewModel() {
    private val repository = CurrencyRepositoryImpl()
    private val getCurrenciesUseCase = GetCurrenciesUseCase(repository)

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            val currencies = getCurrenciesUseCase()
            _uiState.update { currentState ->
                currentState.copy(items = currencies)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
    }
}