package com.hfad.currencycalculator.presentation.currency_screen

import com.hfad.currencycalculator.data.local.models.CurrencyListEntity
import com.hfad.currencycalculator.domain.model.CurrencyResponse
import com.hfad.currencycalculator.presentation.currency_screen.currency_state.CurrencyEnum


data class CoinListState(
    var mEnum:CurrencyEnum? = null,
    var listEntity: List<CurrencyListEntity> = emptyList(),
    var messageError: String = "",
    var isLoading:Boolean = false
)
