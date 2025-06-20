package com.example.kurrencyconvert1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.kurrencyconvert1.ui.screen.CurrencyScreen
import com.example.kurrencyconvert1.ui.theme.KurrencyConvert1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KurrencyConvert1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("converter") }

                    when (currentScreen) {
                        "converter" -> CurrencyScreen(
                            onNavigateToHistory = { currentScreen = "history" }
                        )

                    }
                }
            }
        }
    }
}