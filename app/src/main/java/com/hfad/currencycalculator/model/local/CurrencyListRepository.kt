package com.hfad.currencycalculator.model.local

import com.hfad.currencycalculator.model.local.models.CurrencyResponse
import com.hfad.currencycalculator.model.local.models.mapToCurrencyResponse
import com.hfad.currencycalculator.model.room.dao.CurrencyListEntity

import io.reactivex.Completable
import io.reactivex.Single

import javax.inject.Inject

class CurrencyListRepository @Inject constructor(
    private val currencyListLocalDataSource: CurrencyListLocalDataSource,
    private val currencyListRemoteDataSource: CurrencyListRemoteDataSource,
) {

    private var mLeftCurrency: CurrencyResponse? = null
    private var mRightCurrency: CurrencyResponse? = null

    fun fetchCurrencyListFromSRV(): Single<List<CurrencyListEntity>> {

        return currencyListRemoteDataSource.getCurrencyList()
            .flatMap {
                clearTables().andThen(
                currencyListLocalDataSource.saveResponse(it))
                    .andThen(currencyListLocalDataSource.loadAllCurrency())

            }

    }
    fun fetchCurrencyListFromDB():Single<List<CurrencyListEntity>>{
        return currencyListLocalDataSource.loadAllCurrency()
    }

    fun saveLeftCurrency(mLeftCurrency: CurrencyResponse?,mLeftValue:CharSequence?){
        this.mLeftCurrency = mLeftCurrency
        this.mLeftCurrency?.currentValue = mLeftValue
    }
    fun saveRightCurrency(mRightCurrency: CurrencyResponse?,mRightValue:CharSequence?){
        this.mRightCurrency = mRightCurrency
        this.mRightCurrency?.currentValue = mRightValue
    }
    fun getLeftCurrency():CurrencyResponse? = mLeftCurrency
    fun getRightCurrency():CurrencyResponse? = mRightCurrency
    private fun clearTables(): Completable = currencyListLocalDataSource.deleteTables()

}