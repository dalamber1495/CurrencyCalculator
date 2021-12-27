package com.hfad.currencycalculator.domain.model

data class CurrencyResponse(
    val NumCode: String? = null,
    val CharCode: String? = null,
    val Nominal: Int? = null,
    val Name: String? = null,
    val Value: Double? = null,
    val Previous: Double? = null,
    var currentValue: CharSequence? = null
)

fun CurrencyResponse.mapToChange(
    NumCode: String? = this.NumCode,
    CharCode: String? = this.CharCode,
    Nominal: Int? = this.Nominal,
    Name: String? = this.Name,
    Value: Double? = this.Value,
    Previous: Double? = this.Previous,
    currentValue: CharSequence? = this.currentValue
): CurrencyResponse =
    CurrencyResponse(NumCode, CharCode, Nominal, Name, Value, Previous, currentValue)



