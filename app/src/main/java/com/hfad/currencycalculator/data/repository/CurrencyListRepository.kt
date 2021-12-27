package com.hfad.currencycalculator.data.repository

import com.hfad.currencycalculator.common.Resource
import com.hfad.currencycalculator.domain.repository.CurrencyListLocalDataSource
import com.hfad.currencycalculator.domain.repository.CurrencyListRemoteDataSource
import com.hfad.currencycalculator.domain.model.CurrencyResponse
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity
import com.hfad.currencycalculator.data.remote.models.toCurrencyListEntity

import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

import javax.inject.Inject

class CurrencyListRepository @Inject constructor(
    private val currencyListLocalDataSource: CurrencyListLocalDataSource,
    private val currencyListRemoteDataSource: CurrencyListRemoteDataSource,
) {

    fun fetchCurrencyListFromSRV(): Flow<Resource<List<CurrencyListEntity>>> = flow {
        try {
            emit(currencyListLocalDataSource.loadAllCurrency())
            emit(Resource.Loading<List<CurrencyListEntity>>())
            val currencyResponse = currencyListRemoteDataSource.getCurrencyList().toCurrencyListEntity()
            emit(Resource.Success<List<CurrencyListEntity>>(currencyResponse))
            currencyListLocalDataSource.deleteTables()
            currencyListLocalDataSource.saveResponse(currencyResponse)
        } catch(e: HttpException) {
            emit(Resource.Error<List<CurrencyListEntity>>(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error<List<CurrencyListEntity>>("Couldn't reach server. Check your internet connection."))
        }
    }.flowOn(Dispatchers.IO)


}