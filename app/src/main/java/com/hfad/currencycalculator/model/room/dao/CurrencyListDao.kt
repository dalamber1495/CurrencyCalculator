package com.hfad.currencycalculator.model.room.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.DELETE
import java.nio.charset.CodingErrorAction.REPLACE

@Dao
interface CurrencyListDao {

    @Query("SELECT * FROM ${CurrencyListEntity.TABLE_NAME}")
    fun loadAllCurrencies(): Single<List<CurrencyListEntity>>

    @Query("SELECT * FROM ${CurrencyEntity.TABLE_NAME}" )
    fun loadCurrenciesEntity(): Single<List<CurrencyEntity>>

    @Insert(entity = CurrencyListEntity::class,onConflict = OnConflictStrategy.REPLACE)
    fun addCurrency(currencyListEntity: CurrencyListEntity):Completable

    @Insert(entity = CurrencyListEntity::class,onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun saveAllCurrencies(entities:List<CurrencyListEntity>):Completable
    @Insert(entity = CurrencyEntity::class,onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun saveEntityCurrencies(entity:CurrencyEntity):Completable

    @Query("DELETE FROM ${CurrencyListEntity.TABLE_NAME}" )
    fun clearCurrencyListTable():Completable
    @Query("DELETE FROM ${CurrencyEntity.TABLE_NAME}" )
    fun clearCurrencyTable():Completable
}