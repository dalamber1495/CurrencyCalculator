package com.hfad.currencycalculator.base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hfad.currencycalculator.model.room.dao.CurrencyEntity
import com.hfad.currencycalculator.model.room.dao.CurrencyListDao
import com.hfad.currencycalculator.model.room.dao.CurrencyListEntity

@Database(entities = [
    CurrencyListEntity::class,CurrencyEntity::class
],version = 1, exportSchema = true)
abstract class CurrencyDatabase:RoomDatabase() {

abstract fun currencyListDao(): CurrencyListDao
}