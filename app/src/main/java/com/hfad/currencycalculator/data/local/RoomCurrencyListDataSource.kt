package com.hfad.currencycalculator.data.local

import com.hfad.currencycalculator.common.Resource
import com.hfad.currencycalculator.domain.repository.CurrencyListLocalDataSource
import com.hfad.currencycalculator.data.remote.models.RemoteValuteResponce
import com.hfad.currencycalculator.data.dao.CurrencyListDao
import com.hfad.currencycalculator.data.local.models.CurrencyEntity
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity
import com.hfad.currencycalculator.data.remote.models.toCurrencyListEntity
import io.reactivex.Completable
import io.reactivex.Single

class RoomCurrencyListDataSource(val currencyListDao: CurrencyListDao) :
    CurrencyListLocalDataSource {
    override suspend fun loadAllCurrency(): Resource<List<CurrencyListEntity>> =
        Resource.Success(currencyListDao.loadAllCurrencies())

    override suspend fun saveResponse(response: List<CurrencyListEntity>) {
        currencyListDao.saveAllCurrencies(response)
    }

    override suspend fun deleteTables() {
        currencyListDao.clearCurrencyListTable()
        currencyListDao.clearCurrencyTable()

    }


}
