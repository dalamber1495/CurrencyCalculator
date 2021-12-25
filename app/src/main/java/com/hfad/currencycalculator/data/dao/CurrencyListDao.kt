package com.hfad.currencycalculator.data.dao

import androidx.room.*
import com.hfad.currencycalculator.data.local.models.CurrencyEntity
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CurrencyListDao {

    @Query("SELECT * FROM ${CurrencyListEntity.TABLE_NAME}")
    suspend fun loadAllCurrencies(): List<CurrencyListEntity>

    @Query("SELECT * FROM ${CurrencyEntity.TABLE_NAME}" )
    suspend fun loadCurrenciesEntity(): List<CurrencyEntity>

    @Insert(entity = CurrencyListEntity::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCurrency(currencyListEntity: CurrencyListEntity)

    @Insert(entity = CurrencyListEntity::class,onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun saveAllCurrencies(entities:List<CurrencyListEntity>)
    @Insert(entity = CurrencyEntity::class,onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun saveEntityCurrencies(entity: CurrencyEntity)

    @Query("DELETE FROM ${CurrencyListEntity.TABLE_NAME}" )
    suspend fun clearCurrencyListTable()
    @Query("DELETE FROM ${CurrencyEntity.TABLE_NAME}" )
    suspend fun clearCurrencyTable()
}