package com.hfad.currencycalculator.Di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.hfad.currencycalculator.MyApplication
import com.hfad.currencycalculator.base.CurrencyDatabase
import com.hfad.currencycalculator.model.local.CurrencyListLocalDataSource
import com.hfad.currencycalculator.model.local.CurrencyListRemoteDataSource
import com.hfad.currencycalculator.model.local.CurrencyListRepository
import com.hfad.currencycalculator.model.room.RoomCurrencyListDataSource
import com.hfad.currencycalculator.model.retrofit.RetrofitCurrencyListDataSource
import com.hfad.currencycalculator.network.CurrencyApi
import dagger.Binds
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