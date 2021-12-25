package com.hfad.currencycalculator.data.network

import com.hfad.currencycalculator.data.remote.models.RemoteValuteResponce
import retrofit2.http.GET

interface CurrencyApi {

    @GET("./daily_json.js")
    suspend fun fetchValute(): RemoteValuteResponce

}