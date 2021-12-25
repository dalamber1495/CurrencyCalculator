package com.hfad.currencycalculator.domain.repository

import com.hfad.currencycalculator.common.Resource
import com.hfad.currencycalculator.data.remote.models.RemoteValuteResponce
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity
import io.reactivex.Completable
import io.reactivex.Single

interface CurrencyListLocalDataSource {
    suspend fun loadAllCurrency(): Resource<List<CurrencyListEntity>>
    suspend fun saveResponse(response: List<CurrencyListEntity>)
    suspend fun deleteTables()
}