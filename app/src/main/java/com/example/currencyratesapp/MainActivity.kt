package com.example.currencyratesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// ✅ ВИПРАВЛЕНО: Імпорт теми з правильного пакета
import com.example.currencyratesapp.ui.theme.CurrencyRatesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyRatesAppTheme {
                CurrencyScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen() {
    val context = LocalContext.current

    val initialItems = remember {
        listOf(
            Currency("USD", "Долар США", 38.45, true),
            Currency("EUR", "Євро", 41.20, false),
            Currency("PLN", "Польський злотий", 9.55, false),
            Currency("GBP", "Фунт стерлінгів", 48.10, true),
            Currency("CHF", "Швейцарський франк", 42.30, false),
            Currency("JPY", "Японська єна", 0.25, false),
            Currency("CAD", "Канадський долар", 28.15, false),
            Currency("CNY", "Китайський юань", 5.30, false)
        )
    }

    var searchQuery by remember { mutableStateOf("") }

    val filteredItems = initialItems.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.code.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Курси валют (НБУ)") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Пошук валюти (напр. USD)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredItems) { currency ->
                    CurrencyCard(
                        currency = currency,
                        onFavoriteClick = {
                            Toast.makeText(context, "Обрано: ${currency.code}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CurrencyCard(currency: Currency, onFavoriteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = currency.code,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currency.name,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "%.2f ₴".format(currency.rate),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        // ✅ ВИПРАВЛЕНО: Використовуємо Icons.Outlined.Star замість StarBorder
                        imageVector = if (currency.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Favorite",
                        tint = if (currency.isFavorite) Color(0xFFFFC107) else Color.Gray
                    )
                }
            }
        }
    }
}