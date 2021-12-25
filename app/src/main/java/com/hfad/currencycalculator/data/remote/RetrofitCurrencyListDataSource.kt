package com.hfad.currencycalculator.data.remote

import com.hfad.currencycalculator.domain.repository.CurrencyListRemoteDataSource
import com.hfad.currencycalculator.data.remote.models.RemoteValuteResponce
import com.hfad.currencycalculator.data.network.CurrencyApi

class RetrofitCurrencyListDataSource(private val mApi: CurrencyApi) : CurrencyListRemoteDataSource {

    override suspend fun getCurrencyList(): RemoteValuteResponce = mApi.fetchValute()
}