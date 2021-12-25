package com.hfad.currencycalculator.base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hfad.currencycalculator.data.local.models.CurrencyEntity
import com.hfad.currencycalculator.data.dao.CurrencyListDao
import com.hfad.currencycalculator.data.local.models.CurrencyListEntity

@Database(entities = [
    CurrencyListEntity::class, CurrencyEntity::class
],version = 1, exportSchema = true)
abstract class CurrencyDatabase:RoomDatabase() {

abstract fun currencyListDao(): CurrencyListDao
}