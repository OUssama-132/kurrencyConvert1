package com.example.kurrencyconvert1.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kurrencyconvert1.data.api.CurrencyApi
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.exchangerate.host/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(CurrencyApi::class.java)
    private val database = FirebaseDatabase.getInstance().reference.child("conversions")
    private val apiKey = "f53fe76372b717ca79c99d06d9c266ea"

    private val _conversionResult = MutableStateFlow<Double?>(null)
    val conversionResult: StateFlow<Double?> = _conversionResult

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun convertCurrency(from: String, to: String, amount: Double) {
        viewModelScope.launch {
            try {
                val response = api.convertCurrency(from, to, amount, apiKey)
                if (response.success) {
                    val result = response.result
                    _conversionResult.value = result
                    saveConversionToFirebase(from, to, amount, result)
                } else {
                    _error.value = "Conversion failed:"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            }
        }
    }

    private fun saveConversionToFirebase(
        from: String,
        to: String,
        amount: Double,
        result: Double
    ) {
        val conversionData = mapOf(
            "from" to from,
            "to" to to,
            "amount" to amount,
            "result" to result,
            "timestamp" to System.currentTimeMillis()
        )

        database.push().setValue(conversionData)
            .addOnFailureListener { e ->
                _error.value = "Firebase save failed: ${e.message}"
            }
    }

    fun clearError() {
        _error.value = null
    }
}