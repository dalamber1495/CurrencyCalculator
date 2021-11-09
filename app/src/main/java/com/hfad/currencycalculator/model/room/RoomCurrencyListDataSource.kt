package com.hfad.currencycalculator.model.room

import android.provider.ContactsContract
import android.util.Log
import com.hfad.currencycalculator.model.local.CurrencyListLocalDataSource
import com.hfad.currencycalculator.model.remote.RemoteValuteResponce
import com.hfad.currencycalculator.model.room.dao.CurrencyEntity
import com.hfad.currencycalculator.model.room.dao.CurrencyListDao
import com.hfad.currencycalculator.model.room.dao.CurrencyListEntity
import io.reactivex.Completable
import io.reactivex.Single

class RoomCurrencyListDataSource(val currencyListDao: CurrencyListDao) :
    CurrencyListLocalDataSource {
    override fun loadAllCurrency(): Single<List<CurrencyListEntity>> =
        currencyListDao.loadAllCurrencies()

    override fun saveResponse(response: RemoteValuteResponce): Completable =
        currencyListDao.saveEntityCurrencies(
            CurrencyEntity(
                Date = response.Date,
                PreviousDate = response.PreviousDate,
                PreviousURL = response.PreviousURL,
                Timestamp = response.Timestamp
            )
        ).andThen(
            currencyListDao.addCurrency(
                CurrencyListEntity(
                    NumCode="1",
                    CharCode = "RUB",
                    Nominal = 1,
                    Name = "Российский рубль",
                    Value = 1.0,
                    Previous = 1.0
                )
            )
        ).andThen(currencyListDao.saveAllCurrencies(response.Valute.map {
            CurrencyListEntity(
                NumCode = it.value.NumCode,
                CharCode = it.value.CharCode,
                Nominal = it.value.Nominal,
                Name = it.value.Name,
                Value = it.value.Value,
                Previous = it.value.Previous
            )

        }))

    override fun deleteTables(): Completable =
        currencyListDao.clearCurrencyListTable().andThen(
            currencyListDao.clearCurrencyTable()
        )


}
