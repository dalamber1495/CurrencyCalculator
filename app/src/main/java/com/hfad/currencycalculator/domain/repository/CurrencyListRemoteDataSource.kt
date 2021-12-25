package com.hfad.currencycalculator.domain.repository

import com.hfad.currencycalculator.data.remote.models.RemoteValuteResponce

interface CurrencyListRemoteDataSource {
    suspend fun getCurrencyList(): RemoteValuteResponce
}