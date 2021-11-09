package com.hfad.currencycalculator

import com.google.android.material.circularreveal.CircularRevealHelper
import com.hfad.currencycalculator.model.local.models.CurrencyResponse
import com.hfad.currencycalculator.model.room.dao.CurrencyEntity
import com.hfad.currencycalculator.model.room.dao.CurrencyListEntity
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

@OneExecution
interface CurrencyView: MvpView {
    fun openCurrencyFragmentDialog(currencies:List<CurrencyListEntity>,numCode:String?)
    fun showLoad()
    fun showAll()
    fun changeRightCurrency(currency:CurrencyResponse?)
    fun changeLeftCurrency(currency:CurrencyResponse?)
}