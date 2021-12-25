package com.hfad.currencycalculator.domain.model

data class CurrencyResponse(
    val NumCode: String,
    val CharCode: String,
    val Nominal: Int,
    val Name: String,
    val Value: Double,
    val Previous: Double,
    var currentValue: CharSequence? = null,
)



