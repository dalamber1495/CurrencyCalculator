package com.hfad.currencycalculator.model.retrofit

import com.hfad.currencycalculator.model.local.CurrencyListRemoteDataSource
import com.hfad.currencycalculator.model.remote.RemoteValuteResponce
import com.hfad.currencycalculator.network.CurrencyApi
import io.reactivex.Single

class RetrofitCurrencyListDataSource(val mApi: CurrencyApi) : CurrencyListRemoteDataSource {

    override fun getCurrencyList(): Single<RemoteValuteResponce> = mApi.fetchValute()
}