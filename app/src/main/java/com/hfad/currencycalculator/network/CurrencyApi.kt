package com.hfad.currencycalculator.network

import com.hfad.currencycalculator.model.remote.RemoteValuteResponce
import io.reactivex.Single
import retrofit2.http.GET

interface CurrencyApi {

    @GET("./daily_json.js")
    fun fetchValute(): Single<RemoteValuteResponce>

}