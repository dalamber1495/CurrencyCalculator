package com.hfad.currencycalculator.data.remote.models

import com.hfad.currencycalculator.data.local.models.CurrencyListEntity

data class RemoteValuteResponce(
    val Date: String,
    val PreviousDate: String,
    val PreviousURL: String,
    val Timestamp: String,
    val Valute: Map<String, RemoteValute>
)

fun RemoteValuteResponce.toCurrencyListEntity(): List<CurrencyListEntity> {
    val listEntity = mutableListOf<CurrencyListEntity>(
        CurrencyListEntity(
            NumCode = "1",
            CharCode = "RUB",
            Nominal = 1,
            Name = "Российский рубль",
            Value = 1.0,
            Previous = 1.0
        )
    )
    listEntity.addAll(Valute.map {
            CurrencyListEntity(
                NumCode = it.value.NumCode,
                CharCode = it.value.CharCode,
                Nominal = it.value.Nominal,
                Name = it.value.Name,
                Value = it.value.Value,
                Previous = it.value.Previous
            )
    }
    )
    return listEntity
}
