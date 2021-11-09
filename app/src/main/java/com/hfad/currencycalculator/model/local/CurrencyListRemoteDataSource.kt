package com.hfad.currencycalculator.model.local

import com.hfad.currencycalculator.model.remote.RemoteValuteResponce
import io.reactivex.Single

interface CurrencyListRemoteDataSource {
    fun getCurrencyList():Single<RemoteValuteResponce>
}