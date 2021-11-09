package com.hfad.currencycalculator.model.local

import com.hfad.currencycalculator.model.remote.RemoteValuteResponce
import com.hfad.currencycalculator.model.room.dao.CurrencyListEntity
import io.reactivex.Completable
import io.reactivex.Single

interface CurrencyListLocalDataSource {
    fun loadAllCurrency(): Single<List<CurrencyListEntity>>
    fun saveResponse(responce: RemoteValuteResponce):Completable
    fun deleteTables():Completable
}