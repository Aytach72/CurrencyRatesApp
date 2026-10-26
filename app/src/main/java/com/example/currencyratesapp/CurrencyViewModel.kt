package com.example.currencyratesapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// 1. Описуємо стан екрана (UiState)
data class CurrencyUiState(
    val items: List<Currency> = emptyList(),
    val searchQuery: String = ""
)

class CurrencyViewModel : ViewModel() {

    // 2. Приватний MutableStateFlow
    private val _uiState = MutableStateFlow(CurrencyUiState())

    // 3. Публічний StateFlow (лише для читання)
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    // 4. Ініціалізація тестовим списком
    init {
        _uiState.value = CurrencyUiState(
            items = listOf(
                Currency("USD", "Долар США", 38.45, true, "Долар США", "Офіційний курс НБУ. Основна світова резервна валюта."),
                Currency("EUR", "Євро", 41.20, false, "Євро", "Офіційна валюта Європейського Союзу."),
                Currency("PLN", "Польський злотий", 9.55, false, "Польський злотий", "Національна валюта Польщі."),
                Currency("GBP", "Фунт стерлінгів", 48.10, true, "Фунт стерлінгів", "Національна валюта Великої Британії."),
                Currency("CHF", "Швейцарський франк", 42.30, false, "Швейцарський франк", "Валюта Швейцарії, відома своєю стабільністю."),
                Currency("JPY", "Японська єна", 0.25, false, "Японська єна", "Національна валюта Японії."),
                Currency("CAD", "Канадський долар", 28.15, false, "Канадський долар", "Валюта Канади."),
                Currency("CNY", "Китайський юань", 5.30, false, "Китайський юань", "Валюта Китайської Народної Республіки.")
            )
        )
    }

    // 5. Метод для оновлення пошуку
    fun onSearchQueryChange(query: String) {
        _uiState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
    }
}