package com.hfad.currencycalculator.model.local.models

import com.hfad.currencycalculator.model.room.dao.CurrencyListEntity

data class CurrencyResponse(
    val NumCode: String,
    val CharCode: String,
    val Nominal: Int,
    val Name: String,
    val Value: Double,
    val Previous: Double,
    var currentValue: CharSequence? = null,
)


fun CurrencyListEntity.mapToCurrencyResponse() = CurrencyResponse(
    NumCode = NumCode,
    CharCode = CharCode,
    Nominal = Nominal/Nominal,
    Name = Name,
    Value = Value/Nominal,
    Previous = Previous/Nominal
)
