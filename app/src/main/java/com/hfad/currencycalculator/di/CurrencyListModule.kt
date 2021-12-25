package com.hfad.currencycalculator.di

import android.content.Context
import androidx.room.Room
import com.hfad.currencycalculator.base.CurrencyDatabase
import com.hfad.currencycalculator.domain.repository.CurrencyListLocalDataSource
import com.hfad.currencycalculator.domain.repository.CurrencyListRemoteDataSource
import com.hfad.currencycalculator.data.repository.CurrencyListRepository
import com.hfad.currencycalculator.data.local.RoomCurrencyListDataSource
import com.hfad.currencycalculator.data.remote.RetrofitCurrencyListDataSource
import com.hfad.currencycalculator.data.network.CurrencyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
class CurrencyListModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext myApplication: Context) :CurrencyDatabase =
        Room.databaseBuilder(
            myApplication,
            CurrencyDatabase::class.java,
            "currency_room_database"
        ).build()

    @Provides
    @Singleton
    fun provideLocalDataSource(currencyDatabase: CurrencyDatabase): CurrencyListLocalDataSource =
        RoomCurrencyListDataSource(currencyListDao = currencyDatabase.currencyListDao())

    @Provides
    @Singleton
    fun provideRemoteDataSource(mApi: CurrencyApi): CurrencyListRemoteDataSource =
        RetrofitCurrencyListDataSource(mApi)

    @Provides
    @Singleton
    fun provideCurrencyListRepository(
        local: CurrencyListLocalDataSource,
        remote: CurrencyListRemoteDataSource
    ): CurrencyListRepository =
        CurrencyListRepository(local, remote)

}